package me.kickscar.practices.jpa03.model09.repository;

import me.kickscar.practices.jpa03.model09.config.JpaConfig;
import me.kickscar.practices.jpa03.model09.domain.Genre;
import me.kickscar.practices.jpa03.model09.domain.Song;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaSongRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaSongRepository songRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        Genre genre = new Genre();
        genre.setName("쟝르1");
        genre.setAbbrName("쟝르1");
        genreRepository.save(genre);

        Song song = new Song();
        song.setTitle("노래1");
        song.getGenres().add(genre);
        songRepository.save(song);
;    }
}