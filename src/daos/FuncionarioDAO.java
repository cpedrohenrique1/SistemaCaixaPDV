package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.Funcionario;
import utils.DatabaseConnection;

public class FuncionarioDAO {
    private final DatabaseConnection connectionFactory = new DatabaseConnection();

    public Funcionario buscarPorUsuario(String nomeUsuario) throws SQLException {
        String sql = "SELECT * FROM Funcionarios WHERE nomeUsuario = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }
        }

        return null;
    }

    public Funcionario buscarPorId(UUID idFuncionario) throws SQLException {
        String sql = "SELECT * FROM Funcionarios WHERE pkFuncionario = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }
        }

        return null;
    }

    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionarios ORDER BY nomeCompleto";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                funcionarios.add(mapearFuncionario(rs));
            }
        }

        return funcionarios;
    }

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO Funcionarios (nomeCompleto, nomeUsuario, senha, perfil) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.nomeCompleto());
            stmt.setString(2, funcionario.nomeUsuario());
            stmt.setString(3, funcionario.senha());
            stmt.setString(4, funcionario.perfil());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE Funcionarios SET nomeCompleto = ?, nomeUsuario = ?, senha = ?, perfil = ? WHERE pkFuncionario = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.nomeCompleto());
            stmt.setString(2, funcionario.nomeUsuario());
            stmt.setString(3, funcionario.senha());
            stmt.setString(4, funcionario.perfil());
            stmt.setObject(5, funcionario.pkFuncionario());
            stmt.executeUpdate();
        }
    }

    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        return new Funcionario(
                rs.getObject("pkFuncionario", UUID.class),
                rs.getString("nomeCompleto"),
                rs.getString("nomeUsuario"),
                rs.getString("senha"),
                rs.getString("perfil"));
    }
}
