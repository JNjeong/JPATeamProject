package com.lec.spring.controller;


import com.lec.spring.domain.Comment;
import com.lec.spring.domain.QryCommentList;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import com.lec.spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController //data를 response 한다. ('View'를 리턴하는게 아니다)
@RequestMapping("/comment")
public class CommentController {
    //java객체는 Json의 object로 변환된다

    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
        System.out.println(getClass().getName() + "() 생성");
    }

    @GetMapping("/test1")
    public QryCommentList test1(Long id){

        QryCommentList list = new QryCommentList();

        list.setCount(1);
        list.setStatus("OK");
        Comment cmt = Comment.builder()
                .user(User.builder().username("한가위").id(34L).name("대보름").build())
                .content("정말 재밌어요")
                .build();
        List<Comment> cmtList = new ArrayList<>();
        cmtList.add(cmt);
        list.setList(cmtList);

        return list;
    }

    @GetMapping("/test2")
    public List<Integer> test2(){
        List<Integer> list = Arrays.asList(10,20,30);
        return list;
    }

    @GetMapping("/test3")
    public Map<Integer, String> test3(){
        //double-brace 구문 : {{}} (java 11인가부터 추가된 기능)
        Map<Integer, String> myMap = new HashMap<>(){{
            put(100, "백이다");
            put(200, "이백이다");
        }};
        return myMap;
    }

    @GetMapping("/list")
    public QryCommentList list(Long id){
        return commentService.list(id);
    }

    @PostMapping("/write")
    public QryResult write(
            //어느글의 누가 보냈나? 명시되어야 한다
            @RequestParam("write_id") Long writeId,
            @RequestParam("user_id") Long userId,
            String content
    ){
        return commentService.write(writeId, userId, content);
    }

    @PostMapping("/delete")
    public QryResult detele(Long id){
        return commentService.delete(id);
    }


}
