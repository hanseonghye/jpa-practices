package me.kickscar.practices.jpa03.model01.app03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import me.kickscar.practices.jpa03.model01.app03.service.GuestbookService;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan( basePackages = {  // 하위 패키지에 Config, Service, Repository가 있기 때문에 자동스캔 가능(생략가능)
        "me.kickscar.practices.jpa03.model01.app03.config",
        "me.kickscar.practices.jpa03.model01.app03.service",
        "me.kickscar.practices.jpa03.model01.app03.repository" } )
public class SpringBootRestApp03 {

    @RestController
    @RequestMapping("/api/guestbook")
    public class GuestbookAPIController {

        @Autowired
        private GuestbookService guestbookService;

        @PostMapping("")
        public Guestbook add(@RequestBody Guestbook guestbook){
            return guestbookService.addMessage( guestbook );
        }

        @PutMapping("/{no}")
        public Map<String, String> update( @PathVariable("no") Long no, @RequestBody Guestbook guestbook ) {
            guestbook.setNo(no);
            guestbookService.updateMessage(guestbook);

            return new HashMap<String, String>() {{
                put("result", "success");
            }};
        }

        @DeleteMapping("/{no}")
        public Map<String, Long> delete(
                @PathVariable("no") Long no,
                @RequestParam(value = "password", required=true, defaultValue="") String password) {
            guestbookService.deleteMessage(no, password);
            return new HashMap<String, Long>() {{
                put("no", no);
            }};
        }

        @GetMapping("/{no}")
        public Guestbook list( @PathVariable("no") Long no ) {
            return guestbookService.getMessage(no);
        }

        @GetMapping("/all")
        public List<Guestbook> all() {
            return guestbookService.getAllMessages();
        }

        @GetMapping("/count")
        public Map<String, Long> count() {
            return new HashMap<String, Long>() {{
                put("count", guestbookService.getCount());
            }};
        }

        @GetMapping("/list")
        public List<Guestbook> list() {
            return guestbookService.getMessageList();
        }

        @GetMapping("/list/{page}")
        public List<Guestbook> list( @PathVariable("page") Integer page ) {
            return guestbookService.getMessageList(page);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run( SpringBootRestApp03.class, args );
    }
}
