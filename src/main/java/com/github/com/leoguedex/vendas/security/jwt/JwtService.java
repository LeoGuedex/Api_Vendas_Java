package com.github.com.leoguedex.vendas.security.jwt;

import com.github.com.leoguedex.vendas.VendasApiApplication;
import com.github.com.leoguedex.vendas.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtService {


    @Value("${jwt.security.expiration}")
    private String jwtSecurityExpiration;

    @Value("${jwt.subscription.key}")
    private String jwtSubscriptionKey;

    //Executar este metodo main para testar os metodos de gerar token e validar token

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(VendasApiApplication.class);
        JwtService jwtService = run.getBean(JwtService.class);
        Usuario usuario = Usuario.builder().login("fulano").build();
        String token = jwtService.gerarToken(usuario);
        System.out.println(token);
        boolean tokenValid = jwtService.tokenValid(token);
        System.out.println("O token está valido ?" + tokenValid);
        String loginUsuario = jwtService.obterLoginUsuario(token);
        System.out.println(loginUsuario);
    }

    public String gerarToken(Usuario usuario){
        long minutesExpire = Long.valueOf(this.jwtSecurityExpiration);
        LocalDateTime plusMinutes = LocalDateTime.now().plusMinutes(minutesExpire);
        Instant toInstant = plusMinutes.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(toInstant);
        String token = Jwts.builder()
                .setSubject(usuario.getLogin())
                .setExpiration(from)
                .signWith(SignatureAlgorithm.HS512, jwtSubscriptionKey)
                .compact();
        return token;
    }

    //tratamento
    public boolean tokenValid(String token){
        try {
            Claims claims = obterClaims(token);
            Date expiration = claims.getExpiration();
            LocalDateTime localDateTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(localDateTime);
        } catch (Exception e){
            return false;
        }
    }

    //obter jwt
    private Claims obterClaims(String token) throws ExpiredJwtException{
        return Jwts.parser()
                .setSigningKey(jwtSubscriptionKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }
}
