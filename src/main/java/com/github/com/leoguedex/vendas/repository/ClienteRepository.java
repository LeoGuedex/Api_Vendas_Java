package com.github.com.leoguedex.vendas.repository;
import com.github.com.leoguedex.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


// Responsavel pela implementação de alteração de dados <Classe, id>
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
