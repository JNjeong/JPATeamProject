package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.isAssignableFrom(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Board board = (Board)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject" ,"subject 글제목은 필수입니다.");
    }
}
