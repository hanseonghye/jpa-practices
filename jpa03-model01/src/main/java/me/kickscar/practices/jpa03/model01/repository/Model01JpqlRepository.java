package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public class Model01JpqlRepository {

    // EntityManagerFactory가 인식할 수 있게끔!
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Guestbook guestbook){
        guestbook.setRegDate(new Date());
        em.persist(guestbook);
    }

    public List<Guestbook> findAll(){
        TypedQuery query = em.createQuery("select gb from Guestbook gb order by gb.regDate desc", Guestbook.class);
        List<Guestbook> list = query.getResultList();
        return list;
    }

    @Transactional
    public Boolean remove(Guestbook guestbook){

        //
        // :의 용도는  setParameter
        //
        TypedQuery query = em.createQuery("select gb from Guestbook gb where gb.no= :no and gb.password = :password", Guestbook.class);
        query.setParameter("no", guestbook.getNo());
        query.setParameter("password", guestbook.getPassword());

        //
        // ?index 로 파라미터 인덱싱도 가능
        //
        // TypedQuery query = em.createQuery("select gb from Guestbook gb where gb.no= ?1 and gb.password = ?2", Guestbook.class);
        // query.setParameter(1, guestbook.getNo());
        // query.setParameter(2, guestbook.getPassword());

        // 결과가 잘못되면 error handling을 해야하는 번잡함이 있다. 따라서 다음과 같은 코드가 효율적임.
        // Guestbook result = query.getSingleResult();
        List<Guestbook> list = query.getResultList();
        if(list.size() != 1) {
            return false;
        }

        em.remove(list.get(0));

        return true;
    }



}
