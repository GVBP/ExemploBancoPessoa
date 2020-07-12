package exemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exemplo.modelo.Cliente;

public class ClienteDao implements IDao<Cliente> {
	
	public ClienteDao() {
		try {
			createTable();
		} catch (SQLException e) {
			//throw new RuntimeException("Erro ao criar tabela cliente");
			e.printStackTrace();
		}
	}
	
	// Cria a tabela se não existir
	private void createTable() throws SQLException {
		final String sqlCreate = "IF NOT EXISTS (" 
				+ "SELECT * FROM sys.tables t JOIN sys.schemas s ON " 
				+ "(t.schema_id = s.schema_id) WHERE s.name = 'dbo'" 
				+ "AND t.name = 'Cliente')"
				+ "CREATE TABLE Cliente"
				+ " (clienteID int NOT NULL,"
				+ " cpf VARCHAR(11),"
				+ " nome VARCHAR(50),"
				+ " endereco VARCHAR(250),"
				+ " CONSTRAINT PK_Cliente PRIMARY KEY NONCLUSTERED (clienteID),"
				+ " CONSTRAINT FK_Pedido FOREIGN KEY (pedidoID)" 
				+ " REFERENCES ExemploBanco.Pedido (pedidoID)" 
				+ " ON DELETE CASCADE"
				+ " ON UPDATE CASCADE"
				+ " )";
		
		Connection conn = DatabaseAccess.getConnection();
		
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}
	
	public List<Cliente> getAll() {
		Connection conn = DatabaseAccess.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		try {
			stmt = conn.createStatement();
			
			String SQL = "SELECT * FROM Cliente"; // consulta de SELECT
	        rs = stmt.executeQuery(SQL); // executa o SELECT
	        
	        while (rs.next()) {
	        	Cliente c = getClienteFromRs(rs);
	        	
	        	clientes.add(c);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getAllClientes] Erro ao selecionar todos os clientes", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return clientes;		
	}
	
	public Cliente getById(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Cliente cliente = null;
		
		try {
			String SQL = "SELECT * FROM Cliente WHERE id = ?"; // consulta de SELECT
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        rs = stmt.executeQuery(); // executa o SELECT
	        
	        while (rs.next()) {
	        	cliente = getClienteFromRs(rs);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getClienteById] Erro ao selecionar o cliente por id", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return cliente;		
	}
	
	public void insert(Cliente cliente) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "INSERT INTO Cliente (cpf, nome, endereco) VALUES (?, ?, ?)";
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, cliente.getCpf()); // insira na primeira ? o cpf do cliente
	    	stmt.setString(2, cliente.getNome()); // insira na segunda ? o nome do cliente
	    	stmt.setString(3, cliente.getEndereco()); // insira na terceira ? o endereço do cliente
			
	        stmt.executeUpdate(); // executa o SELECT
	        
	        rs = stmt.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	cliente.setId(rs.getInt(1));
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[insereCliente] Erro ao inserir o cliente", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	public void delete(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
			
		try {
			String SQL = "DELETE Cliente WHERE id=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        stmt.executeUpdate(); 			
		} catch (SQLException e) {
			throw new RuntimeException("[deleteCliente] Erro ao remover o cliente por id", e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public void update(Cliente cliente) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "UPDATE Cliente SET cpf = ?, nome = ?, endereco = ? WHERE id=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, cliente.getCpf()); // insira na primeira ? o cpf do cliente
	    	stmt.setString(2, cliente.getNome()); // insira na segunda ? o nome do cliente
	    	stmt.setString(3, cliente.getEndereco()); // insira na terceira ? o endereço do cliente
	    	// insira na última ? o id do cliente
	    	stmt.setInt(4, cliente.getId());
	    	
	        stmt.executeUpdate(); // executa o UPDATE			
		} catch (SQLException e) {
			throw new RuntimeException("[updateCliente] Erro ao atualizar o cliente", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	private Cliente getClienteFromRs(ResultSet rs) throws SQLException {
		Cliente c = new Cliente(); // cria um objeto de cliente
		c.setId(rs.getInt("id")); // insere id recuperado do banco no cliente
		c.setCpf(rs.getString("cpf")); // insere cpf recuperado do banco no cliente
		c.setNome(rs.getString("nome")); // insere nome recuperado do banco no cliente
		c.setEndereco(rs.getString("endereco")); // insere endereço recuperado do banco no cliente
		
		return c;
	}
	
	private void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) { rs.close(); }
			if (stmt != null) { stmt.close(); }
			if (conn != null) { conn.close(); }
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao fechar recursos.", e);
		}
	}
}
