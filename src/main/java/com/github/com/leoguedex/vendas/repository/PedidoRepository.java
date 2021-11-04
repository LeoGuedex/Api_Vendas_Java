package com.github.com.leoguedex.vendas.repository;

import com.github.com.leoguedex.vendas.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
