package com.simple.blog.service.impl;

import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import com.simple.blog.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private User user;

    private static final String USERNAME = "Name";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        user = new User(1, USERNAME, "Password", Role.ROLE_PUBLISHER);
    }

    @Test
    public void whenCreateInvoked() {
        userService.create(user);

        verify(userRepository).saveAndFlush(user);
    }

    @Test
    public void whenIsExistsByNameInvoked() {
        userService.isExists(USERNAME);

        verify(userRepository).existsByName(USERNAME);
    }

    @Test
    public void whenGetByNameInvoked() {
        when(userService.getByName(USERNAME)).thenReturn(Optional.ofNullable(user));

        userService.getByName(USERNAME);

        verify(userRepository).findByName(USERNAME);
    }

    @Test
    public void whenLoadByEmailThenGetUserDetailsWithRightUserName() {
        when(userRepository.findByName(USERNAME)).thenReturn(Optional.ofNullable(user));

        UserDetails userDetails = userService.loadUserByUsername(USERNAME);

        assertEquals(userDetails.getUsername(), USERNAME);
    }

}
