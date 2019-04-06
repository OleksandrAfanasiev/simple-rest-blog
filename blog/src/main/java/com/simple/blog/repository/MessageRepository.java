package com.simple.blog.repository;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUser(User user);
}
