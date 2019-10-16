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
    public List<Order> finadAll(Long userNo) {
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

    // count3 : 이거 말하는 거냐? join + group by?
    /*
     *      user no | order count
     *         1    |      5
     *         2    |      2
     *         3    |      1
     * 플어보마,,,
     *
     * QueryDSL도 JPQL이랑 비숫하다.
     * groupby 해서 count() 하는거랑 select에서 컬럼 선택(projection)하는 것, 그리고 특정 Dto에 담는 것
     * 머 이런거 봐줄만 할 듯! 톡으로 도무지 이런 걸 설명할 수가 없어서 너 머 물어보면 대략 난감하단ㅠ
     *
     * 음...
     *
     * QueryDSL이 JPQL이랑 다른 것이 아니다.
     * 객체지향쿼리의 핵심은 JPQL이다. JPQL이 기본이고 제일 중요해
     * 니가 보는 책에도 JPQL을 QueryDSL, Spring Data JPA 앞에서 제일 많이 다루고 있쟎어.
     *
     * JPQL이 String 기반쿼리니깐 Criteria라는 것으로 객체지향답게 래핑했는데... 이게 워낙 그지같아서 mysema에서 개발한 JPQL의 래퍼라이브러리가
     * QueryDSL이다. 이게 쫌 쓸만하지!!
     *
     * 요는 QueryDSL도 JPQL로 변환되고 JPQL이 SQL로 변환된다.
     * 로그보면 JPQL, SQL(Prepared Statement) 로 2개 보여주쟎어.
     * 그리고 쌤이 해놓은거 보면 JPQLRepository 메소드 전부다 QueryDslRepostory로 변환되는데 열라 심플한게 직관적이고 보기좋게 되쟎어.
     *
     * 니가 쓰고 있는 JpaRepository 상속받은 인터페이스 기반의 Repository는
     * 많이 쓰는 쿼리를 메소드 이름기반으로 정의해 놓으면 Spring JPA가
     * 어플리케이션 뜰 때, JPQL기반의 메소드가 있는 객체를 자동 생성해주는 기능이 있는 것이고...
     *
     * 여튼 JPQL 부터 익혀서 JPQL로 해결이 되면 QueryDSL롭 바꾸고 이러다보면 JPQL 건너띠고 바로 QueryDSL이 툭 튀어 나오겠지...
     * 시간이 지나면 아~~ 이건 QueryDSL로 해야해 아냐 이건 인터페이스에 메소드 이름만 잘 지으면 되 이런 감도 생기고...
     * JpaRepository 자식 레포지토리의 이름 기반 메소드로 안나오겠다 싶으면 QueryDSL 기반의 메소드 구현하면 됨. (이건 할줄아는 거 같으니 안할래, 아니 조만간 이걷소 해놓으마)
     *
     */
    public List<UserOrderCountDto> countsOfEachUser() {
        return queryFactory
                .select(Projections.constructor(UserOrderCountDto.class, order.user.no, order.user.no.count()))
                .from(order)
                .innerJoin(order.user)
                .groupBy(order.user)
                .fetch();
    }
}
