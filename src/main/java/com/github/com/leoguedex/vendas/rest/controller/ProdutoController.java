package com.github.com.leoguedex.vendas.rest.controller;

import com.github.com.leoguedex.vendas.domain.entity.Produto;
import com.github.com.leoguedex.vendas.repository.ProdutoRepository;
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

@RestController
@Api(value = "API De Produto")
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final String PRODUTO_NAO_ENCONTRADO = "Produto Não Encontrado";

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @ApiOperation(value = "Cria um novo Produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Produto includeProduto(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}") // Usado para alteraçoes
    @ApiOperation(value = "Atualiza dados de um Produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado"),
    })
    @ResponseStatus(HttpStatus.OK)
    public void updateProduto(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        produtoRepository.findById(id)
                .map(produtoFound -> {
                    produto.setId(produtoFound.getId());
                    produtoRepository.save(produto);
                    return Void.TYPE;
                })
                .orElseThrow(() -> {
                    new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUTO_NAO_ENCONTRADO);
                    return null;
                });
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deleta um Produto Existente Cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 401, message = "Usuario sem autorização"),
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduto(@PathVariable Integer id) {
        produtoRepository.findById(id)
                .map(produtoFound -> {
                    produtoRepository.delete(produtoFound);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUTO_NAO_ENCONTRADO));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Busca um Cliente por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto localizado com sucesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado"),
    })
    public Produto findProdutoById(@PathVariable Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUTO_NAO_ENCONTRADO));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Busca todos os Produtos")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Usuario sem direito de acesso")
    })
    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }


    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<Produto> filterProduto(Produto produto) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Produto> produtoFiltrado = Example.of(produto, exampleMatcher);
        return produtoRepository.findAll(produtoFiltrado);
    }

}
