package com.ntokozodev.usertasksapi.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.exception.ServiceException;
import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.db.User;
import com.ntokozodev.usertasksapi.model.task.TaskRequest;
import com.ntokozodev.usertasksapi.model.task.UpdateTaskRequest;
import com.ntokozodev.usertasksapi.repository.TaskRepository;
import com.ntokozodev.usertasksapi.repository.UserRepository;

@Service
public class TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskRequest request, long userId) throws ServiceException, EntityNotFoundException {
        var infoMessage = "[createTask] creating task: { name: {}, description: {}, date_time: {} }";
        LOG.info(infoMessage, request.getName(), request.getDescription(), request.getDate_time());

        try {
            Optional<User> userEntity = userRepository.findById(userId);
            if (userEntity.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Couldn't create task no user found for Id [%s]", userId));
            }

            var task = new Task();
            task.setName(request.getName());
            task.setDescription(request.getDescription());
            task.setDate_time(request.getDate_time());
            task.setUser(userEntity.get());

            return taskRepository.save(task);
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw ex;
            }

            throw new ServiceException(String.format("Error creating task [%s]", request.getName()), ex);
        }
    }

    public Task updateUserTask(UpdateTaskRequest request, long userId, long taskId)
            throws ServiceException, EntityNotFoundException {
        LOG.info("[updateUser] updating task with userId: [{}], taskId: [{}]", userId, taskId);

        try {
            Optional<Task> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Couldn't update task no task found for Id [%s]", taskId));
            }

            var task = taskEntity.get();
            if (task.getUser().getId() != userId) {
                throw new ServiceException(String.format("Invalid user for given taskId: [%s]", taskId));
            }

            if (request.getName() != null) {
                task.setName(request.getName());
            }

            if (request.getDescription() != null) {
                task.setDescription(request.getDescription());
            }

            if (request.getDate_time() != null) {
                task.setDate_time(request.getDate_time());
            }

            return taskRepository.save(task);

        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException || ex instanceof ServiceException) {
                throw ex;
            }

            throw new ServiceException(
                    String.format("Error updating task with userId [%s], taskId [%s]", userId, taskId), ex);
        }
    }

    public Task getUserTask(long userId, long taskId)
            throws ServiceException, EntityNotFoundException, IllegalArgumentException {
        LOG.info("[getUserTask] retrieving task taskId: [{}] for userId: [{}]", taskId, userId);

        try {
            Optional<Task> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) {
                throw new EntityNotFoundException(String.format("No task found for Id [%s]", taskId));
            }

            var task = taskEntity.get();
            if (task.getUser().getId() != userId) {
                throw new IllegalArgumentException(String.format("Invalid user for given taskId: [%s]", taskId));
            }

            return task;

        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException || ex instanceof IllegalArgumentException) {
                throw ex;
            }

            throw new ServiceException(
                    String.format("Error retrieving task with userId [%s] - taskId [%s]", userId, taskId), ex);
        }
    }

    public List<Task> getAllUserTasks(long userId) throws ServiceException {
        LOG.info("[getAllUserTasks] request for Id: [{}]", userId);

        try {
            Optional<User> userEntity = userRepository.findById(userId);
            if (userEntity.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Couldn't retrieve tasks, no user found for Id [%s]", userId));
            }

            return taskRepository.findByUser(userEntity.get());

        } catch (Exception ex) {
            throw new ServiceException("Error retrieving all user tasks", ex);
        }
    }

    public void deleteUserTask(long userId, long taskId)
            throws ServiceException, EntityNotFoundException, IllegalArgumentException {
        LOG.info("[deleteUserTask] deleting task taskId: [{}] for userId: [{}]", taskId, userId);

        try {
            Optional<Task> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) {
                throw new EntityNotFoundException(String.format("No task found for Id [%s]", taskId));
            }

            var task = taskEntity.get();
            if (task.getUser().getId() != userId) {
                throw new IllegalArgumentException(String.format("Invalid user for given taskId: [%s]", taskId));
            }

            taskRepository.delete(task);

        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException || ex instanceof IllegalArgumentException) {
                throw ex;
            }

            throw new ServiceException(
                    String.format("Error deleting task with userId [%s] - taskId [%s]", userId, taskId), ex);
        }
    }
}
