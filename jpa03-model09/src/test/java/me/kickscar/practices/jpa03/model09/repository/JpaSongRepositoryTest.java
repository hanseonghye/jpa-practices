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

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

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
        Genre genre1 = new Genre();
        genre1.setName("쟝르1");
        genre1.setAbbrName("쟝르1");
        genreRepository.save(genre1);

        Genre genre2 = new Genre();
        genre2.setName("쟝르2");
        genre2.setAbbrName("쟝르2");
        genreRepository.save(genre2);

        Genre genre3 = new Genre();
        genre3.setName("쟝르3");
        genre3.setAbbrName("쟝르3");
        genreRepository.save(genre3);

        Genre genre4 = new Genre();
        genre4.setName("쟝르4");
        genre4.setAbbrName("쟝르4");
        genreRepository.save(genre4);

        Song song1 = new Song();
        song1.setTitle("노래1");
        song1.getGenres().add(genre1);
        song1.getGenres().add(genre2);
        songRepository.save(song1);

        Song song2 = new Song();
        song2.setTitle("노래2");
        song2.getGenres().add(genre1);
        song2.getGenres().add(genre4);
        songRepository.save(song2);

        assertEquals(4L, genreRepository.count());
        assertEquals(2L, songRepository.count());
    }

    @Test
    @Transactional
    public void test02FindById() {
        Song song = songRepository.findById(2L).get();

        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(song.getGenres()));
        assertEquals(2L, song.getGenres().size());
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(song.getGenres()));
    }

    @Test
    public void test03FindById2() {
        Song song = songRepository.findById2(2L);
        System.out.println(song);
    }

    @Test
    @Transactional
    public void test04FindAll() {
        List<Song> songs = songRepository.findAll();

        assertEquals(2L, songs.size());
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(songs.get(1).getGenres()));
        assertEquals(2L, songs.get(1).getGenres().size());
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(songs.get(1).getGenres()));
    }

    @Test
    public void test05FindAll2() {
        List<Song> songs = songRepository.findAll2();
        for(Song song : songs) {
            System.out.println(song);
        }
    }

}