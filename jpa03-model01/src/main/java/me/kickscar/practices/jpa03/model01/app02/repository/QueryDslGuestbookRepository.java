package me.kickscar.practices.jpa03.model01.app02.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static me.kickscar.practices.jpa03.model01.domain.QGuestbook.guestbook;

@Repository
public class QueryDslGuestbookRepository extends QuerydslRepositorySupport {

    // EntityManagerFactory가 인식할 수 있게끔!
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public QueryDslGuestbookRepository(JPAQueryFactory queryFactory) {
        super(Guestbook.class);
        this.queryFactory = queryFactory;
    }

    public void save( Guestbook guestbook ){
        guestbook.setRegDate(new Date());
        em.persist(guestbook);
    }

    public Boolean remove( Guestbook parameter ) {
        return queryFactory
                .delete( guestbook )
                .where( guestbook.no.eq( parameter.getNo() ).and( guestbook.password.eq( parameter.getPassword() ) ) )
                .execute() == 1;
    }

    public List<Guestbook> findAll(){
        return (List<Guestbook>) queryFactory
                .from( guestbook )
                .orderBy( guestbook.regDate.desc() )
                .fetch();
    }

    public List<Guestbook> findAll(int page){
        return (List<Guestbook>)queryFactory
                .from( guestbook )
                .orderBy( guestbook.regDate.desc() )
                .offset( (page-1)*3 )
                .limit( 3 )
                .fetch();
    }

    public List<Guestbook> findAll(String name){
        return (List<Guestbook>)queryFactory
                .from( guestbook )
                .where( guestbook.name.eq(name) )
                .fetch();
    }
}
