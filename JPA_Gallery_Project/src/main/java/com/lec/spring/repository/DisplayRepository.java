package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayRepository extends JpaRepository<Book, Long> {
}
