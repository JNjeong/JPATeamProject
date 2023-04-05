package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
        boolean result = User.class.isAssignableFrom(clazz);
        System.out.println(result);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;

        System.out.println("Uservalidate() 호출 : " + user);

        String username = user.getUsername();
        String phonenumber = user.getPhonenumber();
        String email = user.getEmail();

        if(username == null || username.trim().isEmpty()) {
            errors.rejectValue("username", "사용자 아이디는 필수 입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "사용자 이름은 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "사용자 비밀번호는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phonenumber", "사용자 전화번호는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "사용자 이메일은 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "re_password", "비밀번호 확인을 입력해 주십시오");

        // 입력 password, re_password 가 동일한지 비교
        if(!user.getPassword().equals(user.getRe_password())){
            errors.rejectValue("re_password", "비밀번호가 일치하지 않습니다");
        }

        // 핸드폰 번호 확인
        String phoneRegex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

        if(!Pattern.matches(phoneRegex, phonenumber)) {
            errors.rejectValue("phonenumber", "전화번호 입력방식이 잘못됐습니다 다시 입력하십시오.");
        }

        // 이메일 확인
        String emailRegex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        if(!Pattern.matches(emailRegex, email)){
            errors.rejectValue("email", "이메일 입력방식이 잘못됐습니다 다시 입력하십시오.");
        }

    }
}
