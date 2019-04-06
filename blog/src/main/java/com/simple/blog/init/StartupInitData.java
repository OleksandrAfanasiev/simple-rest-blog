package com.simple.blog.init;

import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import com.simple.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartupInitData {

    private UserService userService;

    @Autowired
    StartupInitData(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void createDefaultUsers() {
        userService.create(new User(0, "FirstUser", "firstPassword", Role.ROLE_PUBLISHER));
        userService.create(new User(0, "SecondUser", "secondPassword", Role.ROLE_PUBLISHER));
        userService.create(new User(0, "ThirdUser", "thirdPassword", Role.ROLE_PUBLISHER));
    }
}
