package com.ntokozodev.usertasksapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.db.User;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUser(User user);
}
