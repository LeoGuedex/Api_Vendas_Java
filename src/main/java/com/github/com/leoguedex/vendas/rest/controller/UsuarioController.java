package com.github.com.leoguedex.vendas.rest.controller;

import com.github.com.leoguedex.vendas.domain.entity.Usuario;
import com.github.com.leoguedex.vendas.exception.SenhaInvalidaException;
import com.github.com.leoguedex.vendas.rest.dto.CredencialDto;
import com.github.com.leoguedex.vendas.rest.dto.TokenDto;
import com.github.com.leoguedex.vendas.security.jwt.JwtService;
import com.github.com.leoguedex.vendas.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody @Valid Usuario usuario) {
//        String senha = usuario.getSenha();
//        String senhaEncode = passwordEncoder.encode(senha);
//        usuario.setSenha(senhaEncode);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioService.save(usuario);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto authenticate(@RequestBody CredencialDto credencialDto) {
        try{

            Usuario usuario = Usuario.builder()
                    .login(credencialDto.getLogin())
                    .senha(credencialDto.getSenha())
                    .build();

            Usuario usuarioAutenticado = usuarioService.authenticate(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDto(usuario.getLogin(), token);


        } catch (SenhaInvalidaException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
