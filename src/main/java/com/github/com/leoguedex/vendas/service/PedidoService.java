package com.github.com.leoguedex.vendas.service;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.domain.enums.StatusPedido;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;

import java.util.Optional;

public interface PedidoService {

    Pedido includePedido(PedidoDto pedidoDto);

    Optional<Pedido> exibirPedido(Integer id);

    void updateStatus(Integer id, StatusPedido statusPedido);

}
