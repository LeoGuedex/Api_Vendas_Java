package com.github.com.leoguedex.vendas.rest.controller;
import com.github.com.leoguedex.vendas.domain.entity.ItemPedido;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.domain.enums.StatusPedido;
import com.github.com.leoguedex.vendas.rest.dto.AtualizacaoStatusPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.InformacaoItemPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.InformacaoPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;
import com.github.com.leoguedex.vendas.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InformacaoPedidoDto exibirPedido(@PathVariable Integer id){
        return pedidoService.exibirPedido(id)
                .map(pedido ->  builderInformacaoPedidoDto(pedido))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDto atualizacaoStatusPedidoDto){
        String novoStatus = atualizacaoStatusPedidoDto.getNovoStatus();
        pedidoService.updateStatus(id, StatusPedido.valueOf(novoStatus));
    }


    private InformacaoPedidoDto builderInformacaoPedidoDto(Pedido pedido) {
        String dataPedido = pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return InformacaoPedidoDto.builder()
                .codigo(pedido.getId())
                .dataPedido(dataPedido)
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(builderInformacaoItemPedidoDto(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDto> builderInformacaoItemPedidoDto (List<ItemPedido> itens){
       return itens.stream()
                .map(item-> InformacaoItemPedidoDto.builder()
                    .descricaoProduto(item.getProduto().getDescricao())
                    .precoUnitario(item.getProduto().getPreco())
                    .quantidade(item.getQuantidade())
                    .build()
                ).collect(Collectors.toList());
    }


}
