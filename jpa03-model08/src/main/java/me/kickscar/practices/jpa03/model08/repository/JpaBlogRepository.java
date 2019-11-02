package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository {
}
