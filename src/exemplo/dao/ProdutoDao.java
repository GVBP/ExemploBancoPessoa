package exemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exemplo.modelo.Produto;

public class ProdutoDao implements IDao<Produto> {

	public ProdutoDao() {
		try {
			createTableProduto();
		} catch (SQLException e) {
			//throw new RuntimeException("Erro ao criar tabela categoria");
			e.printStackTrace();
		}
	}
	
	// Cria a tabela se não existir
	private void createTableProduto() throws SQLException {
		final String sqlCreate = "IF NOT EXISTS (" 
				+ "SELECT * FROM sys.tables t JOIN sys.schemas s ON " 
				+ "(t.schema_id = s.schema_id) WHERE s.name = 'dbo'" 
				+ "AND t.name = 'Produto')"
				+ "CREATE TABLE Produto"
				+ " (produtoID int IDENTITY,"
				+ " nome VARCHAR(250),"
				+ " preco Float(24),"
				+ " estoque int,"
				+ " categoriaID int,"
				+ " itemID int,"
				+ " CONSTRAINT PK_Produto PRIMARY KEY NONCLUSTERED (produtoID))"
				+ " ALTER TABLE Produto" 
				+ " ADD CONSTRAINT FK_Categoria FOREIGN KEY (categoriaID)" 
				+ " REFERENCES Categoria (categoriaID)";
				//+ " CONSTRAINT FK_Item FOREIGN KEY (itemID)" 
				//+ " REFERENCES Item (itemID)";
		
		Connection conn = DatabaseAccess.getConnection();
		
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}
	
	public List<Produto> getAll() {
		Connection conn = DatabaseAccess.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Produto> produtos = new ArrayList<Produto>();
		
		try {
			stmt = conn.createStatement();
			
			String SQL = "SELECT * FROM Produto"; // consulta de SELECT
	        rs = stmt.executeQuery(SQL); // executa o SELECT
	        
	        while (rs.next()) {
	        	Produto p = getProdutoFromRs(rs);
	        	
	        	produtos.add(p);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getAllProdutos] Erro ao selecionar todos os produtos", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return produtos;		
	}
	
	public Produto getById(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Produto produto = null;
		
		try {
			String SQL = "SELECT * FROM Produto WHERE produtoID = ?"; // consulta de SELECT
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        rs = stmt.executeQuery(); // executa o SELECT
	        
	        while (rs.next()) {
	        	produto = getProdutoFromRs(rs);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getProdutoById] Erro ao selecionar o produto por id", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return produto;		
	}
	
	public void insert(Produto produto) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "INSERT INTO Produto (nome, preco, estoque) VALUES (?, ?, ?)";
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    	stmt.setString(1, produto.getNome()); // insira na primeira ? o nome do produto
	    	stmt.setDouble(2, produto.getPreco()); // insira na segunda ? o preço do produto
	    	stmt.setInt(3, produto.getEstoque()); // insira na terceira ? a quantidade em estoque do produto
	    	
			
	        stmt.executeUpdate(); // executa o SELECT
	        
	        rs = stmt.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	produto.setId(rs.getInt(1));
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[insereProduto] Erro ao inserir o produto", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	public void delete(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
			
		try {
			String SQL = "DELETE Produto WHERE produtoID=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        stmt.executeUpdate(); 			
		} catch (SQLException e) {
			throw new RuntimeException("[deleteProduto] Erro ao remover o produto por id", e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public void update(Produto produto) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "UPDATE Produto SET nome = ?, preco = ?, estoque = ? WHERE produtoID=?";
			stmt = conn.prepareStatement(SQL);
	    	stmt.setString(1, produto.getNome()); // insira na primeira ? o nome do produto
	    	stmt.setDouble(2, produto.getPreco()); // insira na segunda ? o preço do produto
	    	stmt.setInt(3, produto.getEstoque()); // insira na terceira ? a quantidade em estoque d produto
	    	// insira na última ? o id da pessoa
	    	stmt.setInt(4, produto.getId());
	    	
	        stmt.executeUpdate(); // executa o UPDATE			
		} catch (SQLException e) {
			throw new RuntimeException("[updateProduto] Erro ao atualizar o produto", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	private Produto getProdutoFromRs(ResultSet rs) throws SQLException {
		Produto p = new Produto(); // cria um objeto de pessoa
		p.setId(rs.getInt("produtoID")); // insere id recuperado do banco no produto
		p.setNome(rs.getString("nome")); // insere nome recuperado do banco produto
		p.setPreco(rs.getDouble("preco")); // insere preço recuperado do banco produto
		p.setEstoque(rs.getInt("estoque")); // insere estoque recuperado do banco produto
		
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
