package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import aj.org.objectweb.asm.TypePath;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpqlGuestbookRepository2 {
    @Autowired
    private EntityManager em;


    public void save(Guestbook guestbook) {
        em.persist(guestbook);
    }


    public Boolean delete(Long no, String password) {
        String qlString = "delete from Guestbook gb where gb.no=:no and gb.password=:password";
        Query query = em.createQuery(qlString);
        query.setParameter("no", no);
        query.setParameter("password", password);
        return query.executeUpdate() == 1;
    }


    public List<Guestbook> findAll1() {
        String qlString = "select ~";
        TypedQuery<Guestbook> query = em.createQuery(qlString, Guestbook.class);
        return query.getResultList();
    }


    public List<GuestbookDto> findAll2() {
        String qlString = "select ~";
        TypedQuery<GuestbookDto> query = em.createQuery(qlString, GuestbookDto.class);
        return query.getResultList();
    }


    public Long count() {
        String qlString = "select count ~";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);
        return query.getSingleResult();
    }
}
