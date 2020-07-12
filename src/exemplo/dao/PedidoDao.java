package exemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exemplo.modelo.Pedido;

public class PedidoDao implements IDao<Pedido> {
	
	public PedidoDao() {
		try {
			createTable();
		} catch (SQLException e) {
			//throw new RuntimeException("Erro ao criar tabela pedido");
			e.printStackTrace();
		}
	}
	
	// Cria a tabela se não existir
	private void createTable() throws SQLException {
		final String sqlCreate = "IF NOT EXISTS (" 
				+ "SELECT * FROM sys.tables t JOIN sys.schemas s ON " 
				+ "(t.schema_id = s.schema_id) WHERE s.name = 'dbo'" 
				+ "AND t.name = 'Pedido')"
				+ "CREATE TABLE Pedido"
				+ " (pedidoID int NOT NULL,"
				+ " data VARCHAR(8),"
				+ " statusAtual VARCHAR(50),"
				+ " entrega VARCHAR(250),"
				+ " CONSTRAINT PK_Pedido PRIMARY KEY NONCLUSTERED (pedidoID),"
				+ " CONSTRAINT FK_Cliente FOREIGN KEY (clienteID)" 
				+ " REFERENCES ExemploBanco.Cliente (clienteID)" 
				+ " ON DELETE CASCADE"
				+ " ON UPDATE CASCADE"
				+ " ),"
				+ " CONSTRAINT FK_Item FOREIGN KEY (itemID)" 
				+ " REFERENCES ExemploBanco.Item (itemID)" 
				+ " ON DELETE CASCADE"
				+ " ON UPDATE CASCADE"
				+ " )";
		
		Connection conn = DatabaseAccess.getConnection();
		
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}

	public List<Pedido> getAll() {
		Connection conn = DatabaseAccess.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		
		try {
			stmt = conn.createStatement();
			
			String SQL = "SELECT * FROM Pedido"; // consulta de SELECT
	        rs = stmt.executeQuery(SQL); // executa o SELECT
	        
	        while (rs.next()) {
	        	Pedido p = getPedidoFromRs(rs);
	        	
	        	pedidos.add(p);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getAllPedidos] Erro ao selecionar todos os pedidos", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return pedidos;		
	}
	
	public Pedido getById(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Pedido pedido = null;
		
		try {
			String SQL = "SELECT * FROM Pedido WHERE id = ?"; // consulta de SELECT
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        rs = stmt.executeQuery(); // executa o SELECT
	        
	        while (rs.next()) {
	        	pedido = getPedidoFromRs(rs);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getPedidoById] Erro ao selecionar o pedido por id", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return pedido;		
	}
	
	public void insert(Pedido pedido) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "INSERT INTO Pedido (data, statusAtual, entrega) VALUES (?, ?, ?)";
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    	stmt.setString(1, pedido.getData()); // insira na primeira ? a data do pedido
	    	stmt.setObject(2, pedido.getStatusAtual()); // insira na segunda ? o statusAtual do pedido
	    	stmt.setString(3, pedido.getEntrega()); // insira na terceira ? a entrega do pedido
	    	
			
	        stmt.executeUpdate(); // executa o SELECT
	        
	        rs = stmt.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	pedido.setId(rs.getInt(1));
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[inserePedido] Erro ao inserir o pedido", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	public void delete(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
			
		try {
			String SQL = "DELETE Pedido WHERE id=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        stmt.executeUpdate(); 			
		} catch (SQLException e) {
			throw new RuntimeException("[deletePedido] Erro ao remover o pedido por id", e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public void update(Pedido pedido) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "UPDATE Pedido SET data = ?, statusAtual = ?, entrega = ? WHERE id=?";
			stmt = conn.prepareStatement(SQL);
	    	stmt.setString(1, pedido.getData()); // insira na primeira ? a data do pedido
	    	stmt.setObject(2, pedido.getStatusAtual()); // insira na segunda ? o statusAtual do pedido
	    	stmt.setString(3, pedido.getEntrega()); // insira na terceira ? a entrega d pedido
	    	// insira na última ? o id da pessoa
	    	stmt.setInt(4, pedido.getId());
	    	
	        stmt.executeUpdate(); // executa o UPDATE			
		} catch (SQLException e) {
			throw new RuntimeException("[updatePedido] Erro ao atualizar o pedido", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	private Pedido getPedidoFromRs(ResultSet rs) throws SQLException {
		Pedido p = new Pedido(); // cria um objeto de pessoa
		p.setId(rs.getInt("id")); // insere id recuperado do banco no pedido
		p.setData(rs.getString("data")); // insere data recuperado do banco pedido
		p.setStatusAtual(rs.getObject("statusAtual")); // insere statusAtual recuperado do banco pedido
		p.setEntrega(rs.getString("entrega")); // insere entrega recuperado do banco pedido
		
		return p;
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
