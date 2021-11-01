package com.github.com.leoguedex.vendas.domain.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

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
    @NotEmpty(message = "Campo nome é obrigatório")
    private String nome;

    @Column(name = "cpf", length = 11) // Nome coluna e tamanho da coluna
    @CPF(message = "Informe um CPF válido") // Validador de CPF
    @NotEmpty(message = "Campo CPF é obrigatório")
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidos;

}
