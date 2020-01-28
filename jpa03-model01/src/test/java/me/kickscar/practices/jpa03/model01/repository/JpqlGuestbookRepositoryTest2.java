package me.kickscar.practices.jpa03.model01.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;

public class JpqlGuestbookRepositoryTest2 {
    @Autowired
    private JpqlGuestbookRepository2  guestbookRepository;


    public void test01Save() {
        Guestbook gb1 = new Guestbook();
        gb1.setName("name1");
        gb1.setPassword("1234");
        guestbookRepository.save(gb1);
        

        Guestbook gb2 = new Guestbook();
        gb2.setName("name2");
        guestbookRepository.save(gb2);
    }


    public void test02FindAll1() {
        List<Guestbook> list = guestbookRepository.findAll1();
    }


    public void test03Delete() {
        guestbookRepository.delete(1L, "1234");
    }


    public void test04FindAll2() {
        List<GuestbookDto> list = guestbookRepository.findAll2();
        assertEquals(1, list.size());
    }
}