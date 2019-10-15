package me.kickscar.practices.jpa03.model01.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static me.kickscar.practices.jpa03.model01.domain.QGuestbook.guestbook;

@Repository
public class QueryDslGuestbookRepository extends QuerydslRepositorySupport {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public QueryDslGuestbookRepository(JPAQueryFactory queryFactory) {
        super(Guestbook.class);
        this.queryFactory = queryFactory;
    }

    // 저장(영속화)
    public void save(Guestbook guestbook){
        guestbook.setRegDate(new Date());
        em.persist(guestbook);
    }

    // 삭제
    public Boolean remove(Long no, String password) {
        return queryFactory
                .delete(guestbook)
                .where(guestbook.no.eq(no).and(guestbook.password.eq(password)))
                .execute() == 1;
    }

    // Fetch List: Projection with GuestbookVo
    public List<Guestbook> findAll(){
       return (List<Guestbook>) queryFactory
                .from(guestbook)
                .orderBy(guestbook.regDate.desc())
                .fetch();
    }

    // count
    public Long count() {
        return queryFactory.from(guestbook).fetchCount();
    }
}
