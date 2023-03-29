package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Commnet:User = N:1
    @ManyToOne
    @ToString.Exclude
    private User user;   // 댓글 작성자 (FK)

    @Column(name = "board_id")
    @JsonIgnore   // JSON 변환시 제외하는 필드
    private Long board;   // 어느글의 댓글 (FK)

    @Column(nullable = false)
    private String content;   // 댓글 내용

}












