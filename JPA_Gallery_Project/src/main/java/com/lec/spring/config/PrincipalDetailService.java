package com.lec.spring.config;

import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service   // UserDetailsService 빈 등록
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername(" + username + ")");

        // DB 조회
        User user = userService.findByUsername(username);
        
        // 해당 username 의 user 가 DB 에 있다면 UserDetails 생성해서 리턴
        if(user != null){
            PrincipalDetails userDetails = new PrincipalDetails(user);
            userDetails.setUserService(userService);
            return userDetails;
        }

        // 해당 username의 user 가 없다면? ==> UsernameNotFoundException을 throw 해주어야 한다.

        throw new UsernameNotFoundException(username);

        //주의! 여기서 null return 하면 예외 발생된다.
    }
}


