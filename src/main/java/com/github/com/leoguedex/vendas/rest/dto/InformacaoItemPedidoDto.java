package com.github.com.leoguedex.vendas.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformacaoItemPedidoDto {

    private String descricaoProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;
}
