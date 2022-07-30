package com.ntokozodev.usertasksapi.controller;

import com.ntokozodev.usertasksapi.model.task.TaskRequest;
import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.model.task.UpdateTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserTaskController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/api/users/{id}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable("id") String userId, @RequestBody TaskRequest request) {
        LOG.info("[createTask] received request for userId: [{}], task: [{}]", userId, request.getName());
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @PutMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId, @RequestBody UpdateTaskRequest request) {
        LOG.info("[updateTask] received request for userId: [{}], taskId: [{}]", userId, taskId);
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @DeleteMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<Integer> deleteUserTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId) {
        LOG.info("[deleteTask] received delete request for userId: [{}], taskId: [{}]", userId, taskId);
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @GetMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<TaskResponse> getUserTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId) {
        LOG.info("[getTask] received request for userId: [{}], taskId: [{}]", userId, taskId);
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @GetMapping("/api/users/{user_id}/tasks")
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable("user_id") String userId) {
        LOG.info("[getUserTasks] received request for userId: [{}]", userId);
        throw new UnsupportedOperationException("Method not implemented yet.");
    }
}
