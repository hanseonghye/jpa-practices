package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.conf.JpaRepositoryTestConfig;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
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
    public void test02FindByEmailAndPassword(){
        User user = userRepository.findByEmailAndPassword("dooly@kickscar.me", "1");
        System.out.println(user);
        assertEquals("둘리", user.getName());
        assertFalse(em.contains(user));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test03FindByIdAndSave() {  // 영속객체 수정하기(select + update)
        User user = userRepository.findById(1L).get();
        System.out.println(user);
        user.setName("둘리2");
        user.setPassword("2");
        user.setEmail("dooly2@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
    }

    @Test
    public void test04FindById(){
        User user = userRepository.findById(1L).get();
        assertEquals("둘리2", user.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test05Update() { //수정하기(update query만 실행됨)
        userRepository.update(1L, "둘리3", "dooly3@kickscar.me", "3", GenderType.MALE, RoleType.USER);
    }

    @Test
    public void test06FindById(){
        User user = userRepository.findById(1L).get();
        assertEquals("둘리3", user.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test07Update() { //수정하기(update query만 실행됨)
        User user = new User();
        user.setNo(1L);
        user.setName("둘리4");
        user.setPassword("4");
        user.setEmail("dooly4@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        userRepository.update(user);
    }

    @Test
    public void test08FindById(){
        User user = userRepository.findById(1L).get();
        assertEquals("둘리4", user.getName());
    }


}
