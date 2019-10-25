package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.config.JpaRepositoryTestConfig;
import me.kickscar.practices.jpa03.model04.domain.GenderType;
import me.kickscar.practices.jpa03.model04.domain.RoleType;
import me.kickscar.practices.jpa03.model04.domain.User;
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
@SpringBootTest(classes = {JpaRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUerRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save(){
        User user1 = new User();
        user1.setName("둘리");
        user1.setPassword("1234");
        user1.setEmail("dooly@kickscar.me");
        user1.setGender(GenderType.MALE);
        user1.setRole(RoleType.USER);
        userRepository.save(user1);


        //================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);


        //================================

        User user3 = new User();
        user3.setName("또치");
        user3.setPassword("1234");
        user3.setEmail("ddochi@kickscar.me");
        user3.setGender(GenderType.MALE);
        user3.setRole(RoleType.USER);
        userRepository.save(user3);

    }



}