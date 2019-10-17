package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class JpqlUserRepository {

    @Autowired
    private EntityManager em;

    // 저장(영속화)
    public void save(User user){
        em.persist(user);
    }

    // 수정1
    public Boolean update1(User user){
        Query query = em.createQuery("update User u set u.role=:role, u.gender=:gender, u.email=:email, u.name=:name, u.password=:password where u.no=:no");

        query.setParameter("no", user.getNo());
        query.setParameter("role", user.getRole());
        query.setParameter("gender", user.getGender());
        query.setParameter("email", user.getEmail());
        query.setParameter("name", user.getName());
        query.setParameter("password", user.getPassword());

        return query.executeUpdate() == 1;
    }

    // 수정2
    public User update2(User user){
        User userPersisted = em.find(User.class, user.getNo());

        if(userPersisted != null){
            userPersisted.setRole(user.getRole());
            userPersisted.setGender(user.getGender());
            userPersisted.setEmail(user.getEmail());
            userPersisted.setName(user.getName());
            userPersisted.setPassword(user.getPassword());
        }

        return userPersisted;
    }

    // 조회1
    public User find(Long no) {
        return em.find(User.class, no);
    }

    // 조회2 (UserDto, Projection에 사용)
    public User find(String email, String password) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.email=:email and u.password=:password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        return query.getSingleResult();
    }

    // JPQL 집합함수 사용법
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(u) from User u", Long.class);
        return query.getSingleResult();
    }
}
