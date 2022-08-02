package com.ntokozodev.usertasksapi.controller;

import java.util.ArrayList;
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
import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.model.db.User;
import com.ntokozodev.usertasksapi.model.user.UpdateUserRequest;
import com.ntokozodev.usertasksapi.model.user.UserRequest;
import com.ntokozodev.usertasksapi.model.user.UserResponse;
import com.ntokozodev.usertasksapi.service.UserService;

import javax.validation.Valid;

import static com.ntokozodev.usertasksapi.util.Util.logException;
import static com.ntokozodev.usertasksapi.util.Util.parseId;

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
    public ResponseEntity<String> createUser(@Valid  @RequestBody UserRequest request) {
        LOG.info("[createUser] received request for username: [{}]", request.getUsername());

        try {
            var user = userService.createUser(request);
            var response = createResponse(user);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
        } catch (EntityDuplicateException ex) {
            logException("createUser", ex);
            return new ResponseEntity<>(String.format("User with username [%s] already exists", request.getUsername()),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            logException("createUser", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") String id, @RequestBody UpdateUserRequest request) {
        LOG.info("[updateUser] received request for userId: [{}]", id);

        try {
            var user = userService.updateUser(request, parseId(id));
            var response = createResponse(user);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logException("updateUser", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException ex) {
            logException("updateUser", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("updateUser", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<String> getUserById(@PathVariable("id") String id) {
        LOG.info("[getUser] received request for Id: [{}]", id);

        try {
            var userId = parseId(id);
            var user = userService.getUserById(userId);
            var response = createResponse(user);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getUserById", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            logException("getUserById", ex);
            return new ResponseEntity<>("Invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {
            logException("getUserById", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{username}/username")
    public ResponseEntity<String> getUserByUsername(@PathVariable("username") String username) {
        LOG.info("[getUserByUsername] received request for username: [{}]", username);

        try {
            var user = userService.getUserByUsername(username);
            var response = createResponse(user);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getUserByUsername", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("getUserByUsername", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        LOG.info("[getUsers] request");

        try {
            var responses = new ArrayList<UserResponse>();
            var users = userService.getAllUsers();
            users.forEach(user -> responses.add(createResponse(user)));

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            logException("getUsers", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserResponse createResponse(User user) {
        var response = new UserResponse();
        response.setId(user.getId());
        response.setFirst_name(user.getFirst_name());
        response.setLast_name(user.getLast_name());
        response.setUsername(user.getUsername());

        return response;
    }
}
