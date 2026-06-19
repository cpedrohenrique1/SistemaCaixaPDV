package repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import daos.ProdutoDAO;
import models.Produto;

public class ProdutoRepository {
    private final ProdutoDAO dao = new ProdutoDAO();

    public Produto buscarPorCodigo(String codigo) throws SQLException {
        return dao.buscarPorCodigo(codigo);
    }

    public Produto buscarPorId(UUID idProduto) throws SQLException {
        return dao.buscarPorId(idProduto);
    }

    public List<Produto> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void salvar(Produto produto) throws SQLException {
        dao.salvar(produto);
    }

    public void atualizar(Produto produto) throws SQLException {
        if (produto.pkProduto() == null) {
            throw new IllegalArgumentException("O identificador do produto é obrigatório para atualização.");
        }

        dao.atualizar(produto);
    }
}
