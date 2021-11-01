package com.github.com.leoguedex.vendas.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "produto")
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descricao", length = 100)
    @NotEmpty(message = "Campo descrição é obrigatório")
    private String descricao;

    @Column(name = "preco_unitario")
    @NotNull(message = "Campo preco é obrigatório")
    private BigDecimal preco;


}