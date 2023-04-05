package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
@JsonIgnoreProperties(value = {"DP_SUBNAME", "DP_PLACE", "DP_HOMEPAGE", "DP_EVENT", "DP_SPONSOR", "DP_VIEWTIME", "DP_VIEWCHARGE","DP_ART_CNT","DP_VIEWPOINT","DP_INFO","DP_LNK"})
@Entity(name = "tb_display")
public class Display {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("DP_SEQ")
    private Long dp_seq;

    @Column(nullable = false)
    @JsonProperty("DP_NAME")
    private String dp_name;

    @JsonProperty("DP_START")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dp_start;

    @JsonProperty("DP_END")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dp_end;

    @JsonProperty("DP_ART_PART")
    private String dp_art_part;

    @JsonProperty("DP_ARTIST")
    private String dp_artist;

    @JsonProperty("DP_MAIN_IMG")
    private String dp_main_page;


}
