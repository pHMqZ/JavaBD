package jmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);
	
	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "root";
		String SENHA = "";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		} catch (Exception e) {
			if(e instanceof ClassNotFoundException)
				System.out.println("Verifique o driver de conex„o");
			else
				System.out.println("Verifique se o servidor est· ativo");
			System.exit(-42);
			return null;
		}
	}
	
	public static void desconectar(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("N„o foi possÌvel desconectar");
				e.printStackTrace();
			}
		}
	}
	
	public static void listar() {
		System.out.println("Listando produtos...");
	}
	
	public static void inserir() {
		System.out.println("Inserindo produtos...");
	}
	
	public static void atualizar() {
		System.out.println("Atualizando produtos...");
	}
	
	public static void deletar() {
		System.out.println("Deletando produtos...");
	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma op√ß√£o: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(teclado.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Op√ß√£o inv√°lida.");
		}
	}
}
