package com.monorama.medicine;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.monorama.medicine.model.Message;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(com.monorama.medicine.exception.NotFoundException.class)
	public ResponseEntity<Message> NotFoundException(){
		Message message = new Message();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		message.setCode(StatusEnum.NOT_FOUND.getStatusCode());
		message.setResult(StatusEnum.NOT_FOUND.getStatusResult());
		
		return new ResponseEntity<>(message, headers, StatusEnum.NOT_FOUND.getHttpStatus());
	}
	
	/*
	@ExceptionHandler(AuthException.class)
	public ResponseEntity authException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	@ExceptionHandler(LoginFailException.class)
	public ResponseEntity loginFailException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Message> NotFoundException(){
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));


        message.setCode(StatusEnum.NOT_FOUND.getStatusCode());
        message.setMessage("찾을 수 없음");
        
        return new ResponseEntity<>(message, headers, StatusEnum.NOT_FOUND.getHttpStatus());
	}
	
	@ExceptionHandler(DuplicatedException.class)
	public ResponseEntity<Message> DuplicatedException(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}*/
}