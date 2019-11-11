package me.kickscar.practices.jpa03.model12.repository;

import me.kickscar.practices.jpa03.model12.config.JpaConfig;
import me.kickscar.practices.jpa03.model12.domain.Book;
import me.kickscar.practices.jpa03.model12.domain.CartItem;
import me.kickscar.practices.jpa03.model12.domain.User;
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

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaCartItemRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private JpaCartItemRepository cartItemRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        User user1 = new User();
        user1.setName("둘리");
        user1.setEmail("dooly@gmail.com");
        user1.setPassword("1234");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("마이콜");
        user2.setEmail("michol@gmail.com");
        user2.setPassword("1234");
        userRepository.save(user2);

        Book book1 = new Book();
        book1.setTitle("책1");
        book1.setPrice(1000);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("책2");
        book2.setPrice(1000);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("책3");
        book3.setPrice(1000);
        bookRepository.save(book3);

        CartItem cartItem1 = new CartItem();
        cartItem1.setUser(user1);
        cartItem1.setBook(book1);
        cartItem1.setAmount(1);
        cartItemRepository.save(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setUser(user1);
        cartItem2.setBook(book2);
        cartItem2.setAmount(2);
        cartItemRepository.save(cartItem2);

        assertTrue(true);
    }

}