package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class JpqlUserRepository {

    @Autowired
    private EntityManager em;

    // 조회1
    public User find(Long no) {
        return em.find(User.class, no);
    }

    // 조회2 (UserDto, Projection에 사용)
    public User find(String email, String password) {
        return null;
    }

    // 수정
    public Boolean update(User user){
        User userPersisted = em.find(User.class, user.getNo() );

        if( userPersisted == null ) {
            return false;
        }

        userPersisted.setRole(user.getRole() == null ? userPersisted.getRole() : user.getRole());
        userPersisted.setGender(user.getGender() == null ? userPersisted.getGender() : user.getGender() );
        userPersisted.setEmail(user.getEmail() == null ? userPersisted.getEmail() : user.getEmail() );
        userPersisted.setName(user.getName() == null ? userPersisted.getName() : user.getName() );
        userPersisted.setPassword(user.getPassword() == null ? userPersisted.getPassword() : user.getPassword());

        return true;
    }

    // 영속화
    public void save(User user){
        em.persist(user);
    }

    // JPQL 집합함수 사용법
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(u) from User u", Long.class);
        return query.getSingleResult();
    }
}
