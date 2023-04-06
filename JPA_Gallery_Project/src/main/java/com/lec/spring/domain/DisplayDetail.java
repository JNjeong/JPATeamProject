package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_display_detail")
public class DisplayDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DisplayDetail:Display = N:1
    @ManyToOne
    @ToString.Exclude
    private Display display;

    // 잔여석
    @Column(nullable = false)
    private Long seatCount;





}
