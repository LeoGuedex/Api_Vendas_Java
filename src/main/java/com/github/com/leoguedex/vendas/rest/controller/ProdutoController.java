package com.github.com.leoguedex.vendas.rest.controller;

import com.github.com.leoguedex.vendas.domain.entity.Produto;
import com.github.com.leoguedex.vendas.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final String PRODUTO_NAO_ENCONTRADO = "Produto Não Encontrado";

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto includeProduto(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}") // Usado para alteraçoes
    @ResponseStatus(HttpStatus.OK)
    public void updateProduto(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        produtoRepository.findById(id)
                .map(produtoFound -> {
                    produto.setId(produtoFound.getId());
                    produtoRepository.save(produto);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUTO_NAO_ENCONTRADO));
    }


    @DeleteMapping("/{id}")
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
    public Produto findProdutoById(@PathVariable Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUTO_NAO_ENCONTRADO));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
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
