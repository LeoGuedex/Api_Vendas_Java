package com.github.com.leoguedex.vendas.rest;
import com.github.com.leoguedex.vendas.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice // Identifica como controlador de Erros
public class ApplicationControlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) //Retorno desse metodo
    @ExceptionHandler(MethodArgumentNotValidException.class) //Identifica o manipulador de error
    public ApiErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> collectErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(collectErrors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResponseStatusException.class )
    public  ApiErrors handleResponseStatusException(ResponseStatusException ex){
        return new ApiErrors(ex.getReason());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegraNegocioException.class)
    public  ApiErrors handlerRegraNegocioException(RegraNegocioException ex){
        return new ApiErrors(ex.getMessage());
    }


}
