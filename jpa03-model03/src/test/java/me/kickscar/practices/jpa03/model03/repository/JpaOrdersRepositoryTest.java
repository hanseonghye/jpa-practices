package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.config.JpaConfig;
import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.domain.RoleType;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.OrderCountOfUserDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaOrdersRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaOrdersRepository orderRepository;

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

        Orders orders1 = new Orders();
        orders1.setName("주문1");
        orders1.setUser(user1);
        orderRepository.save(orders1);

        Orders orders2 = new Orders();
        orders2.setName("주문2");
        orders2.setUser(user1);
        orderRepository.save(orders2);

        Orders orders3 = new Orders();
        orders3.setName("주문3");
        orders3.setUser(user1);
        orderRepository.save(orders3);

        Orders orders4 = new Orders();
        orders4.setName("주문4");
        orders4.setUser(user1);
        orderRepository.save(orders4);

        Orders orders5 = new Orders();
        orders5.setName("주문5");
        orders5.setUser(user1);
        orderRepository.save(orders5);

        //================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);

        Orders orders6 = new Orders();
        orders6.setName("주문6");
        orders6.setUser(user2);
        orderRepository.save(orders6);

        Orders orders7 = new Orders();
        orders7.setName("주문7");
        orders7.setUser(user2);
        orderRepository.save(orders7);

        //================================

        User user3 = new User();
        user3.setName("또치");
        user3.setPassword("1234");
        user3.setEmail("ddochi@kickscar.me");
        user3.setGender(GenderType.MALE);
        user3.setRole(RoleType.USER);
        userRepository.save(user3);

        Orders orders8 = new Orders();
        orders8.setName("주문6");
        orders8.setUser(user3);
        orderRepository.save(orders8);

        assertEquals(8L, orderRepository.count());
    }

    @Test
    public void test02FindAllByUserNo() {
        final Long userNo = 1L;

        List<Orders> list = orderRepository.findAllByUserNo(userNo);

        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(list));
        assertEquals(orderRepository.countAllByUserNo(userNo).longValue(), list.size());
    }

    @Test
    public void test03FindAllByUserNo() {
        final Long userNo = 1L;
        List<Orders> list = orderRepository.findAllByUserNo(userNo, new Sort(Sort.Direction.DESC, "regDate"));

        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(list));
        assertEquals(orderRepository.countAllByUserNo(userNo).longValue(), list.size());
    }

    @Test
    public void test04FindAllByUserNo2() {
        final Long userNo = 1L;
        List<Orders> list = orderRepository.findAllByUserNo2(userNo);

        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(list));
        assertEquals(orderRepository.countAllByUserNo(userNo).longValue(), list.size());
    }

    @Test
    public void test05FindAllByUserNo2() {
        final Long userNo = 1L;
        List<Orders> list = orderRepository.findAllByUserNo2(userNo, new Sort(Sort.Direction.DESC, "regDate").and(new Sort(Sort.Direction.DESC, "totalPrice")));

        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(list));
        assertEquals(orderRepository.countAllByUserNo(userNo).longValue(), list.size());
    }

    @Test
    public void test06FindAllByUserNo2() {
        Integer page = 0;
        final Integer size = 3;
        final Long userNo = 1L;

        List<Orders> list = orderRepository.findAllByUserNo2(userNo, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(3L, list.size());

        list = orderRepository.findAllByUserNo2(userNo, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(2L, list.size());

        list = orderRepository.findAllByUserNo2(userNo, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(0L, list.size());
    }

    @Test
    public void test07CountAllGroupByUser() {
        Long totalOrdersCount = 0L;
        final Long totalOrdersCountExpected = orderRepository.count();

        List<OrderCountOfUserDto> list = orderRepository.countAllGroupByUser();
        for(OrderCountOfUserDto dto: list){
            totalOrdersCount += dto.getOrderCount();
        }

        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(list));
        assertEquals(totalOrdersCountExpected, totalOrdersCount);
    }
}