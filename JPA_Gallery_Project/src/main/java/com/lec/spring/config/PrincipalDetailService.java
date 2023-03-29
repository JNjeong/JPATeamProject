package com.lec.spring.config;


import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service    // UserDetailsService 빈 등록
public class PrincipalDetailService implements UserDetailsService {

    // "/user/login" url 로 POST request 가 들어오면 시큐리티가 낚아채서 처리, 대신 로그인을 진행해준다.
    // 이와 같이 하면 Controller 에서 /user/login (POST) 를 굳이 만들지 않아도 된다!
    // 위 요청이 오면 자동으로 UserDetailsService 타입 빈객체의 loadUserByUsername() 가 실행되어 인증여부 확인진행 <- 이를 제공해주어야 한다.

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username을 매개변수로 받아서 UserDetails 객체를 반환한다.

        // DB 조회
        User user = userService.findByUsername(username);

        // 해당 username의 user가 DB에 있다면 UserDetails를 생성해서 리턴
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
