package com.lec.spring.repository;

import com.lec.spring.domain.Display;
import com.lec.spring.domain.DisplayDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DisplayDetailRepository extends JpaRepository<DisplayDetail, Long> {
    DisplayDetail findByDisplayAndVisitDate(Display display, LocalDate visit_date);
}
