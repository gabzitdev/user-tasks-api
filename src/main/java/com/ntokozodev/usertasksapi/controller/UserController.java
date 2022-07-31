package com.ntokozodev.usertasksapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntokozodev.usertasksapi.exception.EntityDuplicateException;
import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.model.user.UpdateUserRequest;
import com.ntokozodev.usertasksapi.model.user.UserRequest;
import com.ntokozodev.usertasksapi.model.user.UserResponse;
import com.ntokozodev.usertasksapi.service.UserService;

@RestController
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ObjectMapper mapper;

    public UserController(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest request) {
        LOG.info("[createUser] received request for username: [{}]", request.getUsername());

        try {
            var user = userService.createUser(request);
            var response = new UserResponse();

            response.setId(user.getId());
            response.setFirst_name(user.getFirst_name());
            response.setLast_name(user.getLast_name());
            response.setUsername(user.getUsername());

            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            var body = mapper.writeValueAsString(response);
            
            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
        } catch (EntityDuplicateException ex) {
            var message = String.format("User with username [%s] already exists", request.getUsername());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            var customMessage = "[createUser] exception: { message: {}, type: {} }";
            LOG.error(customMessage, ex.getMessage(), ex.getClass().getCanonicalName());
            LOG.error("[createUser] exception", ex);

            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") String userId, @RequestBody UpdateUserRequest request) {
        LOG.info("[updateUser] received request for userId: [{}], username: [{}]", userId, request.getUsername());
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<TaskResponse> getUser(@PathVariable("id") String userId) {
        LOG.info("[getUser] received request for userId: [{}]", userId);
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        LOG.info("[getUsers] received request");
        throw new UnsupportedOperationException("Method not implemented yet.");
    }
}
