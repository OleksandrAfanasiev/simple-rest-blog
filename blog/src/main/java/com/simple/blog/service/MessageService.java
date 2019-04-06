package com.simple.blog.service;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message create(Message message);

    boolean update(Message message);

    boolean delete(Message message);

    List<Message> getAll();

    Optional<Message> getById(long id);

    List<Message> getAllByUser(User user);
}
