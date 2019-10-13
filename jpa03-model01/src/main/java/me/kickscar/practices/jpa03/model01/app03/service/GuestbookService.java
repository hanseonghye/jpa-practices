package me.kickscar.practices.jpa03.model01.app03.service;

import me.kickscar.practices.jpa03.model01.app03.repository.JpaGuestbookRepository;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GuestbookService {

    @Autowired
    private JpaGuestbookRepository guestbookRepository;

    public Guestbook addMessage(Guestbook guestbook){
        guestbook.setRegDate(new Date());
        guestbookRepository.save(guestbook);
        return guestbook;
    }

    public void updateMessage(Guestbook guestbook){
        Guestbook gb = guestbookRepository.findById(guestbook.getNo()).get();

        gb.setName( guestbook.getName() );
        gb.setContents( guestbook.getContents() );
        gb.setPassword( guestbook.getPassword() );
    }

    public void deleteMessage(Long no) {
        guestbookRepository.deleteById(1L);
    }

    public void deleteMessage(Long no, String password){
        guestbookRepository.deleteByNoAndPassword(no, password);
    }

    public Long getCount() {
        return guestbookRepository.count();
    }

    public Guestbook getMessage(Long no) {
        return guestbookRepository.findById(no).get();
    }

    public List<Guestbook> getAllMessages(){
        return guestbookRepository.findAll();
    }

    public List<Guestbook> getMessageList() {
        return guestbookRepository.findAllByOrderByRegDateDesc();
    }

    public List<Guestbook> getMessageList(Integer page) {
        //페이징 조건과 정렬 조건 설정
        PageRequest pageRequest = new PageRequest(page, 2, new Sort(Sort.Direction.DESC, "regDate"));
        return guestbookRepository.findAllByOrderByRegDateDesc( pageRequest );
    }
}