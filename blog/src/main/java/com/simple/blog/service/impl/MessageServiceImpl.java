package com.simple.blog.service.impl;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;
import com.simple.blog.repository.MessageRepository;
import com.simple.blog.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Autowired
    MessageServiceImpl(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Override
    public boolean update(Message message) {
        if (messageRepository.existsById(message.getId())) {
            messageRepository.saveAndFlush(message);
            return true;
        }
        log.error("No such message with id: '%d'.", message.getId());
        return false;
    }

    @Override
    public boolean delete(Message message) {
        if (messageRepository.existsById(message.getId())) {
            messageRepository.delete(message);
            return true;
        }
        log.error("No such message with id: '%d'.", message.getId());
        return false;
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> getById(long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getAllByUser(User user) {
        return messageRepository.findByUser(user);
    }
}
