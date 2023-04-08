package com.lec.spring.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.repository.DisplayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DisplayTest {

    @Autowired
    private DisplayRepository displayRepository;

    @Test
    void diplayData() throws Exception{
        String url = "http://openapi.seoul.go.kr:8088/" + "6e4b4657486869683130306761526963/" + "json/ListExhibitionOfSeoulMOAInfo/1/6/";
        String respStr = readFromUrl(url);

        ObjectMapper mapper = new ObjectMapper();

        ListExhibition info = mapper.readValue(respStr, ListExhibition.class);

        info.getMOA().getRows().forEach(diplay -> {
            //System.out.println(diplay);
            displayRepository.saveAndFlush(diplay);
        });



    }

    private String readFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            return bf.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}