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
		conn = cx.getConnection();
		if (conn == null) {
			System.out.println("Erro");
		} else {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pdv_supermercado;");
			stmt.executeUpdate("USE pdv_supermercado;");
			stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Funcionarios (" +
	                "    pkFuncionario INT AUTO_INCREMENT PRIMARY KEY," +
	                "    nomeCompleto VARCHAR(150) NOT NULL," +
	                "    nomeUsuario VARCHAR(50) NOT NULL UNIQUE," +
	                "    senha VARCHAR(64) NOT NULL," +
	                "    perfil VARCHAR(20) NOT NULL" +
	                ");"
	            );

	            System.out.println("Criando tabela Produtos...");
	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Produtos (" +
	                "    pkProduto INT AUTO_INCREMENT PRIMARY KEY," +
	                "    codigo VARCHAR(50) NOT NULL UNIQUE," +
	                "    nome VARCHAR(100) NOT NULL," +
	                "    preco DECIMAL(10,2) NOT NULL" +
	                ");"
	            );

	            System.out.println("Criando tabela Vendas...");
	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS Vendas (" +
	                "    pkVenda INT AUTO_INCREMENT PRIMARY KEY," +
	                "    dataVenda DATETIME NOT NULL," +
	                "    total DECIMAL(10,2) NOT NULL," +
	                "    metodoPagamento VARCHAR(30) NOT NULL," +
	                "    fkFuncionario INT NOT NULL," +
	                "    FOREIGN KEY (fkFuncionario) REFERENCES Funcionarios(pkFuncionario)" +
	                ");"
	            );

	            System.out.println("Criando tabela ItemVenda...");
	            stmt.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS ItemVenda (" +
	                "    pkItemVenda INT AUTO_INCREMENT PRIMARY KEY," +
	                "    quantidade INT NOT NULL," +
	                "    precoUnitario DECIMAL(10,2) NOT NULL," +
	                "    fkVenda INT NOT NULL," +
	                "    fkProduto INT NOT NULL," +
	                "    FOREIGN KEY (fkVenda) REFERENCES Vendas(pkVenda)," +
	                "    FOREIGN KEY (fkProduto) REFERENCES Produtos(pkProduto)" +
	                ");"
	            );

	            System.out.println("Inserindo usuário administrador padrão...");
	            // 4. Faz a inserção obrigatória usando IGNORE ou uma verificação para não duplicar o admin se rodar duas vezes
	            // Senha padrão: '123456' em hash SHA-256
	            String hashSenhaAdmin = Criptografia.hashSenha("123456");
	            
	            String sqlAdmin = "INSERT IGNORE INTO Funcionarios (nomeCompleto, nomeUsuario, senha, perfil) " +
	                              "VALUES ('Administrador do Sistema', 'admin', '" + hashSenhaAdmin + "', 'GERENTE');";
	            stmt.executeUpdate(sqlAdmin);

	            System.out.println("Inserindo alguns produtos de teste...");
	            stmt.executeUpdate("INSERT IGNORE INTO Produtos (codigo, nome, preco) VALUES ('78910001', 'Arroz 5kg', 24.90);");
	            stmt.executeUpdate("INSERT IGNORE INTO Produtos (codigo, nome, preco) VALUES ('78910002', 'Feijão Carioca 1kg', 7.50);");

	            System.out.println("Banco de dados e tabelas inicializados com sucesso!");
	            
			System.out.println("Conectado!");
			SwingUtilities.invokeLater(() -> {
	            LoginView login = new LoginView();
	            login.setVisible(true);
	        });
		}
	}
}
