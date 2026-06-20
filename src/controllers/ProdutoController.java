package controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import dtos.ProdutoExibicaoDTO;
import models.Produto;
import services.ProdutoService;

public class ProdutoController {
    private final ProdutoService service = new ProdutoService();

    public ProdutoExibicaoDTO pesquisarProdutoCarrinho(String codigo) throws Exception {
        return service.consultarPorCodigo(codigo);
    }

    public void registrarProduto(String codigo, String nome, double preco) throws Exception {
        service.cadastrarNovoProduto(codigo, nome, new BigDecimal(preco));
    }

    public void atualizarProduto(UUID pkProduto, String codigo, String nome, double preco) throws Exception {
        Produto produto = new Produto(pkProduto, codigo, nome, new BigDecimal(preco));
        service.atualizarProduto(produto);
    }

    public List<Produto> listarCatalogoCompleto() throws Exception {
        return service.listarProdutos();
    }

    public Produto buscarProdutoPorId(UUID pkProduto) throws Exception {
        return service.buscarPorId(pkProduto);
    }

    public Produto pesquisarProdutoPorCodigo(String codigo) throws Exception {
        return service.buscarEntidadePorCodigo(codigo);
    }
}
