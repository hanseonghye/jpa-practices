package me.kickscar.practices.jpa03.model03.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import me.kickscar.practices.jpa03.model03.config.JpaConfig;
import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUserRepositoryTest02 {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaOrdersRepository ordersRepository;


    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        final User user1 = new User();
        user1.setName("둘리");

        final User user1Persisted = userRepository.save(user1);

        final Orders orders1 = new Orders();
        orders1.setName("주문1");
        orders1.setUser(user1Persisted);
        ordersRepository.save(orders1);
    }


    @Test
    @Transactional
    @Rollback(false)
    public void test02Update() {
        final User user = new User();
        user.setNo(1L);
        user.setName("둘리2");
        assertTrue(userRepository.update(user));
    }


    @Test
    @Transactional
    public void test03OneToManyCollectionJoinProblem() {
        assertEquals(ordersRepository.count(), userRepository.findAllCollectionJoinProblem().size());
    }


    public void test04() {
        userRepository.findAllCollectionJoinProblemSolved().size();
    }
}