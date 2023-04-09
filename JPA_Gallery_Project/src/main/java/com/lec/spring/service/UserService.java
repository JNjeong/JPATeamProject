package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.BookRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;
    private final BookRepository bookRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public UserService(BookRepository bookRepository){
        System.out.println(getClass().getName() + "() 생성");
        this.bookRepository = bookRepository;
    }

    // 아이디 (username) 의 user 정보
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    // phonnumber(전화번호) 의 User 정보 읽어오기
    public User findByPhonenumber(String phonenumber) {
        return userRepository.findByPhonenumber(phonenumber);
    }

    // email(전화번호) 의 User 정보 읽어오기
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // 특정 아이디 (username) 의 회원이 존재 여부
    public boolean isExist(String username){
        User user = findByUsername(username);
        return (user != null) ? true : false;
    }

    public boolean isPhonenumber(String phonenumber){
        User user = findByPhonenumber(phonenumber);
        return (user != null) ? true : false;
    }

    public boolean isEmail(String email){
        User user = findByEmail(email);
        return (user != null) ? true : false;
    }

    // 회원가입
    public int register(User user){
        user.setUsername(user.getUsername().toUpperCase()); // 아이디 DB 에 대문자로 저장
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화하여 저장
        user = userRepository.save(user); // 새로운 회원 저장 (user 는 id 값)

        // 신규 회원 member 권한 부여
        Authority authority = authorityRepository.findByName("ROLE_MEMBER");
        user.addAuthority(authority);
        userRepository.save(user);

        return 1;
    }

    public boolean checkPassword(Long id, String checkPassword) {
        User user = userRepository.findById(id).orElse(null);
        String realPassword = user.getPassword();
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        return matches;
    }

    // 회원정보 수정
    public int modify(Long id, User updatedUser){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findById(id);
        if(user != null){
            user.get().setPassword(encoder.encode(updatedUser.getPassword()));
            user.get().setName(updatedUser.getName());
            user.get().setPhonenumber(updatedUser.getPhonenumber());
            user.get().setEmail(updatedUser.getEmail());
            userRepository.save(user.get());
            return 1;
        } else {
            return 0;
        }
    }

    // 회원 탈퇴
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    // 특정 회원의 authorities
    public List<Authority> selectAuthoritiesById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){ return user.getAuthorities(); }
        return new ArrayList<>();
    }

}
