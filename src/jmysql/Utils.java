package jmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Utils {

	static Scanner teclado = new Scanner(System.in);

	static int qtd, qtdAll;
	static ResultSet res, resAll;
	static PreparedStatement produtos, produto;
	static Connection conn;
	static String buscarTodos = "select * from produtos";
	static String buscarPorID = "select * from produtos where id=?";
	static String inserir = "insert into produtos (nome, preco, estoque) values (?,?,?)";
	static String atualizar = "update produtos set nome=?, preco=?, estoque=? where id=?";
	static String deletar = "delete from produtos where id=?";

	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "root";
		String SENHA = "";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";

		try {
			Class.forName(CLASSE_DRIVER);
			// System.out.println("Conexão ok");
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

	public static void statement() throws SQLException {
		conn = conectar();
		produtos = conn.prepareStatement(buscarTodos, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		produto = conn.prepareStatement(buscarPorID, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}

	public static void result() throws SQLException {
		res = produto.executeQuery();
		resAll = produtos.executeQuery();

		res.last();
		qtd = res.getRow();
		res.beforeFirst();
		resAll.last();
		qtdAll = resAll.getRow();
		resAll.beforeFirst();
	}

	public static void listar() {

		try {
			statement();
			result();

			if (qtd > 0) {
				System.out.println("---Listando produtos---");
				while (resAll.next()) {
					System.out.println("---------------");
					System.out.println("ID: " + resAll.getInt(1));
					System.out.println("Produto: " + resAll.getString(2));
					System.out.println("Preço: " + resAll.getFloat(3));
					System.out.println("Estoque: " + resAll.getInt(4));
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
		System.out.println("Informe os dados do produtos: ");
		System.out.println("Nome: ");
		String nome = teclado.nextLine();

		System.out.println("Preço: ");
		float preco = teclado.nextFloat();

		System.out.println("Estoque: ");
		int estoque = teclado.nextInt();

		try {
			statement();
			PreparedStatement salvar = conn.prepareStatement(inserir);

			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);

			salvar.executeUpdate();
			salvar.close();

			desconectar(conn);

			System.out.println("Produto " + nome + " salvo com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro salvando produto");
			System.exit(-42);
		}

	}

	public static void atualizar() {
		System.out.println("Informe o código do produto: ");
		int id = Integer.parseInt(teclado.nextLine());

		try {
			statement();

			produto.setInt(1, id);

			if (qtd > 0) {
				System.out.println("Informe os dados do produtos: ");
				System.out.println("Nome: ");
				String nome = teclado.nextLine();

				System.out.println("Preço: ");
				float preco = teclado.nextFloat();

				System.out.println("Estoque: ");
				int estoque = teclado.nextInt();

				PreparedStatement upd = conn.prepareStatement(atualizar);
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);

				upd.executeUpdate();
				upd.close();
				desconectar(conn);

				System.out.println("Produto " + nome + " foi atualizado com sucesso");
			} else {
				System.out.println("Não existe produto com o id informado");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar o produto");
			System.exit(-42);
		}
	}

	public static void deletar() {
		System.out.println("Informe o codigo do produto: ");
		int id = Integer.parseInt(teclado.nextLine());

		try {
			statement();
			produto.setInt(1, id);
			result();

			if (qtd > 0) {
				PreparedStatement del = conn.prepareStatement(deletar);
				del.setInt(1, id);
				del.executeUpdate();
				del.close();
				desconectar(conn);
				System.out.println("Produto excluído com sucesso");
			} else {
				System.out.println("Produto com ID informado não encontrado na lista de dados");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao deletar o produto");
			System.exit(-42);
		}
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
		} else if (opcao == 5) {
			conectar();
		} else {
			System.out.println("Opção Inválida.");
		}
	}
}
