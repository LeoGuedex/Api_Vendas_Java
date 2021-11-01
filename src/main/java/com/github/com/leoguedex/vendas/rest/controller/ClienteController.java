package com.github.com.leoguedex.vendas.rest.controller;
import com.github.com.leoguedex.vendas.domain.entity.Cliente;
import com.github.com.leoguedex.vendas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

// Controlador do cliente

@RestController //Identifica a classe como controlador
@RequestMapping("/api/clientes") // Identifica o caminho (url principal) da api
public class ClienteController {

private static final String CLIENTE_NAO_ENCONTRADO = "Produto Não Encontrado";

    @Autowired //Impede a criação de varias instancias e cria um objeto unico, com metodos prontos.
    private ClienteRepository clienteRepository;


    @PostMapping // RequestBody = será requesita  do pelo JSON
    @ResponseStatus(HttpStatus.CREATED) // o retorno web que será retornado
    public Cliente includeCliente(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }


    @PutMapping("/{id}") // Local de acesso para fazer o update do cliente
    @ResponseStatus(HttpStatus.OK) // Retorno padrão
    public void updateCliente(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
        clienteRepository.findById(id)
                .map(clienteFound -> {
                    cliente.setId(clienteFound.getId());
                    clienteRepository.save(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCliente(@PathVariable Integer id) {
        clienteRepository.findById(id)
                .map(clienteFound -> {
                    clienteRepository.delete(clienteFound);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente findClienteById(@PathVariable Integer id){
    return    clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> findAllClientes(){
        return clienteRepository.findAll();
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> filterCliente(Cliente cliente){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Cliente> clienteFiltrado = Example.of(cliente, exampleMatcher);
        return clienteRepository.findAll(clienteFiltrado);
    }

}
