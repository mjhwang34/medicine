package com.monorama.medicine;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum StatusEnum {
	OK("200", "OK", HttpStatus.OK),
    NOT_FOUND("404", "NOT_FOUND", HttpStatus.NOT_FOUND)
    //LOGIN_FAIL("401", "LOGIN_FAIL", HttpStatus.UNAUTHORIZED),
    //DUPLICATED("001", "DUPLICATED", HttpStatus.BAD_REQUEST)
    ;

    String statusCode;
    String statusResult;
    HttpStatus httpStatus;

    StatusEnum(String statusCode, String statusResult, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.statusResult = statusResult;
        this.httpStatus = httpStatus;
    }

}