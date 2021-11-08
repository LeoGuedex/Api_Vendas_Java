package com.github.com.leoguedex.vendas.rest.dto;
import com.github.com.leoguedex.vendas.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto {

    @NotNull(message = "Informe o cliente o código do cliente")
    private Integer cliente;

    @NotNull(message = "Campo total do pedido é obrigatório")
    private BigDecimal total;

    @NotEmptyList(message = "Pedido não pode ser realizado sem itens")
    private List<ItemPedidoDto> itens;


}
