package com.github.com.leoguedex.vendas.rest;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

//Classe que ira receber os erros.
public class ApiErrors {

    @Getter
    private List<String> erros;

    public ApiErrors(List<String> erros){
        this.erros = erros;
    }

    public ApiErrors(String erro){
        this.erros = Arrays.asList(erro);
    }

}
