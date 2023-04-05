package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.DisplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisplayService {

    private DisplayRepository displayRepository;
//    private UserRepository userRepository;


    @Autowired
    public void setDisplayRepository(DisplayRepository displayRepository) {
        this.displayRepository = displayRepository;
    }

//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    public DisplayService(){
        System.out.println("DisplayService() 생성");
    }

    public int reserve(Book book){

        // 현재 로그인한 작성자 정보
//        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다
//        user = userRepository.findById(user.getId()).orElse(null);
//        book.setUser(user);  // 예약자 세팅

        countSeat(book);
        book = displayRepository.saveAndFlush(book);        // INSERT

        return 1;
    }


    // 잔여석 정보를 가져와서 -1 해주기
    public void countSeat(Book book){
        Long seat = book.getSeat();

        if(seat > 0){
            seat = seat - 1;
            book.setSeat(seat);
        }
    }


}
