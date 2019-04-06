package com.simple.blog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.blog.entity.Message;

import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import com.simple.blog.entity.dto.MessageDto;
import com.simple.blog.service.MessageService;
import com.simple.blog.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogController.class)
@AutoConfigureMockMvc(secure = false)
public class BlogControllerTest {

    private static final String URL = "/blog";

    private static final long CORRECT_ID = 1;

    private static final long INCORRECT_ID = 2;

    private static final String USERNAME = "Name";

    private User user;

    private Message message;

    @Autowired
    private MockMvc server;

    @MockBean
    UserService userService;

    @MockBean
    MessageService messageService;

    @Mock
    SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @Mock
    Authentication authentication;

    @Before
    public void setUp() {
        user = new User();
        user.setId(CORRECT_ID);
        user.setName(USERNAME);
        user.setRole(Role.ROLE_PUBLISHER);

        message = new Message();
        message.setId(CORRECT_ID);
        message.setText("Text");
        message.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userService.getByName(USERNAME)).thenReturn(Optional.ofNullable(user));
    }

    @Test
    public void getAllInvoked() throws Exception {
        when(messageService.getAll()).thenReturn(Collections.singletonList(message));
        server
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(Collections.singleton(message))));

        verify(messageService, times(1)).getAll();
    }

    @Test
    public void successWhenGetByIdOfExistingMessage() throws Exception {
        when(messageService.getById(CORRECT_ID)).thenReturn(Optional.ofNullable(message));
        server
                .perform(get(URL.concat("/").concat(String.valueOf(CORRECT_ID))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(message)));

        verify(messageService, times(1)).getById(CORRECT_ID);
    }

    @Test
    public void failedWhenGetByIdOfNotExistingMessage() throws Exception {
        when(messageService.getById(INCORRECT_ID)).thenReturn(Optional.empty());
        server
                .perform(get(URL.concat("/").concat(String.valueOf(INCORRECT_ID))))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(messageService, times(1)).getById(INCORRECT_ID);
    }

    @Test
    public void getUserMessagesInvoked() throws Exception {
        when(messageService.getAllByUser(user)).thenReturn(Collections.singletonList(message));

        server
                .perform(get(URL.concat("/myMessages")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(Collections.singleton(message))));

        verify(messageService, times(1)).getAllByUser(user);
        verify(userService, times(1)).getByName(USERNAME);
    }

    @Test
    public void createMessageInvoked() throws Exception {
        Message newMessage = new Message(0, "text", user);
        when(messageService.create(newMessage)).thenReturn(message);

        server
                .perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("text"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(message)));

        verify(messageService, times(1)).create(newMessage);
        verify(userService, times(1)).getByName(USERNAME);
    }

    @Test
    public void updateMessageInvoked() throws Exception {
        MessageDto messageDto = new MessageDto(CORRECT_ID, "text");
        message.setText(messageDto.getText());
        when(messageService.update(message)).thenReturn(true);
        when(messageService.getById(CORRECT_ID)).thenReturn(Optional.ofNullable(message));

        server
                .perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(messageDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(messageService, times(1)).update(message);
        verify(userService, times(1)).getByName(USERNAME);
    }

    @Test
    public void successWhenDeleteMessageWithCorrectId() throws Exception {
        when(messageService.getById(CORRECT_ID)).thenReturn(Optional.ofNullable(message));
        when(messageService.delete(message)).thenReturn(true);

        server
                .perform(delete(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CORRECT_ID)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(messageService, times(1)).getById(CORRECT_ID);
        verify(messageService, times(1)).delete(message);
        verify(userService, times(1)).getByName(USERNAME);
    }

    @Test
    public void failedWhenDeleteMessageWithIncorrectId() throws Exception {
        when(messageService.getById(INCORRECT_ID)).thenReturn(Optional.empty());

        server
                .perform(delete(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(INCORRECT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(messageService, times(1)).getById(INCORRECT_ID);
        verify(userService, times(1)).getByName(USERNAME);
    }
}
