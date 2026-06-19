package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.Produto;
import utils.DatabaseConnection;

public class ProdutoDAO {
    private final DatabaseConnection connectionFactory = new DatabaseConnection();

    public Produto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    public Produto buscarPorId(UUID idProduto) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE pkProduto = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos ORDER BY nome";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    public void salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (codigo, nome, preco) VALUES (?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.codigoProduto());
            stmt.setString(2, produto.nomeProduto());
            stmt.setBigDecimal(3, produto.precoProduto());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE Produtos SET codigo = ?, nome = ?, preco = ? WHERE pkProduto = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.codigoProduto());
            stmt.setString(2, produto.nomeProduto());
            stmt.setBigDecimal(3, produto.precoProduto());
            stmt.setObject(4, produto.pkProduto());
            stmt.executeUpdate();
        }
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getObject("pkProduto", UUID.class),
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getBigDecimal("preco"));
    }
}
