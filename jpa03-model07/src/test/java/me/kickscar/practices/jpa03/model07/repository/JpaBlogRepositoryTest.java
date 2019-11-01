package me.kickscar.practices.jpa03.model07.repository;

import me.kickscar.practices.jpa03.model07.config.JpaConfig;
import me.kickscar.practices.jpa03.model07.domain.Blog;
import me.kickscar.practices.jpa03.model07.domain.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaBlogRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBlogRepository blogRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        Blog blog1 = new Blog();
        blog1.setName("둘리의블로그");
        blogRepository.save(blog1);

        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리");
        user1.setBlog(blog1);
        user1.setPassword("1234");
        userRepository.save(user1);

        Blog blog2 = new Blog();
        blog2.setName("또치의블로그");
        blogRepository.save(blog2);

        User user2 = new User();
        user2.setId("ddochi");
        user2.setName("또치");
        user2.setBlog(blog2);
        user2.setPassword("1234");
        userRepository.save(user2);

        Blog blog3 = new Blog();
        blog3.setName("주인없는블로그");
        blogRepository.save(blog3);

        User user3 = new User();
        user3.setId("michol");
        user3.setName("마이콜");
        user3.setPassword("1234");
        userRepository.save(user3);

        assertEquals(3L, userRepository.count());
        assertEquals(3L, blogRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02UpdateUser01(){
        Blog blog = blogRepository.findById(3L).get();
        User user = userRepository.findById("michol").get();

        blog.setUser(user);
    }

    @Test
    @Transactional
    public void test03VerifyUpdateUser01(){
        User user = userRepository.findById("michol").get();
        assertNull(user.getBlog());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test04UpdateUser02(){
        Blog blog = blogRepository.findById(3L).get();
        User user = userRepository.findById("michol").get();

        user.setBlog(blog);
    }

    @Test
    @Transactional
    public void test05VerifyUpdateUser02(){
        User user = userRepository.findById("michol").get();
        assertEquals("주인없는블로그", user.getBlog().getName());
    }

}