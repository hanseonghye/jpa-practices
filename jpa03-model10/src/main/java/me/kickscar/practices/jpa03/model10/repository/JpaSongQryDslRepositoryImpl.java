package me.kickscar.practices.jpa03.model10.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model10.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model10.domain.QGenre.genre;
import static me.kickscar.practices.jpa03.model10.domain.QSong.song;


public class JpaSongQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaSongQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaSongQryDslRepositoryImpl() {
        super(Song.class);
    }

    @Override
    public Song findById2(Long no) {
        return queryFactory
                .selectDistinct(song)
                .from(song)
                .innerJoin(song.genres, genre)
                .fetchJoin()
                .where(song.no.eq(no))
                .fetchOne();
    }

    @Override
    public List<Song> findAll2() {
        return queryFactory
                .selectDistinct(song)
                .from(song)
                .innerJoin(song.genres, genre)
                .fetchJoin()
                .fetch();
    }

}