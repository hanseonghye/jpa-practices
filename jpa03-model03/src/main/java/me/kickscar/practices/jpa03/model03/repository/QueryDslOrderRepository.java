package me.kickscar.practices.jpa03.model03.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model03.domain.Order;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserOrderCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.kickscar.practices.jpa03.model03.domain.QOrder.order;

@Repository
public class QueryDslOrderRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslOrderRepository() {
        super(Order.class);
    }

    // 저장(영속화)
    public void save(Order order){
        getEntityManager().persist(order);
    }

    // 객체그래프(OneToMany Collection 조회)
    public List<Order> finadAll1(Long userNo) {
        User user = getEntityManager().find(User.class, userNo);
        return user.getOrders();
    }

    // Fetch Join + Paging 사용하기 : 예제 데이터 수는 3개씩
    public List<Order> findAll2(Long userNo, Integer page){
        return (List<Order>) queryFactory
                .from(order)
                .innerJoin(order.user).fetchJoin()
                .where(order.user.no.eq(userNo))
                .orderBy(order.regDate.desc())
                .offset((page - 1) * 3)
                .limit(3)
                .fetch();
    }

    // Inner Join + Paging 사용하기 : 예제 데이터 수는 3개씩
    public List<Order> findAll3(Long userNo, Integer page){
        return (List<Order>) queryFactory
                .from(order)
                .innerJoin(order.user)
                .where(order.user.no.eq(userNo))
                .orderBy(order.regDate.desc())
                .offset((page - 1) * 3)
                .limit(3)
                .fetch();
    }

    // count1
    public Long count() {
        return queryFactory
                .from(order)
                .fetchCount();
    }

    // count2
    public Long count(Long userNo) {
        return queryFactory
                .from(order)
                .where(order.user.no.eq(userNo))
                .fetchCount();
    }

    public List<UserOrderCountDto> countOfEachUsers() {
        return queryFactory
                .select(Projections.constructor(UserOrderCountDto.class, order.user.no, order.user.no.count()))
                .from(order)
                .innerJoin(order.user)
                .groupBy(order.user)
                .fetch();
    }
}
