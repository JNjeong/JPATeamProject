package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.domain.Display;
import com.lec.spring.domain.DisplayDetail;
import com.lec.spring.domain.User;
import com.lec.spring.repository.BookRepository;
import com.lec.spring.repository.DisplayDetailRepository;
import com.lec.spring.repository.DisplayRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Service
public class DisplayService {

    private DisplayRepository displayRepository;
    private UserRepository userRepository;

    private BookRepository bookRepository;

    private DisplayDetailRepository displayDetailRepository;


    @Autowired
    public void setDisplayRepository(DisplayRepository displayRepository) {
        this.displayRepository = displayRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    public void setDisplayDetailRepository(DisplayDetailRepository displayDetailRepository) {
        this.displayDetailRepository = displayDetailRepository;
    }


    public DisplayService() {
        System.out.println("DisplayService() 생성");
    }

    public long reserve(Display display, DisplayDetail displayDetail) {

        // 현재 로그인한 작성자 정보
        User user = Util.getLoggedUser();
//
//        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다

        LocalDate visitDate = displayDetail.getVisitDate();
        DisplayDetail detail = displayDetailRepository.findByDisplayAndVisitDate(display, visitDate);
        detail.setSeatCount(detail.getSeatCount()-1);
        displayDetailRepository.saveAndFlush(detail);  // UPDATE

        user = userRepository.findById(user.getId()).orElse(null);
        Book book = new Book();
        book.setUser(user);  // 예약자 세팅
        book.setDisplayDetail(detail);
        book = bookRepository.saveAndFlush(book);        // INSERT

        return book.getId();
    }


    public Long getCountSeat(Long dp_seq, LocalDate visit_date) {
        Display display = new Display();
        display.setDp_seq(dp_seq);
        DisplayDetail displayDetail = displayDetailRepository.findByDisplayAndVisitDate(display, visit_date);
        if (displayDetail == null) {
            return 0L;
        }else {
            return displayDetail.getSeatCount();
        }
    }

}