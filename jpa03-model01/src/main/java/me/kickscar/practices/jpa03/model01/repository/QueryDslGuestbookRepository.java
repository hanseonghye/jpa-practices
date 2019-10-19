package me.kickscar.practices.jpa03.model01.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        em.persist(guestbook);
    }

    // 조회1
    public List<Guestbook> findAllByOrderByRegDateDesc1(){
        return (List<Guestbook>) queryFactory
                .from(guestbook)
                .orderBy(guestbook.regDate.desc())
                .fetch();
    }

    // 조회2 - 프로젝션
    public List<GuestbookDto> findAllByOrderByRegDateDesc2(){
        return (List<GuestbookDto>) queryFactory
                .select(Projections.constructor(GuestbookDto.class, guestbook.no, guestbook.name, guestbook.contents, guestbook.regDate))
                .from(guestbook)
                .orderBy(guestbook.regDate.desc())
                .fetch();
    }


    // 삭제
    public Boolean deleteByNoAndPassword(Long no, String password) {
        return queryFactory
                .delete(guestbook)
                .where(guestbook.no.eq(no).and(guestbook.password.eq(password)))
                .execute() == 1;
    }

    // count
    public Long count() {
        return queryFactory.from(guestbook).fetchCount();
    }
}
