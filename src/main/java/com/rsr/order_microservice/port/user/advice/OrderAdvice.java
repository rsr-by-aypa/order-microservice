package com.rsr.order_microservice.port.user.advice;


import com.rsr.order_microservice.utils.exceptions.UnknownProductIdException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderAdvice {

    @ResponseBody
    @ExceptionHandler(value = UnknownProductIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String unknownProductIdHandler(UnknownProductIdException e) {
        return "No Product with that id! Your Order can not be accepted.";
    }
}
