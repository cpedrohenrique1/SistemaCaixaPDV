package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Produto;
import utils.DatabaseConnection;

public class ProdutoRepository {
	DatabaseConnection conexaoBanco = new DatabaseConnection();
	
	public Produto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE codigo = ?";
        try (Connection conn = conexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                        rs.getInt("pkProduto"),
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                    );
                }
            }
        }
        return null;
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Produtos ORDER BY nome";
        try (Connection conn = conexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("pkProduto"),
                    rs.getString("codigo"),
                    rs.getString("nome"),
                    rs.getDouble("preco")
                ));
            }
        }
        return lista;
    }

    public void salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (codigo, nome, preco) VALUES (?, ?, ?)";
        try (Connection conn = conexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.codigo());
            stmt.setString(2, produto.nome());
            stmt.setDouble(3, produto.preco());
            stmt.executeUpdate();
        }
    }
}
