package com.lec.spring.config;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public PrincipalDetails(User user){
        System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("getAuthorities() 호출");
        Collection<GrantedAuthority> collect = new ArrayList<>();

        List<Authority> list = userService.selectAuthoritiesById(user.getId());
        for(Authority auth : list){
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth.getName();
                }
                @Override
                public String toString() {
                    return auth.getName();
                }
            });
        }
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // 계정이 만료되지 않았나?

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // 계정이 잠긴건 아닌지?

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // 계정 credential 이 만료 된건 아닌지?

    @Override
    public boolean isEnabled() {
        return true;
        // 예를 들어 1년동안 로그인 안하면 휴면계정으로 전환 (현재시간 - 로그인시간이 1년을 초과하면 false)
    }
}