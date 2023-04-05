package com.lec.spring.config;


// WebSecurityConfigurerAdapter
// deprecated 공식 : https://docs.spring.io/spring-security/site/docs/5.7.0-M2/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html
//    ↑ 읽어보면  WebSecurityConfigurerAdapter가 Deprecated 되었으니 SecurityFilterChain를 Bean으로 등록해서 사용하라는 말.
// 대안 공식문서 참조 : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter


//  Spring Security 6 에선
// authorizeRequests() 는 deprecated 되고
// antMathers(), mvcMathcers(), regexMatchers() @EnableGlobalMethodSecurity 들은  없어졌다?
// https://stackoverflow.com/questions/74683225/updating-to-spring-security-6-0-replacing-removed-and-deprecated-functionality


// What's new Sprint Security 6
// https://docs.spring.io/spring-security/reference/whats-new.html


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableWebSecurity를 통해 @Configuration으로 bean 생성된 이 클래스는 web security 작업이 가능해진다.
public class SecurityConfig {
    // WebSecurityConfigurerAdapter
    // deprecated 공식 : https://docs.spring.io/spring-security/site/docs/5.7.0-M2/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html
    //    ↑ 읽어보면  WebSecurityConfigurerAdapter가 Deprecated 되었으니 SecurityFilterChain를 Bean으로 등록해서 사용하라는 말.
    // 대안 공식문서 참조 : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter


    //  Spring Security 6 에선
    // authorizeRequests() 는 deprecated 되고
    // antMathers(), mvcMathcers(), regexMatchers() @EnableGlobalMethodSecurity 들은  없어졌다?
    // https://stackoverflow.com/questions/74683225/updating-to-spring-security-6-0-replacing-removed-and-deprecated-functionality


    // What's new Sprint Security 6
    // https://docs.spring.io/spring-security/reference/whats-new.html

    // ↓ Security를 동작시키지 않도록 함.
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring().anyRequest(); //lambda expression. WebSecurityCustomizer가 가지고 있는 추상메소드를 구현한것.
//    }
//    예제용 코드이다.
//==============================================================


    // PasswordEncoder 를 bean 으로 IoC 에 등록
    // IoC 에 등록된다, IoC 내에선 '어디서든' 가져다가 사용할수 있다.
    @Bean
    public PasswordEncoder encoder(){
        System.out.println("PasswordEncoder bean 생성");
        return new BCryptPasswordEncoder();
    }

    //이전에는 xml 로 설정했지만, 객체지향 언어이다 보니 객체지향적으로 설정하기 위해 등장한 방법이다.
    // ↓ SecurityFilterChain을 bean으로 등록해서 사용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception{  //filterChain을 등록시 web에서 HttpSecurity를 넘겨주기에, 받아주어야 한다.
//        return security
//                .build();   //default 동작하는 filterChain이 만들어진다.

        return security
                .csrf(csrf -> csrf.disable())   //CSRF 비활성화.

                /**********************************************
                 * ① request URL 에 대한 접근 권한 세팅  : authorizeHttpRequests()
                 * .authorizeHttpRequests( AuthorizationManagerRequestMatcherRegistry)
                 **********************************************/
                .authorizeHttpRequests(auth -> auth  // AuthorizationManagerRequestMatcherRegistry 란 이름의 매개변수가 있다~
                        //URL 과 접근권한 세팅(들)
                        // ↓ /board/detail/** URL로 들어오는 요청은 '인증'(로그인)만 필요
                        .requestMatchers("/board/detail/**").authenticated()
                        // ↓ "/board/write/**", "/board/update/**", "/board/delete/**" URL로 들어오는 요청은 '인증'(로그인)과 '권한'(ROLE_MEMBER / ROLE_ADMIN)도 필요하다.
                        .requestMatchers("/board/write/**", "/board/update/**", "/board/delete/**").hasAnyRole("MEMBER", "ADMIN")   //ROLE_은 생략가능
                        // ↓ 그밖의 모든 URL요청은 모두 허락(permit)한다.
                        .anyRequest().permitAll()
                )


                /********************************************
                 * ② 폼 로그인 설정
                 * .formLogin(HttpSecurityFormLoginConfigurer)
                 *  form 기반 인증 페이지 활성화.
                 *  만약 .loginPage(url) 가 세팅되어 있지 않으면 '디폴트 로그인' form 페이지가 활성화 된다
                 ********************************************/
                .formLogin(form -> form
                        .loginPage("/user/login")    //로그인이 필요한 상황 발생시 매개변수의 url(로그인 폼)으로 request가 발생한다.
                        .loginProcessingUrl("/user/login")  // "/user/login" url 로 POST request 가 들어오면 시큐리티가 낚아채서 처리, 대신 로그인을 진행해준다.
                                                            // 이와 같이 하면 Controller 에서 /user/login (POST) 를 굳이 만들지 않아도 된다!
                                                            // 위 요청이 오면 자동으로 UserDetailsService 타입 빈객체의 loadUserByUsername() 가 실행되어 인증여부 확인진행 <- 이를 제공해주어야 한다.

                        .defaultSuccessUrl("/")     // '직접 /login' → /loginOk 에서 성공하면 "/" 로 이동시키기
                                                    // 만약 다른 특정페이지에 진입하려다 로그인 하여 성공하면 해당 페이지로 이동 (너무 편리!)

                        //로그인 성공 직후에 수행할 코드 등록
                        //.successHandler(AuthenticationSuccessHandler)  // 로그인 성공후 수행할 코드.
                        .successHandler(new CustomLoginSuccessHandler("/home"))

                        // 로그인 실패하면 수행할 코드
                        // .failureHandler(AuthenticationFailureHandler)
                        .failureHandler(new CustomLoginFailureHandler())
                )
                /********************************************
                 * ③ 로그아웃 설정
                 * .logout(LogoutConfigurer)
                 ********************************************/
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/user/logout")  //로그아웃 수행 url
//                        .logoutSuccessUrl("/login?logout")    //로그아웃 성공 후 redirect할 url

                        .invalidateHttpSession(false)  //session invalidate (default=true) : 세션을 지금 날리지 않겠다.
                                // 이따가 CustomLogoutSuccessHandler 에서 꺼낼 정보가 있기 때문에
                                // false 로 세팅한다
                                // .deleteCookies("JSESSIONID")   // 쿠키 제거

                        // 로그아웃 성공후 수행할 코드
                        // .logoutSuccessHandler(LogoutSuccessHandler)
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )
                /********************************************
                 * ④ 예외처리 설정
                 * .exceptionHandling(ExceptionHandlingConfigure)
                 ********************************************/
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        // 권한(Authorization) 오류 발생시 수행할 코드
                        // .accessDeniedHandler(AccessDeniedHandler)
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )

                .build();
    }


}





