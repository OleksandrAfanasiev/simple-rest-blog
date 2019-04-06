package com.simple.blog.repository;

import com.simple.blog.entity.Message;
import com.simple.blog.entity.User;
import com.simple.blog.entity.core.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ITMessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private Message message1;

    private Message message2;

    private User user;

    @Before
    public void setup() {
        user = new User(0, "Name", "Password", Role.ROLE_PUBLISHER);
        message1 = new Message(0, "Text1", user);
        message2 = new Message(0, "Text2", user);

        entityManager.persist(user);
        entityManager.persist(message1);
        entityManager.persist(message2);
    }

    @Test
    public void whenFindByUserThenReturnMessage() {

        List<Message> messages = messageRepository.findByUser(user);

        assertThat(messages, hasSize(2));
        assertThat(messages, hasItems(message1, message2));
    }
}
