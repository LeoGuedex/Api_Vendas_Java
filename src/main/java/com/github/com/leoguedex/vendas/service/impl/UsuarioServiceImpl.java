package com.github.com.leoguedex.vendas.service.impl;

import com.github.com.leoguedex.vendas.domain.entity.Usuario;
import com.github.com.leoguedex.vendas.exception.SenhaInvalidaException;
import com.github.com.leoguedex.vendas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario authenticate(Usuario usuario) {
        Optional<Usuario> usuarioByLogin = usuarioRepository.findByLogin(usuario.getLogin());

        boolean senhaOk = passwordEncoder.matches(usuario.getSenha(), usuarioByLogin.get().getSenha());

        if (!usuarioByLogin.isPresent() || !senhaOk) {
            throw new SenhaInvalidaException();
        }
        return usuarioByLogin.get();
    }
}