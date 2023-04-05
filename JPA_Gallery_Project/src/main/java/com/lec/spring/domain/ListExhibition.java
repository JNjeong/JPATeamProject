package com.lec.spring.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListExhibition {

    @JsonProperty("ListExhibitionOfSeoulMOAInfo")
    MOA MOA;

}
