package com.simple.blog.init;

import com.simple.blog.Application;
import com.simple.blog.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ITStartupInitDataTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenAfterPropertiesSetInvokedThenDefaultDataCreated() {
        assertTrue(userRepository.existsByName("FirstUser"));
        assertTrue(userRepository.existsByName("SecondUser"));
        assertTrue(userRepository.existsByName("ThirdUser"));
    }
}
