package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.config.JpaRepositoryTestConfig;
import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.Order;
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
import sun.lwawt.macosx.CSystemTray;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;

import java.util.List;

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

    @Autowired
    private JpaOrderRepository orderRepository;

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

        Order Order1 = new Order();
        Order1.setName("주문1");
        Order1.setUser(user1);
        orderRepository.save(Order1);

        Order Order2 = new Order();
        Order2.setName("주문2");
        Order2.setUser(user1);
        orderRepository.save(Order2);

        Order Order3 = new Order();
        Order3.setName("주문3");
        Order3.setUser(user1);
        orderRepository.save(Order3);

        Order Order4 = new Order();
        Order4.setName("주문4");
        Order4.setUser(user1);
        orderRepository.save(Order4);

        Order Order5 = new Order();
        Order5.setName("주문5");
        Order5.setUser(user1);
        orderRepository.save(Order5);

        //================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);

        Order Order6 = new Order();
        Order6.setName("주문6");
        Order6.setUser(user2);
        orderRepository.save(Order6);

        Order Order7 = new Order();
        Order7.setName("주문7");
        Order7.setUser(user2);
        orderRepository.save(Order7);

        //================================

        User user3 = new User();
        user3.setName("또치");
        user3.setPassword("1234");
        user3.setEmail("ddochi@kickscar.me");
        user3.setGender(GenderType.MALE);
        user3.setRole(RoleType.USER);
        userRepository.save(user3);

        Order Order8 = new Order();
        Order8.setName("주문6");
        Order8.setUser(user3);
        orderRepository.save(Order8);

        assertEquals(8L, orderRepository.count());
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

    @Test
    @Transactional
    public void test03OneToManyCollectionJoinProblem() {
        assertEquals(orderRepository.count(), userRepository.findAllCollectionJoinProblem().size());
    }

    @Test
    @Transactional
    public void test04OCollectionJoinProblemSolved() {
        assertEquals(userRepository.count(), userRepository.findAllCollectionJoinProblemSolved().size());
    }

    @Test
    @Transactional
    public void test05NplusOneProblem() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = orderRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAll();

        for(User user : users) {
            List<Order> orders = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                qryCount++;
            }
            ordersCountActual += orders.size();

        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(N+1, qryCount.longValue());
    }

    @Test
    @Transactional
    public void test06NplusOneProblemNotSolvedYet() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = orderRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAllCollectionJoinProblemSolved();

        for(User user : users) {
            List<Order> orders = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                qryCount++;
            }
            ordersCountActual += orders.size();
        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(N+1, qryCount.longValue());
    }

    @Test
    @Transactional
    public void test07NplusOneProblemSolved() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = orderRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAllCollectionJoinAndNplusOneProblemSolved();

        for(User user : users) {
            List<Order> orders = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                qryCount++;
            }
            ordersCountActual += orders.size();
        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(1, qryCount.longValue());
    }

}