package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 으로 조회
    User findByUsername(String username);

    // phonenumber 으로 조회
    User findByPhonenumber(String phonenumber);

    // email 으로 조회
    User findByEmail(String email);
}
