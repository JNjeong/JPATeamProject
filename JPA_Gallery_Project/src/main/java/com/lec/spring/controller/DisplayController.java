package com.lec.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Book;
import com.lec.spring.domain.Display;
import com.lec.spring.domain.ListExhibition;
import com.lec.spring.service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/display")
public class DisplayController {

    @Autowired
    private DisplayService displayService;

    public DisplayController(){
        System.out.println("DisplayController() 생성");
    }

    @GetMapping("/elist")
    public void displayAPI(Model model) throws Exception{
        String url = "http://openapi.seoul.go.kr:8088/" + "6e4b4657486869683130306761526963/" + "json/ListExhibitionOfSeoulMOAInfo/1/6/";
        String respStr = readFromUrl(url);

        ObjectMapper mapper = new ObjectMapper();

        ListExhibition info = mapper.readValue(respStr, ListExhibition.class);

        System.out.println(info);

        model.addAttribute("list", info);


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




    @PostMapping("/edetail")
    public void displayDetail(Display display, Model model){
        System.out.println(display);
        model.addAttribute("detail", display);
    }

    @PostMapping("/ereserve")
    public String displayReserve(Display display, Book book, Model model){
        model.addAttribute("detail", display);
        model.addAttribute("book", book);
        model.addAttribute("result", displayService.reserve(book));

        return "display/reserveOk";
    }







}
