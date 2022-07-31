package com.ntokozodev.usertasksapi.service;

import com.ntokozodev.usertasksapi.exception.EntityDuplicateException;
import com.ntokozodev.usertasksapi.exception.ServiceException;
import com.ntokozodev.usertasksapi.model.db.User;
import com.ntokozodev.usertasksapi.model.user.UserRequest;
import com.ntokozodev.usertasksapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(UserRequest request) throws ServiceException, EntityDuplicateException {
        String infoMessage = "[createUser] creating user: { username: {}, name: {}, surname: {} }";
        LOG.info(infoMessage, request.getUsername(), request.getFirst_name(), request.getLast_name());

        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setFirst_name(request.getFirst_name());
            user.setLast_name(request.getLast_name());

            return repository.save(user);
        } catch (DataIntegrityViolationException ex) {
            var message = String.format("User with username [%s] already exists", request.getUsername());
            throw new EntityDuplicateException(message, ex);
        } catch (Exception ex) {
            LOG.info("[createUser] failed with username: [{}]", request.getUsername());
            throw new ServiceException(String.format("Error creating user [%s]", request.getUsername()), ex);
        }
    }
}
