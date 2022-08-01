package com.ntokozodev.usertasksapi.service;

import com.ntokozodev.usertasksapi.exception.EntityDuplicateException;
import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.exception.ServiceException;
import com.ntokozodev.usertasksapi.model.db.User;
import com.ntokozodev.usertasksapi.model.user.UpdateUserRequest;
import com.ntokozodev.usertasksapi.model.user.UserRequest;
import com.ntokozodev.usertasksapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(UserRequest request) throws ServiceException, EntityDuplicateException {
        var infoMessage = "[createUser] creating user: { username: {}, name: {}, surname: {} }";
        LOG.info(infoMessage, request.getUsername(), request.getFirst_name(), request.getLast_name());

        try {
            var user = new User();
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

    public User updateUser(UpdateUserRequest request, long userId) throws ServiceException, EntityNotFoundException {
        LOG.info("[updateUser] updating user: {}", request);

        try {
            Optional<User> entity = repository.findById(userId);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException(String.format("No user found for Id [%s]", userId));
            }

            User user = entity.get();
            // For large objects could probably consider an auto mapper
            if (request.getFirst_name() != null) {
                user.setFirst_name(request.getFirst_name());
            }

            if (request.getLast_name() != null) {
                user.setLast_name(request.getLast_name());
            }

            return repository.save(user);
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw ex;
            }

            throw new ServiceException(String.format("Error updating user with Id [%s]", userId), ex);
        }
    }

    public User getUserById(long userId) throws ServiceException, EntityNotFoundException {
        LOG.info("[getUserById] retrieving user with Id: {}", userId);

        try {
            Optional<User> entity = repository.findById(userId);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException(String.format("No user found for Id [%s]", userId));
            }

            return entity.get();
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw ex;
            }

            throw new ServiceException(String.format("Error retrieving user with Id [%s]", userId), ex);
        }
    }

    public User getUserByUsername(String username) throws ServiceException, EntityNotFoundException {
        LOG.info("[getUserByUsername] retrieving user with username: {}", username);

        try {
            User user = repository.findByUsername(username);
            if (user == null) {
                throw new EntityNotFoundException(String.format("No user found for username [%s]", username));
            }

            return user;
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw ex;
            }

            throw new ServiceException(String.format("Error retrieving user with username [%s]", username), ex);
        }
    }

    // TODO: Paging
    public List<User> getAllUsers() throws ServiceException {
        LOG.info("[getAllUsers] retrieving users");

        try {
            List<User> users = new ArrayList<>();
            repository.findAll().forEach(users::add);
            return users;
        } catch (Exception ex) {
            throw new ServiceException("Error retrieving all users", ex);
        }
    }
}
