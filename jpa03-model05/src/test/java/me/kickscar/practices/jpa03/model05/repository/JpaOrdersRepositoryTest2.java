package me.kickscar.practices.jpa03.model05.repository;

import static org.junit.Assert.assertNotEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import me.kickscar.practices.jpa03.model05.domain.Orders;
import me.kickscar.practices.jpa03.model05.domain.User;

public class JpaOrdersRepositoryTest2 {
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaOrdersRepository orderRepository;


    public void test01Save() {
        User user1 = new User();
        user1.setName("둘리");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("마이콜");
        userRepository.save(user2);

        
        orderRepository.save(1L, new Orders("주문1"), new Orders("주문2"), new Orders("주문3"));
    }


    public void test02UpdateUser() {
        Orders orders = orderRepository.findById(1L).get();

        User user = userRepository.findById(2L).get();
        orders.setUser(user);
    }

    
    public void test03UpdateUserResultFails() {
        Orders orders = orderRepository.findById(1L).get();
        assertNotEquals("마이콜",orders.getUser().getName());
    }
}