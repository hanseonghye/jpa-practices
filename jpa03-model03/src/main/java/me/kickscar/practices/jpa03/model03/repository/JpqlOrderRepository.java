package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.Order;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserOrderCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpqlOrderRepository {

    @Autowired
    private EntityManager em;

    // 저장(영속화)
    public void save(Order order){
        em.persist(order);
    }

    // 객체그래프(OneToMany Collection 조회)
    // 1. Fetch.LAZY(디폴트) : user.getOrders() 호출 시 select query 실행
    // 2. Fetch.EAGER : Left Outer Join (User 에 가서 바꿔보고 로그 확인해 볼 것)
    // 3. Fetch.LAZY 추천  : Fetch.EAGER로 조회한 User의 orders가 다 채워져야 할 경우는 많지않다(예로 로그인시 --;;;)
    // 4. 문제는 select가 2회 실행되는 것이다.
    public List<Order> findAll1(Long userNo){
        User user = em.find(User.class, userNo);
        return user.getOrders();
    }

    // Entity(ManyToOne의 Many) Fetch Join + Paging 사용하기: 예제 데이터 수는 3개씩
    // 1. 반대편 OneToMany 쪽(User)에서 하면 Collection Fetch Join이 되며 N+1 문제가 발생할 수 있다.(DISTINCT로 해결가능)
    // 2. SQL은 inner join이 걸리고 대체적으로 만족스럽다.
    // 3. fetch join은 JPQL의 inner join 보다 성능면에서 좋다. (findAll3와 쿼리 비교룰 해보자)
    public List<Order> findAll2(Long userNo, Integer page){
        String qlString = "select o from Order o join fetch o.user where o.user.no=:userNo order by o.regDate desc";
        TypedQuery<Order> query = em.createQuery(qlString, Order.class);
        query.setParameter("userNo", userNo);
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // Inner Join + Paging 사용하기 : 예제 데이터 수는 3개씩
    // inner는 생략 가능
    public List<Order> findAll3(Long userNo, Integer page){
        String qlString = "select o from Order o inner join o.user where o.user.no=:userNo order by o.regDate desc";
        TypedQuery<Order> query = em.createQuery(qlString, Order.class);
        query.setParameter("userNo", userNo);
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // count1
    public Long count() {
        String qlString = "select count(o) from Order o";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);
        return query.getSingleResult();
    }

    // count2
    public Long count(Long userNo) {
        String qlString = "select count(o) from Order o where o.user.no=:userNo";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);
        query.setParameter("userNo", userNo);
        return query.getSingleResult();
    }

    // count3 : 이거 말하는 거냐? group by?
    /*
     *      user no | order count
     *         1    |      5
     *         2    |      2
     *         3    |      1
     *
     * 플어보마,,,
     *
     * 1. JPQL inner join 이랑 group by 쓰면된다.
     *
     * 2. projection 이  user no, order count 이니깐
     *    UserOrderCountDto로 projection 결과를 받는 특정 객체로 사용했다. (엔티티 클래스 객체를 안받는 경우도 많으니깐 알아두며 좋을 듯~)
     */
    public List<UserOrderCountDto> countOfEachUsers() {
        String qlString = "select new me.kickscar.practices.jpa03.model03.dto.UserOrderCountDto( u.no, count(o.no) ) from Order o join o.user u group by o.user";
        TypedQuery<UserOrderCountDto> query = em.createQuery(qlString, UserOrderCountDto.class);

        return query.getResultList();
    }

}
