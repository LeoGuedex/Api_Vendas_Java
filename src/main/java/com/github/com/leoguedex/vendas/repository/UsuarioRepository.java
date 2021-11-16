package com.github.com.leoguedex.vendas.repository;
import com.github.com.leoguedex.vendas.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Querry method
    Optional<Usuario> findByLogin(String login);

    //Estudar Querry Methods
    Optional<Usuario> findByLoginAndSenha(String login, String senha);
}
