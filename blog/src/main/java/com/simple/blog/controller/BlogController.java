package com.simple.blog.controller;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;
import com.simple.blog.entity.dto.MessageDto;
import com.simple.blog.service.MessageService;
import com.simple.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private UserService userService;

    private MessageService messageService;

    @Autowired
    BlogController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAll();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable("id") long id) {
        return messageService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No message with id: '%s'.", id)));
    }

    @GetMapping("/myMessages")
    public List<Message> getUsersMessages() {
        User user = getUserFromRequest();
        return messageService.getAllByUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@RequestBody String text) {
        User user = getUserFromRequest();
        Message message = new Message(0, text, user);
        return messageService.create(message);
    }

    @PutMapping
    public boolean updateMessage(@RequestBody MessageDto messageDto) {
        User user = getUserFromRequest();
        Message message = messageService.getById(messageDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such message with id: '%d'.", messageDto.getId())));
        message.setText(messageDto.getText());
        if (user.equals(message.getUser())) {
            return messageService.update(message);
        }
        return false;
    }

    @DeleteMapping
    public boolean deleteMessage(@RequestBody long id) {
        User user = getUserFromRequest();
        Message message = messageService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such message with id: '%d'.", id)));
        if (user.equals(message.getUser())) {
            return messageService.delete(message);
        }
        return false;
    }

    private User getUserFromRequest() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        return userService.getByName(name)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such user with name: '%s'.", name)));
    }
}