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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
@Transactional
public class JpqlUserRepositoryTest {

    @Autowired
    private JpqlUserRepository userRepository;

    @Test
    @Rollback(false)
    public void test01UserInsert(){
        User user = new User();
        user.setName( "둘리" );
        user.setPassword( "1234" );
        user.setEmail( "dooly@kickscar.me" );
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
        userRepository.save(user);

        assertEquals( 1L,  userRepository.count().longValue() );
    }

    @Test
    @Rollback(false)
    public void test02UserUpdate(){
        User user = new User();
        user.setNo(1L);
        user.setName("마이콜");
        user.setEmail("michol@kickscar.me");

        Boolean result = userRepository.update(user);
        assertTrue(result);
    }
}
