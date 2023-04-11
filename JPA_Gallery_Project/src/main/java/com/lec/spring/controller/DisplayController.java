package com.lec.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.*;
import com.lec.spring.service.DisplayService;
import com.lec.spring.service.RegisterMail;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/display")
public class DisplayController {

    @Autowired
    private DisplayService displayService;

    @Autowired
    private RegisterMail registerMail;

    public DisplayController(){
        System.out.println("DisplayController() 생성");
    }

    @GetMapping("/elist")
    public void displayAPI(Model model) throws Exception{
        String url = "http://openapi.seoul.go.kr:8088/" + "/" + "json/ListExhibitionOfSeoulMOAInfo/1/6/";
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
    public void displayReserve(Display display, Book book, DisplayDetail detail, User user, Model model){
        model.addAttribute("display", display);
        model.addAttribute("book", book);
        model.addAttribute("detail", detail);
        model.addAttribute("user", user);

    }


    @RequestMapping(value = "/getCount")
    @ResponseBody
    public Long getCounts(
            @RequestParam("data2")Long dp_seq,
            @RequestParam("data1")LocalDate visit_date){
        Long seatCount = displayService.getCountSeat(dp_seq, visit_date);
        return seatCount;
//        return 0;
    }



    @PostMapping("/reserveOk")
    public void displayReserveOk(Display display, Book book, DisplayDetail detail, User user, Model model){
        model.addAttribute("display", display);
        model.addAttribute("book", book);
        model.addAttribute("detail", detail);
        model.addAttribute("user", user);
        long bookId = displayService.reserve(display, detail);
        model.addAttribute("result", bookId);

        User userInfo = ((PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        String userName = userInfo.getName();
        String userContact = userInfo.getPhonenumber();
        String email = userInfo.getEmail();
        if(bookId > 0) {
                registerMail.sendMail(email, bookId, userName, userContact, display.getDp_name(), detail.getVisitDate());
        }

    }


}
