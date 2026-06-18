package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.ItemVenda;
import models.Venda;
import utils.DatabaseConnection;

public class VendaRepository {
	public void salvarVendaCompleta(Venda venda) throws SQLException {
        String sqlVenda = "INSERT INTO Vendas (dataVenda, total, metodoPagamento, fkFuncionario) VALUES (NOW(), ?, ?, ?)";
        String sqlItem = "INSERT INTO ItemVenda (quantidade, precoUnitario, fkVenda, fkProduto) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmtVenda = null;
        PreparedStatement stmtItem = null;
        DatabaseConnection conexaoBanco = new DatabaseConnection();

        try {
            conn = conexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia a transação manual para garantir atomicidade

            // 1. Grava a Venda (Cabeçalho) indicando que queremos recuperar a chave primária autogerada
            stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            stmtVenda.setDouble(1, venda.getTotal());
            stmtVenda.setString(2, venda.getMetodoPagamento());
            stmtVenda.setInt(3, venda.getFkFuncionario());
            stmtVenda.executeUpdate();

            // Recupera a pkVenda que acabou de ser gerada no banco de dados
            int idVendaGerado = 0;
            try (ResultSet rs = stmtVenda.getGeneratedKeys()) {
                if (rs.next()) {
                    idVendaGerado = rs.getInt(1);
                }
            }

            if (idVendaGerado == 0) {
                throw new SQLException("Falha ao obter o ID gerado para a Venda.");
            }

            // 2. Grava os Itens da Venda associando a fkVenda correta
            stmtItem = conn.prepareStatement(sqlItem);
            for (ItemVenda item : venda.getItens()) {
                stmtItem.setInt(1, item.getQuantidade());
                stmtItem.setDouble(2, item.getPrecoUnitario());
                stmtItem.setInt(3, idVendaGerado); // Injeta a FK referenciando a tabela Vendas
                stmtItem.setInt(4, item.getFkProduto());
                stmtItem.addBatch(); // Adiciona ao lote de execução rápida
            }
            stmtItem.executeBatch(); // Executa todos os inserts de itens de uma só vez

            conn.commit(); // Salva permanentemente no banco
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Desfaz tudo se der algum erro
            }
            throw e;
        } finally {
            if (stmtItem != null) stmtItem.close();
            if (stmtVenda != null) stmtVenda.close();
            if (conn != null) conn.close();
        }
    }
}
