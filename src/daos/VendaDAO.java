package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dtos.ItemVendaDetalheDTO;
import dtos.VendaDTO;
import models.ItemVenda;
import models.Venda;
import utils.DatabaseConnection;

public class VendaDAO {
    private final DatabaseConnection connectionFactory = new DatabaseConnection();

    public void salvarVendaCompleta(Venda venda) throws SQLException {
        String sqlVenda = "INSERT INTO Vendas (pkVenda, dataVenda, total, metodoPagamento, fkFuncionario) VALUES (?, NOW(), ?, ?, ?)";
        String sqlItem = "INSERT INTO ItemVenda (quantidade, precoUnitario, fkVenda, fkProduto) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtVenda = null;
        PreparedStatement stmtItem = null;

        try {
            conn = connectionFactory.getConnection();
            conn.setAutoCommit(false);

            UUID idVenda = UUID.randomUUID();

            stmtVenda = conn.prepareStatement(sqlVenda);
            stmtVenda.setString(1, idVenda.toString());
            stmtVenda.setBigDecimal(2, venda.totalVenda());
            stmtVenda.setString(3, venda.metodoPagamento());
            stmtVenda.setString(4, venda.fkFuncionario().toString());
            stmtVenda.executeUpdate();

            stmtItem = conn.prepareStatement(sqlItem);
            for (ItemVenda item : venda.itens()) {
                stmtItem.setInt(1, item.quantidade());
                stmtItem.setBigDecimal(2, item.precoUnitario());
                stmtItem.setString(3, idVenda.toString());
                stmtItem.setString(4, item.fkProduto().toString());
                stmtItem.addBatch();
            }
            stmtItem.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (stmtItem != null) {
                stmtItem.close();
            }
            if (stmtVenda != null) {
                stmtVenda.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<VendaDTO> listarVendas() throws SQLException {
        List<VendaDTO> vendas = new ArrayList<>();
        String sql = "SELECT v.pkVenda, v.dataVenda, v.total, v.metodoPagamento, f.nomeCompleto "
                + "FROM Vendas v "
                + "INNER JOIN Funcionarios f ON f.pkFuncionario = v.fkFuncionario "
                + "ORDER BY v.dataVenda DESC";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vendas.add(new VendaDTO(
                        UUID.fromString(rs.getString("pkVenda")),
                        rs.getString("dataVenda"),
                        rs.getBigDecimal("total"),
                        rs.getString("metodoPagamento"),
                        rs.getString("nomeCompleto")));
            }
        }

        return vendas;
    }

    public List<ItemVendaDetalheDTO> listarItensPorVenda(UUID idVenda) throws SQLException {
        List<ItemVendaDetalheDTO> itens = new ArrayList<>();
        String sql = "SELECT i.pkItemVenda, p.pkProduto, p.codigo, p.nome, i.quantidade, i.precoUnitario "
                + "FROM ItemVenda i "
                + "INNER JOIN Produtos p ON p.pkProduto = i.fkProduto "
                + "WHERE i.fkVenda = ? "
                + "ORDER BY p.nome";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idVenda.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(new ItemVendaDetalheDTO(
                            UUID.fromString(rs.getString("pkItemVenda")),
                            UUID.fromString(rs.getString("pkProduto")),
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("quantidade"),
                            rs.getBigDecimal("precoUnitario")));
                }
            }
        }

        return itens;
    }
}
