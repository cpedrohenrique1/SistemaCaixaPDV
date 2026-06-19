package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private String login="root";
	private String senha="password";
	private String host="localhost";
	private String nomeBanco = "pdv_supermercado";
	private String url="jdbc:mysql://"+host+":3306/";
	
	private Connection conexao = null;
	
	public DatabaseConnection() {
		
	}
	
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			return null;
		}
		try {
			this.conexao = (Connection) DriverManager.getConnection(url+nomeBanco,login,senha);
		}catch(SQLException e) {
			return null;
		}
		return this.conexao;
	}
	
	public Connection firstConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			return null;
		}
		try {
			this.conexao = (Connection) DriverManager.getConnection(url,login,senha);
		}catch(SQLException e) {
			return null;
		}
		return this.conexao;
	}
}
