package com.lec.spring.controller;

import com.lec.spring.domain.Board;
import com.lec.spring.domain.BoardValidator;
//import com.lec.spring.service.BoardService;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    public BoardController(){
        System.out.println("BoardController() 생성");
    }

    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files, //첨부파일들
            @ModelAttribute("dto") Board board
            , Model model
    ){
        model.addAttribute("result", boardService.write(board, files));
        return "board/writeOk";
    }

    @GetMapping("/detail")
    public void detail(long id, Model model){
        model.addAttribute("list", boardService.detail(id));
        model.addAttribute("conPath", Util.getRequest().getContextPath());
    }

    @GetMapping("/list")
    public void list(Integer page, Model model){
        boardService.list(page, model);
    }

    @GetMapping("/update")
    public void update(long id, Model model){
        model.addAttribute("list", boardService.selectById(id));
    }

    @PostMapping("/update")
    public String updateOk(@ModelAttribute("dto") Board board,
                           @RequestParam Map<String, MultipartFile> files,      //새로 추가될 첨부파일들
                           Long[] delfiles,             //삭제될 파일들
                           Model model
    ){
        model.addAttribute("result", boardService.update(board, files, delfiles));
        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        System.out.println("initBinder() 호출");
        binder.setValidator(new BoardValidator());
    }

    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        Util.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/board/list?page=" + page;
    }

}










