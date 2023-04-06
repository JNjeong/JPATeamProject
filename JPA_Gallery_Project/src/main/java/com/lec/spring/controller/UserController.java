package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserValidator;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserController(UserRepository userRepository){
        System.out.println(getClass().getName() + "() 생성");
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public void login(){}

    @PostMapping("/loginError")
    public String loginError(){
        return "user/login";
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth(){
        return "common/rejectAuth";
    }

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String registerOk(@Valid User user
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs){
        // 중복된 아이디
        if(!result.hasFieldErrors("username") && userService.isExist(user.getUsername())){
            result.rejectValue("username", "이미 존재하는 아이디입니다");
        }

        // 이미 등록된 중복된 전화번호나 이메일이 들어오면
        if(!result.hasFieldErrors("phonenumber") && userService.isPhonenumber(user.getPhonenumber())){
            result.rejectValue("phonenumber", "이미 존재하는 전화번호입니다");
        } else if (!result.hasFieldErrors("email") && userService.isEmail(user.getEmail())) {
            result.rejectValue("email", "이미 존재하는 이메일입니다");
        }

        // 검증 에러가 있었다면 redirect 한다
        if(result.hasErrors()){
            redirectAttrs.addFlashAttribute("username", user.getUsername());
            redirectAttrs.addFlashAttribute("name", user.getName());
            redirectAttrs.addFlashAttribute("password", user.getPassword());
            redirectAttrs.addFlashAttribute("re_password", user.getRe_password());
            redirectAttrs.addFlashAttribute("phonenumber", user.getPhonenumber());
            redirectAttrs.addFlashAttribute("email", user.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error", err.getCode());    // 가장 처음에 발견된 에러를 담아 보냄
                break;
            }

            return "redirect:/user/register";
        }

        // 에러 없었으면 회원 등록 진행
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);
        return "/user/registerOk";
    }

    // 회원 정보 수정
    @GetMapping("/modify")
    public String modify(@AuthenticationPrincipal PrincipalDetails userDetail, Model model){
        Optional<User> user = userRepository.findById(userDetail.getUser().getId());
        User user1 = user.get();
        model.addAttribute("userInfo", user1);
        return "user/modify";
    }

    @PostMapping("/modify")
    public String modifyOk(@Valid User user
            , BindingResult result  // UserValidator 가 유효성 검증한 결과가 담긴 객체
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        // 이미 등록된 중복된 전화번호나 이메일이 들어오면
        if(!result.hasFieldErrors("phonenumber") && userService.isPhonenumber(user.getPhonenumber())){
            result.rejectValue("phonenumber", "이미 존재하는 전화번호입니다");
        } else if (!result.hasFieldErrors("email") && userService.isEmail(user.getEmail())) {
            result.rejectValue("email", "이미 존재하는 이메일입니다");
        }

        // 검증 에러가 있었다면 redirect 한다
        if(result.hasErrors()){
            redirectAttrs.addFlashAttribute("username", user.getUsername());
            redirectAttrs.addFlashAttribute("name", user.getName());
            redirectAttrs.addFlashAttribute("phonenumber", user.getPhonenumber());
            redirectAttrs.addFlashAttribute("email", user.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error", err.getCode());    // 가장 처음에 발견된 에러를 담아 보냄
                break;
            }

            return "redirect:/user/modify?id=" + user.getId();
        }

        model.addAttribute("dto", user);
        model.addAttribute("result", userService.modify(user.getId(), user));
        return "user/modifyOk";
    }


    @GetMapping("/mypage")
    public String view(Principal principal, Model model){
        if(principal == null){
            return "user/login";
        }
        String loginId = principal.getName();
        User user = userService.findByUsername(loginId);
        model.addAttribute("user", user);
        return "user/mypage";
    }

    // 회원 탈퇴
    @GetMapping("/deleteUser")
    public String delete(Principal principal, Model model){
        if(principal == null){
            return "user/login";
        }
        String loginId = principal.getName();
        User user = userService.findByUsername(loginId);
        model.addAttribute("user", user);
        return "user/deleteUser";
    }

    @PostMapping("/deleteUserOk")
    public String deleteUserOk(Principal principal){
        String loginId = principal.getName();
        User user = userService.findByUsername(loginId);
        userService.deleteUser(user.getId());
        return "user/deleteUserOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new UserValidator());
    }
}