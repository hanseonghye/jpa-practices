package me.kickscar.practices.jpa03.model01.app01.service;

import me.kickscar.practices.jpa03.model01.app01.repository.JpqlGuestbookRepository;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuestbookService {

    @Autowired
    JpqlGuestbookRepository guestbookRepository;

    public void addMessage(Guestbook guestbook){
        guestbookRepository.save(guestbook);
    }

    public List<Guestbook> getMessageList(){
        return guestbookRepository.findAll();
    }

    public void deleteMessage(Guestbook guestbook){
        guestbookRepository.remove(guestbook);
    }
}