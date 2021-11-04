package com.github.com.leoguedex.vendas.rest.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDto {

    private Integer produto;
    private Integer quantidade;

}
