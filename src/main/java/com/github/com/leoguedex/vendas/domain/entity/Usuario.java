package com.github.com.leoguedex.vendas.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login")
//    @NotEmpty(message = "Compo login obrigatorio")
    private String login;

    @Column(name = "senha")
//    @NotEmpty(message = "campo senha obrigatorio")
    private String senha;

    @Column()
    private boolean admin;


}
