package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.config.JpqlRepositoryTestConfig;
import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.Order;
import me.kickscar.practices.jpa03.model03.domain.RoleType;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserOrderCountDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
@Transactional
public class JpqlOrderRepositoryTest {

    @Autowired
    private JpqlUserRepository userRepository;

    @Autowired
    private JpqlOrderRepository orderRepository;

    @Test
    @Rollback(false)
    public void test01OrderInsert() {
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

        assertEquals(8L, orderRepository.count().longValue());
    }

    @Test
    public void test02OrdersFetchList() {
        Long userNo = 1L;

        List<Order> list = orderRepository.findAll(userNo);
        for(Order o : list){
            System.out.println(o);
        }

        long expected = 5L;
        assertEquals(expected, orderRepository.count(userNo).longValue());
    }

    @Test
    public void test03OrdersFetchList() {
        Long userNo = 1L;
        Integer page = 1;

        List<Order> list1 = orderRepository.findAll2(userNo, page++);
        for(Order o : list1){
            System.out.println(o);
        }
        assertEquals(3, list1.size());

        List<Order> list2 = orderRepository.findAll2(userNo, page++);
        for(Order o : list2){
            System.out.println(o);
        }
        assertEquals(2, list2.size());

        List<Order> list3 = orderRepository.findAll2(userNo, page);
        for(Order o : list3){
            System.out.println(o);
        }
        assertEquals(0, list3.size());
    }

    @Test
    public void test04OrderFetchPagingList() {
        Long userNo = 1L;
        Integer page = 1;

        List<Order> list1 = orderRepository.findAll3(userNo, page++);
        for(Order o : list1){
            System.out.println(o);
        }
        assertEquals(3, list1.size());

        List<Order> list2 = orderRepository.findAll3(userNo, page++);
        for(Order o : list2){
            System.out.println(o);
        }
        assertEquals(2, list2.size());

        List<Order> list3 = orderRepository.findAll3(userNo, page);
        for(Order o : list3){
            System.out.println(o);
        }
        assertEquals(0, list3.size());
    }

    @Test
    public void test05UserOrderCountFetchList() {
        List<UserOrderCountDto> list = orderRepository.countsOfEachUser();
        for(UserOrderCountDto dto: list){
            System.out.println(dto);
        }

        assertTrue( true );
    }

}
