package com.ntokozodev.usertasksapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.ntokozodev.usertasksapi.model.db.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
