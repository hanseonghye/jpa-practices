package me.kickscar.practices.jpa03.model07.repository;

import me.kickscar.practices.jpa03.model07.config.JpaConfig;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUserRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save(){
        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리");
        user1.setPassword("1234");
        userRepository.save(user1);

        assertEquals(1L, userRepository.count());
   }
}