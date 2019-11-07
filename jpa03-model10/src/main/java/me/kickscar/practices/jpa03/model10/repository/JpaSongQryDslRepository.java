package me.kickscar.practices.jpa03.model10.repository;

import me.kickscar.practices.jpa03.model10.domain.Song;

import java.util.List;

public interface JpaSongQryDslRepository {
    Song findById2(Long no);
    List<Song> findAll2();
}
