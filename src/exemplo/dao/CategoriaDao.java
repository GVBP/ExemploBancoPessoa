package exemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exemplo.modelo.Categoria;

public class CategoriaDao implements IDao<Categoria> {
	
	public CategoriaDao() {
		try {
			createTableCategoria();
		} catch (SQLException e) {
			//throw new RuntimeException("Erro ao criar tabela categoria");
			e.printStackTrace();
		}
	}
	
	// Cria a tabela se não existir
	private void createTableCategoria() throws SQLException {
		final String sqlCreate = "IF NOT EXISTS (" 
				+ "SELECT * FROM sys.tables t JOIN sys.schemas s ON " 
				+ "(t.schema_id = s.schema_id) WHERE s.name = 'dbo'" 
				+ "AND t.name = 'Categoria')"
				+ "CREATE TABLE Categoria"
				+ " (categoriaID int IDENTITY,"
				+ " nome VARCHAR(50),"
				+ " produtoID int,"
				+ " CONSTRAINT PK_Categoria PRIMARY KEY NONCLUSTERED (categoriaID))"
				+ " ALTER TABLE Categoria" 
				+ " ADD CONSTRAINT FK_Produto FOREIGN KEY (produtoID)" 
				+ " REFERENCES Produto (produtoID)";
		
		Connection conn = DatabaseAccess.getConnection();
		
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}
	
	public List<Categoria> getAll() {
		Connection conn = DatabaseAccess.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Categoria> categorias = new ArrayList<Categoria>();
		
		try {
			stmt = conn.createStatement();
			
			String SQL = "SELECT * FROM Categoria"; // consulta de SELECT
	        rs = stmt.executeQuery(SQL); // executa o SELECT
	        
	        while (rs.next()) {
	        	Categoria c = getCategoriaFromRs(rs);
	        	
	        	categorias.add(c);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getAllCategorias] Erro ao selecionar todos os categorias", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return categorias;		
	}
	
	public Categoria getById(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Categoria categoria = null;
		
		try {
			String SQL = "SELECT * FROM Categoria WHERE categoriaID = ?"; // consulta de SELECT
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        rs = stmt.executeQuery(); // executa o SELECT
	        
	        while (rs.next()) {
	        	categoria = getCategoriaFromRs(rs);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getCategoriaById] Erro ao selecionar o categoria por id", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return categoria;		
	}
	
	public void insert(Categoria categoria) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "INSERT INTO Categoria (nome) VALUES (?)";
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    	stmt.setString(1, categoria.getNome()); // insira na primeira ? o nome do categoria
			
	        stmt.executeUpdate(); // executa o SELECT
	        
	        rs = stmt.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	categoria.setId(rs.getInt(1));
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[insereCategoria] Erro ao inserir o categoria", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	public void delete(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
			
		try {
			String SQL = "DELETE Categoria WHERE categoriaID=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        stmt.executeUpdate(); 			
		} catch (SQLException e) {
			throw new RuntimeException("[deleteCategoria] Erro ao remover o categoria por id", e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public void update(Categoria categoria) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "UPDATE Categoria SET nome = ? WHERE categoriaID=?";
			stmt = conn.prepareStatement(SQL);
	    	stmt.setString(1, categoria.getNome()); // insira na primeira ? o nome do categoria
	    	// insira na última ? o id do categoria
	    	stmt.setInt(2, categoria.getId());
	    	
	        stmt.executeUpdate(); // executa o UPDATE			
		} catch (SQLException e) {
			throw new RuntimeException("[updateCategoria] Erro ao atualizar o categoria", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	private Categoria getCategoriaFromRs(ResultSet rs) throws SQLException {
		Categoria c = new Categoria(); // cria um objeto de categoria
		c.setId(rs.getInt("categoriaID")); // insere id recuperado do banco no categoria
		c.setNome(rs.getString("nome")); // insere nome recuperado do banco no categoria
		
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
