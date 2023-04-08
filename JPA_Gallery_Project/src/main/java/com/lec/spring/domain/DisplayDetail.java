package com.lec.spring.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @JoinColumn(referencedColumnName = "dp_seq")
    @ToString.Exclude
    private Display display;

    // 잔여석
    @Column(nullable = true)
    private Long seatCount;


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "visit_date")
    private LocalDate visitDate;





}
