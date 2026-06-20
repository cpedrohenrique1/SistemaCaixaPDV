package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SwingUtilities;

import views.LoginView;

public class Main {
	public static void main(String[] args) throws SQLException {
		DatabaseConnection cx = new DatabaseConnection();
		Connection conn = null;
		conn = cx.firstConnection();
		if (conn == null) {
			System.out.println("Erro");
		} else {
			Statement stmt = conn.createStatement();
			System.out.println("Criando banco de dados");
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pdv_supermercado;");
			stmt.executeUpdate("USE pdv_supermercado;");
			stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Funcionarios (" +
	                "    pkFuncionario VARCHAR(36) DEFAULT(UUID()) PRIMARY KEY," +
	                "    nomeCompleto VARCHAR(150) NOT NULL," +
	                "    nomeUsuario VARCHAR(50) NOT NULL UNIQUE," +
	                "    senha VARCHAR(64) NOT NULL," +
	                "    perfil VARCHAR(20) NOT NULL" +
	                ");"
	            );
	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Produtos (" +
	                "    pkProduto VARCHAR(36) DEFAULT(UUID()) PRIMARY KEY," +
	                "    codigo VARCHAR(50) NOT NULL UNIQUE," +
	                "    nome VARCHAR(100) NOT NULL," +
	                "    preco DECIMAL(10,2) NOT NULL" +
	                ");"
	            );

	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Vendas (" +
	                "    pkVenda VARCHAR(36) DEFAULT(UUID()) PRIMARY KEY," +
	                "    dataVenda DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
	                "    total DECIMAL(10,2) NOT NULL," +
	                "    metodoPagamento VARCHAR(30) NOT NULL," +
	                "    fkFuncionario VARCHAR(36) NOT NULL," +
	                "    FOREIGN KEY (fkFuncionario) REFERENCES Funcionarios(pkFuncionario)" +
	                ");"
	            );

	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS ItemVenda (" +
	                "    pkItemVenda VARCHAR(36) DEFAULT(UUID()) PRIMARY KEY," +
	                "    quantidade INT NOT NULL," +
	                "    precoUnitario DECIMAL(10,2) NOT NULL," +
	                "    fkVenda VARCHAR(36) NOT NULL," +
	                "    fkProduto VARCHAR(36) NOT NULL," +
	                "    FOREIGN KEY (fkVenda) REFERENCES Vendas(pkVenda)," +
	                "    FOREIGN KEY (fkProduto) REFERENCES Produtos(pkProduto)" +
	                ");"
	            );

	            System.out.println("Inserindo dados no banco");
	            String hashSenhaAdmin = Criptografia.hashSenha("123456");
	            
	            String sqlAdmin = "INSERT IGNORE INTO Funcionarios (nomeCompleto, nomeUsuario, senha, perfil) " +
	                              "VALUES ('Administrador do Sistema', 'admin', '" + hashSenhaAdmin + "', 'GERENTE');";
	            stmt.executeUpdate(sqlAdmin);
	            String sqlFuncionario = "INSERT IGNORE INTO Funcionarios (nomeCompleto, nomeUsuario, senha, perfil) " +
                        "VALUES ('Caixa', 'caixa', '" + hashSenhaAdmin + "', 'CAIXA');";
	            stmt.executeUpdate(sqlFuncionario);
	            System.out.println("Inserindo alguns produtos de teste...");
	            stmt.executeUpdate("INSERT IGNORE INTO Produtos (codigo, nome, preco) VALUES ('1', 'Arroz 5kg', 24.90);");
	            stmt.executeUpdate("INSERT IGNORE INTO Produtos (codigo, nome, preco) VALUES ('2', 'Feijão Carioca 1kg', 7.50);");
	            stmt.executeUpdate("INSERT IGNORE INTO Produtos (codigo, nome, preco) VALUES ('3', 'Leite 1L', 4.50);");

	            System.out.println("Banco de dados e tabelas inicializados com sucesso!");
			SwingUtilities.invokeLater(() -> {
	            LoginView login = new LoginView();
	            login.setVisible(true);
	        });
		}
	}
}
