package com.github.com.leoguedex.vendas.rest.controller;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;
import com.github.com.leoguedex.vendas.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {


    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer includePedido(@RequestBody PedidoDto pedidoDto){
        Pedido pedido = pedidoService.includePedido(pedidoDto);
        return pedido.getId();

    }

}
