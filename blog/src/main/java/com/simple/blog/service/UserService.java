package com.simple.blog.service;

import com.simple.blog.entity.User;

import java.util.Optional;

public interface UserService {
    User create(User user);

    boolean isExists(String name);

    Optional<User> getByName(String name);
}
