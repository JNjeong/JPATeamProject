package com.lec.spring.controller;


import com.lec.spring.domain.Comment;
import com.lec.spring.domain.QryCommentList;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import com.lec.spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
        System.out.println(getClass().getName() + "() 생성");
    }


    @GetMapping("/list")
    public QryCommentList list(Long id){
        return commentService.list(id);
    }

    @PostMapping("/write")
    public QryResult write(
            @RequestParam("board_id") Long boardId,
            @RequestParam("user_id") Long userId,
            String content
    ){
        return commentService.write(boardId, userId, content);
    }

    @PostMapping("/delete")
    public QryResult detele(Long id){
        return commentService.delete(id);
    }


}
