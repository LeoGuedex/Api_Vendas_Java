package com.github.com.leoguedex.vendas.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data // criar todos os encapsulamentos, equals e toString
@Entity // Informa que é uma classe que sera mapeada para o banco
@NoArgsConstructor // Construtor Vazio
@AllArgsConstructor // Construtor Completo
@Table(name = "cliente") //Definir nome da Tabela
public class Cliente {

    //Atributos do cliente
    @Id //Chave primaria Identification
    @Column(name = "id") // nome da coluna no banco
    @GeneratedValue(strategy = GenerationType.IDENTITY) // como será identificado a chave primaria
    private Integer id;

    @Column(name = "nome", length = 100) // Nome coluna e tamanho da coluna
    private String nome;

    @Column(name = "cpf", length = 11) // Nome coluna e tamanho da coluna
    private String cpf;

}
