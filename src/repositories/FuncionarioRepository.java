package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Funcionario;
import utils.DatabaseConnection;

public class FuncionarioRepository {
    DatabaseConnection conexaoBanco = new DatabaseConnection();

    public Funcionario buscarPorUsuario(String nomeUsuario) throws SQLException {
        String sql = "SELECT * FROM Funcionarios WHERE nomeUsuario = ?";
        try (Connection conn = conexaoBanco.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Funcionario(
                            rs.getInt("pkFuncionario"),
                            rs.getString("nomeCompleto"),
                            rs.getString("nomeUsuario"),
                            rs.getString("senha"),
                            rs.getString("perfil"));
                }
            }
        }
        return null;
    }

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO Funcionarios (nomeCompleto, nomeUsuario, senha, perfil) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexaoBanco.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.nomeCompleto());
            stmt.setString(2, funcionario.nomeUsuario());
            stmt.setString(3, funcionario.senha());
            stmt.setString(4, funcionario.perfil());
            stmt.executeUpdate();
        }
    }
}
