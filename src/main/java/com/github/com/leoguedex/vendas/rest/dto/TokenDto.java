package com.github.com.leoguedex.vendas.rest.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    private String login;
    private String senha;
}
