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
	
	static Statement produtos;
	static PreparedStatement produto;
	static Connection conn;

	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "root";
		String SENHA = "";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";

		try {
			Class.forName(CLASSE_DRIVER);
			//System.out.println("Conex�o ok");
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		} catch (Exception e) {
			if (e instanceof ClassNotFoundException)
				System.out.println("Verifique o driver de conex�o");
			else
				System.out.println("Verifique se o servidor est� ativo");
			System.exit(-42);
			return null;
		}
	}

	public static void desconectar(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("N�o foi poss�vel desconectar");
				e.printStackTrace();
			}
		}
	}
	
	public static void statement() throws SQLException {
		conn = conectar();
	}

	public static void listar() {
		String buscarTodos = "select * from produtos";

		try {
			statement();
			produtos = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
					System.out.println("Pre�o: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
					System.out.println("---------------");
				}
			} else {
				System.out.println("N�o existem produtos cadastrados");
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
		System.out.println("Informe os dados do produtos: ");
		System.out.println("Nome: ");
		String nome = teclado.nextLine();
		
		System.out.println("Pre�o: ");
		float preco = teclado.nextFloat();
		
		System.out.println("Estoque: ");
		int estoque = teclado.nextInt();
		
		String inserir = "insert into produtos (nome, preco, estoque) values (?,?,?)";
		
		try {
			statement();
			PreparedStatement salvar = conn.prepareStatement(inserir);
			
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);
			
			salvar.executeUpdate();
			salvar.close();
			
			desconectar(conn);
			
			System.out.println("Produto "+nome+" salvo com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro salvando produto");
			System.exit(-42);
		}
		
	}

	public static void atualizar() {
		System.out.println("Informe o c�digo do produto: ");
		int id = Integer.parseInt(teclado.nextLine());
		
		String buscarPorID = "select * from produtos where id=?";
		
		try {
			statement();
			produto = conn.prepareStatement(buscarPorID,ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			
			produto.setInt(1, id);
			
			ResultSet res = produto.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("Informe os dados do produtos: ");
				System.out.println("Nome: ");
				String nome = teclado.nextLine();
				
				System.out.println("Pre�o: ");
				float preco = teclado.nextFloat();
				
				System.out.println("Estoque: ");
				int estoque = teclado.nextInt();
				
				String atualizar = "update produtos set nome=?, preco=?, estoque=? where id=?";
				
				PreparedStatement upd = conn.prepareStatement(atualizar);
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);
				
				upd.executeUpdate();
				upd.close();
				desconectar(conn);
				
				System.out.println("Produto "+nome+" foi atualizado com sucesso");
			} else {
				System.out.println("N�o existe produto com o id informado");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar o produto");
			System.exit(-42);
		}
	}

	public static void deletar() {
		System.out.println("Deletando produtos...");
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma op��o: ");
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
			System.out.println("Op��o Inv�lida.");
		}
	}
}
