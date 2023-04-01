package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert      // insert시 null 인 필드 제외
@DynamicUpdate      // update시 null 인 필드 제외
@Entity(name = "tb_board")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 글 id (PK)
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String content;
    @ColumnDefault(value= "0")
    private long viewCnt;

    @ManyToOne
    @ToString.Exclude
    private User user;   // 글 작성자 (FK)

    // 첨부파일, 댓글
    // Write : File = 1:N
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<FileDTO> fileList = new ArrayList<>();

    public void addFiles(FileDTO... files){
        Collections.addAll(this.fileList, files);
    }

}












