package com.vivek.food.dtp.glitches;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlitchesHandler {

	
	@ExceptionHandler(value= {MethodArgumentNotValidException.class})
	public ResponseEntity<?> getValidationException(MethodArgumentNotValidException e){
		return ResponseEntity
				.badRequest()
				.body(e.getAllErrors()
						.stream()
						.map(x-> x.getDefaultMessage())
						.collect(Collectors.toList()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> getException(Exception e){
		return ResponseEntity.internalServerError().body("Something went wrong.. we are looking into it, please try after sometime.");
	}
}
