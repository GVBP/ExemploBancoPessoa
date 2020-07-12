package exemplo.ui;

import java.util.List;

import exemplo.dao.PedidoDao;
import exemplo.modelo.Pedido;
import exemplo.modelo.Status;

public class MenuPedidoTexto extends MenuEspecificoTexto {
	
	private static final int OP_ANALISE = 1;
	private static final int OP_PROCESSO = 2;
	private static final int OP_CONCLUIDO = 3;
	
	private Status statusAtual; // armazena o status atual do pedido
    private PedidoDao dao;

    public MenuPedidoTexto() {
        super();
        dao = new PedidoDao();
    }

    private int obterIdPedido() {
        System.out.print("Escolha o id do pedido: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        return id;
    }
    
    private void defineStatus() {
    	System.out.println();
    	System.out.print("1 - Em análise");
    	System.out.print("2 - Em processo");
    	System.out.print("3 - Concluido");
    	int status = entrada.nextInt();
    	
    	switch (status) {
			case OP_ANALISE:
				statusAtual = Status.ANALISE;
				break;
			case OP_PROCESSO:
				statusAtual = Status.PROCESSO;
				break;
			case OP_CONCLUIDO:
				statusAtual = Status.CONCLUIDO;
				break;
			default:
				System.out.println("Opção inválida. Tente novamente!");
		}
    }

    private Pedido obterDadosPedido(Pedido pedido) {
        Pedido p;

        if (pedido == null) {
            p = new Pedido();
        } else {
            p = pedido;
        }
        
        System.out.print("Digite a data do pedido: ");
        String data = entrada.nextLine();

        System.out.print("Digite o status do pedido: ");
        defineStatus();
        
        System.out.print("Digite o endereço da entrega do pedido: ");
        String entrega = entrada.nextLine();
        entrada.nextLine();

        p.setData(data);
        p.setStatusAtual(statusAtual);
        p.setEntrega(entrega);

        return p;
    }

    @Override
    public void adicionar() {
        System.out.println("Adicionar Pedido");
        System.out.println();

        // obter dados do pedido
        Pedido novoPedido = obterDadosPedido(null);

        // inserir no banco a pedido -> DAO
        dao.insert(novoPedido);
    }

    @Override
    public void editar() {
        System.out.println("Editar Pedido");
        System.out.println();

        // listar os pedidos
        imprimirPedidos();

        // pedir um id de pedido
        int id = obterIdPedido();

        Pedido pedidoAModificar = dao.getById(id);

        // obter os dados do pedido
        Pedido novoPedido = obterDadosPedido(pedidoAModificar);

        // atualizar pedido no banco
        novoPedido.setId(pedidoAModificar.getId());
        dao.update(novoPedido);
    }

    @Override
    public void excluir() {
        System.out.println("Excluir Pedido");
        System.out.println();

        // listar os pedidos
        imprimirPedidos();
        // pedir um id de pedido
        int id = obterIdPedido();

        // remover pedido do banco
        dao.delete(id);
    }

    @Override
    public void listarTodos() {
        System.out.println("Lista de Pedidos");
        System.out.println();

        imprimirPedidos();
    }

    private void imprimirPedidos() {
        // obter pedidos do banco
        List<Pedido> pedidos = dao.getAll();

        // imprimir pedidos
        System.out.println("id\tData\tStatus\tEntrega");

        for (Pedido p : pedidos) {
            System.out.println(p.getId() + "\t" + p.getData() + "\t" + p.getStatusAtual() + "\t" + p.getEntrega());
        }
    }
}
