package com.github.com.leoguedex.vendas.rest.controller;
import com.github.com.leoguedex.vendas.domain.entity.Cliente;
import com.github.com.leoguedex.vendas.repository.ClienteRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "API De Cliente")
@RequestMapping("/api/clientes") // Identifica o caminho (url principal) da api
public class ClienteController {

private static final String CLIENTE_NAO_ENCONTRADO = "Produto Não Encontrado";

    @Autowired //Impede a criação de varias instancias e cria um objeto unico, com metodos prontos.
    private ClienteRepository clienteRepository;


    @PostMapping // RequestBody = será requesita  do pelo JSON
    @ApiOperation(value = "Cria um novo Cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
    })
    @ResponseStatus(HttpStatus.CREATED) // o retorno web que será retornado
    public Cliente includeCliente(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }


    @PutMapping("/{id}") // Local de acesso para fazer o update do cliente
    @ApiOperation(value = "Atualiza dados de um Cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Usuario não encontrado"),
    })
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
    @ApiOperation(value = "Deleta um Cliente Existente Cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Usuario não encontrado"),
    })
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
    @ApiOperation(value = "Busca um Cliente por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente localizado com sucesso"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Usuario não encontrado"),
    })
    @ResponseStatus(HttpStatus.OK)
    public Cliente findClienteById(@PathVariable Integer id){
    return    clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    @GetMapping()
    @ApiOperation(value = "Busca todos os clientes")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Usuario não encontrado"),
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> findAllClientes(){
        return clienteRepository.findAll();
    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filtra cliente por letras ou nomes")
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> filterCliente(Cliente cliente){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Cliente> clienteFiltrado = Example.of(cliente, exampleMatcher);
        return clienteRepository.findAll(clienteFiltrado);
    }

}
