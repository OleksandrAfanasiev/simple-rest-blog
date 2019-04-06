package com.simple.blog.service.impl;

import com.simple.blog.entity.User;
import com.simple.blog.repository.UserRepository;
import com.simple.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        if (isExists(user.getName())) {
            throw new EntityExistsException(String.format("User with name: '%s' already exists.", user.getName()));
        }
        if (user.getId() == 0) {
            encodePassword(user);
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    public boolean isExists(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    public Optional<User> getByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such user with name: '%s'.", username)));

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString())
        );
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
