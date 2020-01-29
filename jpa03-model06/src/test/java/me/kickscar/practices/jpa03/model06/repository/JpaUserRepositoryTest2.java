package me.kickscar.practices.jpa03.model06.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import me.kickscar.practices.jpa03.model06.domain.Blog;
import me.kickscar.practices.jpa03.model06.domain.User;

public class JpaUserRepositoryTest2 {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBlogRepository blogRepository;


    public void test01() {
        User user = new User();
        user.setId("dooly");
        user.setName("둘리");
        user.setPassword("1234");

        userRepository.save(user);
    }


    public void test02SaveBlog() {
        Blog blog = new Blog();
        blog.setName("둘리블로그");
        Blog blogPersisted = blogRepository.save(blog);

        User user = userRepository.findById("dooly").get();
        user.setBlog(blogPersisted);
    }
}