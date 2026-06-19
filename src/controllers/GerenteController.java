package controllers;

import java.math.BigDecimal;

import services.ProdutoService;

public class GerenteController {
    private final ProdutoService produtoService = new ProdutoService();

    public void cadastrarProduto(String codigo, String nome, BigDecimal preco) throws Exception {
        produtoService.cadastrarNovoProduto(codigo, nome, preco);
    }
}
