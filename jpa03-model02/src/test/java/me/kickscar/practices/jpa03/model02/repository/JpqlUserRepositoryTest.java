package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.conf.JpqlRepositoryTestConfig;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
public class JpqlUserRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpqlUserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01UserInsert(){
        User user = new User();
        user.setName("둘리");
        user.setPassword("1");
        user.setEmail("dooly@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
        userRepository.save(user);

        assertEquals( 1L,  userRepository.count().longValue() );
    }

    @Test
    public void test02UserFetchOnePersisted(){
        User user = userRepository.find("dooly@kickscar.me", "1");
        assertFalse(em.contains(user));

        System.out.println(user);
        user.setEmail(".......");
    }

    @Test
    //@Transactional
    @Rollback(false)
    public void test03UserUpdatePersisted(){
        User user = new User();
        user.setNo(1L);
        user.setName("둘리2");
        user.setPassword("2");
        user.setEmail("dooly2@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        //User userPersist =
                userRepository.update(user);
        //assertTrue(em.contains(userPersist));
    }

    @Test
    public void test04UserFetchOne(){
        User user = userRepository.find(1L);
        assertTrue(true);
    }

}
