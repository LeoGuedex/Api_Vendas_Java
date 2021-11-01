package com.github.com.leoguedex.vendas.repository;

import com.github.com.leoguedex.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
