package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.config.JpaConfig;
import me.kickscar.practices.jpa03.model08.domain.Blog;
import me.kickscar.practices.jpa03.model08.domain.User;
import me.kickscar.practices.jpa03.model08.repository.JpaBlogRepository;
import me.kickscar.practices.jpa03.model08.repository.JpaUserRepository;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public void test01SaveBlogs() {
        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리");
        user1.setPassword("1234");
        User user1Persisted = userRepository.save(user1);

        Blog blog1 = new Blog();
        blog1.setName("둘리의블로그");
        blog1.setUser(user1Persisted);
        blogRepository.save(blog1);
        System.out.println(em.contains(blog1));

        // =============================================

        User user2 = new User();
        user2.setId("ddochi");
        user2.setName("또치");
        user2.setPassword("1234");
        User user2Persisted = userRepository.save(user2);

        Blog blog2 = new Blog();
        blog2.setName("또치의블로그");
        blog2.setUser(user2Persisted);
        blogRepository.save(blog2);

        assertEquals(2L, userRepository.count());
        assertEquals(2L, blogRepository.count());
    }


}