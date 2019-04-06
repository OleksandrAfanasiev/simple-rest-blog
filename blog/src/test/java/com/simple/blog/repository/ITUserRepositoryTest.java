package com.simple.blog.repository;

import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ITUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private static final String USERNAME = "Name";

    @Before
    public void setup() {
        user = new User(0, USERNAME, "Password", Role.ROLE_PUBLISHER);
        entityManager.persist(user);
    }

    @Test
    public void whenFindByNameThenReturnUser() {

        Optional<User> optionalUser = userRepository.findByName(USERNAME);

        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }

    @Test
    public void whenExistsByNameInvoked() {
        assertTrue(userRepository.existsByName(USERNAME));
    }
}
