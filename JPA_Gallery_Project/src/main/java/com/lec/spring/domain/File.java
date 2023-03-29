package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id")
    private Long board;  //어느 글의 첨부파일인지? (FK)
    @Column(nullable = false)
    private String source;  //원본 파일명
    @Column(nullable = false)
    private String file;    //저장된 파일명 (rename된 파일명)
    @Transient
    private boolean isImage;    //이미지 파일인지 여부 확인용


}
