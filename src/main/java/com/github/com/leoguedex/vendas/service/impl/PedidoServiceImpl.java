package com.github.com.leoguedex.vendas.service.impl;

import com.github.com.leoguedex.vendas.domain.entity.Cliente;
import com.github.com.leoguedex.vendas.domain.entity.ItemPedido;
import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import com.github.com.leoguedex.vendas.domain.entity.Produto;
import com.github.com.leoguedex.vendas.domain.enums.StatusPedido;
import com.github.com.leoguedex.vendas.exception.PedidoNaoEncontradoException;
import com.github.com.leoguedex.vendas.exception.RegraNegocioException;
import com.github.com.leoguedex.vendas.repository.ClienteRepository;
import com.github.com.leoguedex.vendas.repository.ItemPedidoRepository;
import com.github.com.leoguedex.vendas.repository.PedidoRepository;
import com.github.com.leoguedex.vendas.repository.ProdutoRepository;
import com.github.com.leoguedex.vendas.rest.dto.PedidoDto;
import com.github.com.leoguedex.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        //Monta os itens do pedido
        validItens(pedidoDto);
        //Encontra o cliente
        Cliente cliente = findCliente(pedidoDto);
        //Cria um novo pedido
        Pedido pedido = builderPedido(pedidoDto, cliente);

        List<ItemPedido> itens = builderItemPedido(pedidoDto, pedido);
//
//        Pedido pedido = new Pedido();
//        pedido.setCliente(cliente);
//        pedido.setDataPedido(LocalDate.now());
//        pedido.setStatus(StatusPedido.REALIZADO);
//        pedido.setTotal(pedidoDto.getTotal());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itens);

        return pedido;
    }

    @Override
    public Optional<Pedido> exibirPedido(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(()-> new PedidoNaoEncontradoException("Pedido não foi localizado."));
    }


    private List<ItemPedido> builderItemPedido(PedidoDto pedidoDto, Pedido pedido) {
        return pedidoDto.getItens().stream()
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
    }

    private void validItens(PedidoDto pedidoDto) {
        if (pedidoDto.getItens().isEmpty()) {
            throw new RegraNegocioException("Não é possivel realizar um pedido sem itens.");
        }
    }

    private Pedido builderPedido(PedidoDto pedidoDto, Cliente cliente) {
        return Pedido.builder()
                .cliente(cliente)
                .dataPedido(LocalDate.now())
                .total(pedidoDto.getTotal())
                .status(StatusPedido.REALIZADO)
                .build();
    }

    private Cliente findCliente(PedidoDto pedidoDto) {
        return clienteRepository.findById(pedidoDto.getCliente())
                .orElseThrow(() -> new RegraNegocioException("Código de Cliente inválido"));
    }

}
