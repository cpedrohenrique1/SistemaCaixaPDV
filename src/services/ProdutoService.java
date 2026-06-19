package services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import dtos.ProdutoExibicaoDTO;
import models.Produto;
import repositories.ProdutoRepository;

public class ProdutoService {
    private final ProdutoRepository repository = new ProdutoRepository();

    public ProdutoExibicaoDTO consultarPorCodigo(String codigo) throws Exception {
        Produto produto = buscarEntidadePorCodigo(codigo);
        return new ProdutoExibicaoDTO(
                produto.pkProduto(),
                produto.codigoProduto(),
                produto.nomeProduto(),
                produto.precoProduto());
    }

    public Produto buscarEntidadePorCodigo(String codigo) throws Exception {
        validarCodigo(codigo);

        Produto produto = repository.buscarPorCodigo(codigo.trim());
        if (produto == null) {
            throw new Exception("Produto não cadastrado no sistema!");
        }

        return produto;
    }

    public void cadastrarNovoProduto(String codigo, String nome, BigDecimal preco) throws Exception {
        cadastrarNovoProduto(new Produto(null, codigo, nome, preco));
    }

    public void cadastrarNovoProduto(Produto produto) throws Exception {
        validarProduto(produto, false);

        if (repository.buscarPorCodigo(produto.codigoProduto().trim()) != null) {
            throw new Exception("Este código de barras já está registrado em outro produto.");
        }

        repository.salvar(normalizarProduto(produto));
    }

    public void atualizarProduto(Produto produto) throws Exception {
        validarProduto(produto, true);

        Produto existente = repository.buscarPorCodigo(produto.codigoProduto().trim());
        if (existente != null && !existente.pkProduto().equals(produto.pkProduto())) {
            throw new Exception("Este código de barras já está registrado em outro produto.");
        }

        repository.atualizar(normalizarProduto(produto));
    }

    public List<Produto> listarProdutos() throws SQLException {
        return repository.listarTodos();
    }

    public Produto buscarPorId(UUID idProduto) throws SQLException {
        return repository.buscarPorId(idProduto);
    }

    private void validarProduto(Produto produto, boolean exigirId) {
        if (produto == null) {
            throw new IllegalArgumentException("Dados do produto são obrigatórios.");
        }
        if (exigirId && produto.pkProduto() == null) {
            throw new IllegalArgumentException("Selecione um produto para atualização.");
        }
        validarCodigo(produto.codigoProduto());
        if (produto.nomeProduto() == null || produto.nomeProduto().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.precoProduto() == null || produto.precoProduto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de barras inválido ou vazio!");
        }
    }

    private Produto normalizarProduto(Produto produto) {
        return new Produto(
                produto.pkProduto(),
                produto.codigoProduto().trim(),
                produto.nomeProduto().trim(),
                produto.precoProduto());
    }
}
