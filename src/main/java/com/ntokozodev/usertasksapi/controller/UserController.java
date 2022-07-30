package com.ntokozodev.usertasksapi.controller;

import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.model.user.UpdateUserRequest;
import com.ntokozodev.usertasksapi.model.user.UserRequest;
import com.ntokozodev.usertasksapi.model.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) throws Exception {
        LOG.info("[createUser] received request for username: [{}]", request.getUsername());
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") String userId, @RequestBody UpdateUserRequest request) throws Exception {
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
