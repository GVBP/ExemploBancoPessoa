package exemplo.modelo; 

//import java.util.List;

public class Pedido {
	private int id;
	private String data;
	private String statusAtual;
	private String entrega;
	//private Cliente cliente;
	//private List<Item> itens;
	
	public Pedido() { }
	
	public Pedido(int id, String data, String statusAtual, String entrega) {		
		this.id = id;
		this.data = data;
		this.statusAtual = statusAtual;
		this.entrega = entrega;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public String getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(String statusAtual) {
		this.statusAtual = statusAtual;
	}

	public String getEntrega() {
		return entrega;
	}

	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", data=" + data + ", statusAtual=" + statusAtual + ", entrega=" + entrega + "]";
	}
	
	
}
