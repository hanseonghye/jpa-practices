package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class JpqlGuestbookRepository {

    @Autowired
    private EntityManager em;

    // 저장(영속화)
    public void save(Guestbook guestbook){
        guestbook.setRegDate(new Date());
        em.persist(guestbook);
    }

    // 삭제
    public Boolean remove(Long no, String password) {
        Query query = em.createQuery("delete from Guestbook gb where gb.no= :no and gb.password = :password");
        query.setParameter("no", no);
        query.setParameter("password", password);

        return query.executeUpdate() == 1;
    }

    // Fetch List: Projection with GuestbookVo
    public List<Guestbook> findAll(){
        TypedQuery<Guestbook> query = em.createQuery("select gb from Guestbook gb order by gb.regDate desc", Guestbook.class);
        return query.getResultList();
    }

    // 집합함수
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(gb) from Guestbook gb", Long.class);
        return query.getSingleResult();
    }
}
