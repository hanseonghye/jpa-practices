package me.kickscar.practices.jpa03.model01.app03;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.repository.Model01JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.config", "me.kickscar.practices.jpa03.model01.repository" } )
public class Jpa03SpringBootRestApp03 {

    @RestController
    @RequestMapping("/model01")
    public class Model01Controller {

        @Autowired
        private Model01JpaRepository repository;

        @GetMapping("/list")
        public List<Guestbook> list() {
            return repository.findAllByOrderByRegDateDesc();
        }

        @GetMapping("/list/{page}")
        public List<Guestbook> list2( @PathVariable("page") int page ) {
            //페이징 조건과 정렬 조건 설정
            PageRequest pageRequest = new PageRequest(page, 2, new Sort(Sort.Direction.DESC, "name"));
            return repository.findAllByOrderByRegDateDesc(pageRequest);
        }
    }

    @Component
    public class RepositoryMethodTestAfterBootAppLoaded {

        @Autowired
        private Model01JpaRepository repository;

        public void test(){

            // 기본 CRUD 메소드: save
            Guestbook gb1 = new Guestbook();
            gb1.setName("둘리");
            gb1.setPassword("1234");
            gb1.setContents("안녕1");
            repository.save(gb1);

            Guestbook gb2 = new Guestbook();
            gb2.setName("마이콜");
            gb2.setPassword("1234");
            gb2.setContents("안녕2");
            repository.save(gb2);

            Guestbook gb3 = new Guestbook();
            gb3.setName("또치");
            gb3.setPassword("1234");
            gb3.setContents("안녕3");
            repository.save(gb2);

            Guestbook gb4 = new Guestbook();
            gb4.setName("도우넛");
            gb4.setPassword("1234");
            gb4.setContents("안녕4");
            repository.save(gb4);

            // 쿼리메소드: deleteByNoAndPassword
            repository.deleteByNoAndPassword(0L, "1234");

            // 쿼리메소드: findAllByOrderByRegDateDesc
            List<Guestbook> list = repository.findAllByOrderByRegDateDesc();
            for(Guestbook gb : list){
                System.out.println(gb);
            }
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run( Jpa03SpringBootRestApp03.class, args );
        context.getBean( RepositoryMethodTestAfterBootAppLoaded.class ).test();
    }
}
