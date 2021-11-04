package com.github.com.leoguedex.vendas.service;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;

public interface PedidoService {

    Pedido includePedido(PedidoDto pedidoDto);

}
