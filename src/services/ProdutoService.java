package services;

import java.sql.SQLException;
import java.util.List;

import dtos.ProdutoExibicaoDTO;
import models.Produto;
import repositories.ProdutoRepository;

public class ProdutoService {
	private final ProdutoRepository repository = new ProdutoRepository();

    public ProdutoExibicaoDTO consultarPorCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de barras inválido ou vazio!");
        }

        Produto produto = repository.buscarPorCodigo(codigo);
        if (produto == null) {
            throw new Exception("Produto não cadastrado no sistema!");
        }

        // Mapeia para o DTO de exibição solicitado pela View
        return new ProdutoExibicaoDTO(
            produto.pkProduto(), 
            produto.codigo(), 
            produto.nome(), 
            produto.preco()
        );
    }

    public List<Produto> obterCatalogo() throws SQLException {
        return repository.listarTodos();
    }

    public void cadastrarNovoProduto(Produto produto) throws Exception {
        if (produto.nome().trim().isEmpty() || produto.codigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Todos os campos do produto são obrigatórios.");
        }
        if (produto.preco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (repository.buscarPorCodigo(produto.codigo()) != null) {
            throw new Exception("Este código de barras já está registrado em outro produto.");
        }
        repository.salvar(produto);
    }
}
