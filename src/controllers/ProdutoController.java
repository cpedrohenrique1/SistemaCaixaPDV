package controllers;

import java.util.List;

import dtos.ProdutoExibicaoDTO;
import models.Produto;
import services.ProdutoService;

public class ProdutoController {
	private final ProdutoService service = new ProdutoService();

    public ProdutoExibicaoDTO pesquisarProdutoCarrinho(String codigo) throws Exception {
        return service.consultarPorCodigo(codigo);
    }

    public void registrarProduto(String codigo, String nome, double preco) throws Exception {
        Produto p = new Produto(0, codigo, nome, preco);
        service.cadastrarNovoProduto(p);
    }

    public List<Produto> listarCatalogoCompleto() throws Exception {
        return service.obterCatalogo();
    }
}
