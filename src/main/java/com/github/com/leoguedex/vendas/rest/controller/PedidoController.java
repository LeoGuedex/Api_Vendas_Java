package com.github.com.leoguedex.vendas.rest.controller;
import com.github.com.leoguedex.vendas.domain.entity.ItemPedido;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.domain.enums.StatusPedido;
import com.github.com.leoguedex.vendas.rest.dto.AtualizacaoStatusPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.InformacaoItemPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.InformacaoPedidoDto;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;
import com.github.com.leoguedex.vendas.service.PedidoService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Api(value = "API DE Pedidos")
@RequestMapping("/api/pedidos")
public class PedidoController {


    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ApiOperation(value = "Cria um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido Criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Integer includePedido(@RequestBody @Valid PedidoDto pedidoDto){
        Pedido pedido = pedidoService.includePedido(pedidoDto);
        return pedido.getId();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Exibe os dados de um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido localizado com sucesso"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
    })
    @ResponseStatus(HttpStatus.OK)
    public InformacaoPedidoDto exibirPedido(@PathVariable Integer id){
        return pedidoService.exibirPedido(id)
                .map(pedido ->  builderInformacaoPedidoDto(pedido))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza um pedido existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Pedido não encontrado"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 405, message = "Opcao de Status Invalida"),
    })
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
