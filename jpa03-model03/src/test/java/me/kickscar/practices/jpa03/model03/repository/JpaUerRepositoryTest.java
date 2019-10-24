package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.config.JpaRepositoryTestConfig;
import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.RoleType;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserDto;
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
import static org.junit.Assert.assertTrue;

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
        User user = new User();
        user.setName("둘리");
        user.setPassword("1");
        user.setEmail("dooly@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
        userRepository.save(user);

        assertEquals( 1L,  userRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02Update() {
        User user = new User();
        user.setNo(1L);
        user.setEmail("dooly3@kickscar.me");
        user.setName("둘리3");
        user.setPassword("3");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        assertTrue(userRepository.update(user));
    }

}