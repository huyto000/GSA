package com.example.GemSkillAssessment.error;


import com.example.GemSkillAssessment.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GSAGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotImplementedException.class)
    public void notImolemented(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value());
    }

    @ExceptionHandler(NotAcceptableException.class)
    public void notAcceptable(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
    }


    @ExceptionHandler(NotFoundException.class)
    public void notFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDto> userNameNotFound(HttpServletResponse response) throws IOException {
        ErrorDto error = new ErrorDto();
        error.setErrorId(3);
        error.setMessage("USERNAME_NOT_FOUND");
        //response.sendError(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<ErrorDto>(error, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(DuplicateEntryException.class)
    public void dublicateEntry(HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_ACCEPTABLE.value());

    }


}
