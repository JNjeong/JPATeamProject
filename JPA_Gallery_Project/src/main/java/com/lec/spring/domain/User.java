package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name="tb_user") // 테이블 생성
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENT
    private Long id;

    // 아이디
    @Column(unique = true, nullable = false) // unique, not null
    private String username;

    // 비밀번호
    @Column(nullable = false) // not null
    @JsonIgnore // 비밀번호 Json 변환되지 않도록 !
    private String password;

    // 비밀번호 확인 (회원가입 시)
    @Transient // DB에 반영 X
    @ToString.Exclude
    @JsonIgnore
    private String re_password;

    // 이름
    @Column(nullable = false) // not null
    private String name;

    // 이메일
    @Column(unique = true, nullable = false) // unique, not null
    private String email;

    // 휴대폰 번호
    @Column(unique = true, nullable = false) // unique, not null
    private String phonenumber;

    // 권한
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    public void addAuthority(Authority... authorities){
        Collections.addAll(this.authorities, authorities);
    } // xxxToMany
}
