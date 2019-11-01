package me.kickscar.practices.jpa03.model07.repository;


import me.kickscar.practices.jpa03.model07.domain.Blog;
import me.kickscar.practices.jpa03.model07.domain.BlogDto3;

import java.util.List;

public interface JpaBlogQryDslRepository {
    List<Blog> findAll2();
    List<BlogDto3> findAll3();
}
