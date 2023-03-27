package com.lec.spring.controller;

import com.lec.spring.service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/display")
public class DisplayController {

    @Autowired
    private DisplayService displayService;

    public DisplayController(){
        System.out.println("DisplayController() 생성");
    }

    @GetMapping("/elist")
    public void displayList(){}





}
