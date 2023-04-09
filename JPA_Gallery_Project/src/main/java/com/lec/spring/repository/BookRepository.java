package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.domain.Display;
import com.lec.spring.domain.DisplayDetail;
import com.lec.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
}
