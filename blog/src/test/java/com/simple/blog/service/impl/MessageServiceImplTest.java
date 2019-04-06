package com.simple.blog.service.impl;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import com.simple.blog.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {

    private static final long CORRECT_ID = 1L;

    private Message message;

    private User user;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Before
    public void setUp() {
        user = new User(CORRECT_ID, "Name", "Password", Role.ROLE_PUBLISHER);
        message = new Message(CORRECT_ID, "Text", user);
    }

    @Test
    public void whenCreateInvoked() {
        messageService.create(message);

        verify(messageRepository).saveAndFlush(message);
    }

    @Test
    public void whenUpdateInvoked() {
        when(messageRepository.existsById(CORRECT_ID)).thenReturn(true);

        messageService.update(message);

        verify(messageRepository).saveAndFlush(message);
    }

    @Test
    public void whenDeleteMessageByIdInvoked() {
        when(messageRepository.existsById(CORRECT_ID)).thenReturn(true);

        messageService.delete(message);

        verify(messageRepository).delete(message);
    }

    @Test
    public void whenDeleteMessageByIdInvokedForNotExistingMessage() {
        when(messageRepository.existsById(CORRECT_ID)).thenReturn(false);

        messageService.delete(message);

        verify(messageRepository).existsById(CORRECT_ID);
    }

    @Test
    public void whenGetAllInvoked() {
        messageService.getAll();

        verify(messageRepository).findAll();
    }

    @Test
    public void whenGetByIdInvoked() {
        when(messageService.getById(CORRECT_ID)).thenReturn(Optional.ofNullable(message));

        messageService.getById(CORRECT_ID);

        verify(messageRepository).findById(CORRECT_ID);

    }

    @Test
    public void whenGetAllByUserInvoked() {
        when(messageService.getAllByUser(user)).thenReturn(Collections.singletonList(message));

        messageService.getAllByUser(user);

        verify(messageRepository).findByUser(user);
    }
}
