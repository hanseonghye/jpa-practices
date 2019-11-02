package me.kickscar.practices.jpa03.model08.repository;


import me.kickscar.practices.jpa03.model08.domain.Blog;

import java.util.List;

public interface JpaBlogQryDslRepository {
    List<Blog> findAll2();
}
