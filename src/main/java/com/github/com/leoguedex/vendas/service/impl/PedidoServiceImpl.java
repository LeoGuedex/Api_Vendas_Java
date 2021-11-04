package com.github.com.leoguedex.vendas.service.impl;

import com.github.com.leoguedex.vendas.domain.entity.Cliente;
import com.github.com.leoguedex.vendas.domain.entity.ItemPedido;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.domain.entity.Produto;
import com.github.com.leoguedex.vendas.domain.enums.StatusPedido;
import com.github.com.leoguedex.vendas.exception.RegraNegocioException;
import com.github.com.leoguedex.vendas.repository.ClienteRepository;
import com.github.com.leoguedex.vendas.repository.ItemPedidoRepository;
import com.github.com.leoguedex.vendas.repository.PedidoRepository;
import com.github.com.leoguedex.vendas.repository.ProdutoRepository;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;
import com.github.com.leoguedex.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {


    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;


    @Override
    public Pedido includePedido(PedidoDto pedidoDto) {
        //Encontra o cliente
        Cliente cliente = findCliente(pedidoDto);
        //Cria um novo pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .dataPedido(LocalDate.now())
                .total(pedidoDto.getTotal())
                .status(StatusPedido.REALIZADO)
                .build();

//
//        Pedido pedido = new Pedido();
//        pedido.setCliente(cliente);
//        pedido.setDataPedido(LocalDate.now());
//        pedido.setStatus(StatusPedido.REALIZADO);
//        pedido.setTotal(pedidoDto.getTotal());

        //Monta os itens do pedido
        if (pedidoDto.getItens().isEmpty()) {
            throw new RegraNegocioException("Não é possivel realizar um pedido sem itens.");
        }

        List<ItemPedido> itens = pedidoDto.getItens().stream()
                .map(itemPedidoDto -> {
                    Produto produto = produtoRepository.findById(itemPedidoDto.getProduto()).orElseThrow(() -> new RegraNegocioException("Código de Cliente inválido"));
                    ItemPedido itemPedido = ItemPedido.builder()
                            .pedido(pedido)
                            .produto(produto)
                            .quantidade(itemPedidoDto.getQuantidade())
                            .build();
//                    ItemPedido itemPedido = new ItemPedido();
//                    itemPedido.setPedido(pedido);
//                    itemPedido.setProduto(produto);
//                    itemPedido.setQuantidade(itemPedidoDto.getQuantidade());
                    return itemPedido;
                })
                .collect(Collectors.toList());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itens);

        return pedido;
    }

    private Cliente findCliente(PedidoDto pedidoDto) {
        return clienteRepository.findById(pedidoDto.getCliente())
                .orElseThrow(() -> new RegraNegocioException("Código de Cliente inválido"));
    }

}
