package jmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			//System.out.println("Conexão ok");
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		} catch (Exception e) {
			if (e instanceof ClassNotFoundException)
				System.out.println("Verifique o driver de conexão");
			else
				System.out.println("Verifique se o servidor está ativo");
			System.exit(-42);
			return null;
		}
	}

	public static void desconectar(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Não foi possível desconectar");
				e.printStackTrace();
			}
		}
	}

	public static void listar() {
		String buscarTodos = "select * from produtos";

		try {
			Connection conn = conectar();
			Statement produtos = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = produtos.executeQuery(buscarTodos);

			res.last();
			int qtd = res.getRow();
			res.beforeFirst();

			if (qtd > 0) {
				System.out.println("---Listando produtos---");
				while (res.next()) {
					System.out.println("---------------");
					System.out.println("ID: " + res.getInt(1));
					System.out.println("Produto: " + res.getString(2));
					System.out.println("Preço: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
					System.out.println("---------------");
				}
			} else {
				System.out.println("Não existem produtos cadastrados");
			}
			produtos.close();
			desconectar(conn);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro buscando produtos");
			System.exit(-42);
		}
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
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");

		int opcao = Integer.parseInt(teclado.nextLine());
		if (opcao == 1) {
			listar();
		} else if (opcao == 2) {
			inserir();
		} else if (opcao == 3) {
			atualizar();
		} else if (opcao == 4) {
			deletar();
		} else if(opcao == 5){
			conectar();
		} else {
			System.out.println("Opção Inválida.");
		}
	}
}
