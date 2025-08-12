package com.demo.library.service;

import com.demo.library.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    List<User> findAll(Specification<User> spec);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User createUser(User user);
    User updateUser(Long id, User user);
    boolean deleteUser(Long id);
}