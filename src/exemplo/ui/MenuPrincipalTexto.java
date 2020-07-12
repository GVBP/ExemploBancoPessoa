package exemplo.ui;

import java.util.Scanner;

public class MenuPrincipalTexto {
	
	private static final int OP_CLIENTES = 1;
	private static final int OP_PEDIDOS = 2;
	private static final int OP_PRODUTOS = 3;
	private static final int OP_CATEGORIAS = 4;

	private static final int OP_ADICIONAR = 1;
	private static final int OP_LISTAR = 2;
	private static final int OP_EDITAR = 3;
	private static final int OP_EXCLUIR = 4;
	
	// conjunto de estados possiveis no sistema
	private enum Estado {PRINCIPAL, CLIENTES, PEDIDOS, PRODUTOS, CATEGORIAS};
	
	private Estado estadoAtual; // armazena o estado atual do menu
	private Scanner entrada;
	
	public MenuPrincipalTexto() {
		estadoAtual = Estado.PRINCIPAL;
		entrada = new Scanner(System.in); // configura o Scanner para ler da entrada padr�o (STDIN)
	}
	
	private void imprimeMenuPrincipal() {
		System.out.println("1 - Administra��o de Clientes");
		System.out.println("2 - Administra��o de Pedidos");
		System.out.println("3 - Administra��o de Produtos");
		System.out.println("4 - Administra��o de Categorias");
	}
	
	private void imprimeMenuSecund�rio(String tipoMenu) {
		System.out.println("Administra��o de " + tipoMenu);
		System.out.println();
		System.out.println("1 - Adicionar");
		System.out.println("2 - Listar");
		System.out.println("3 - Editar");
		System.out.println("4 - Excluir");
	}
	
	// m�todo principal de execu��o do menu
	public void executa() {
		int opcao;
		MenuEspecificoTexto menuEspecificoTexto;
		
		do {
			// Mostra o menu principal ou o menu secund�rio
			System.out.println("Administra��o de Sistema"); // T�tulo
			System.out.println();
			
			switch(estadoAtual) {
			// se estado CLIENTES imprime menu clientes
			case CLIENTES:
				imprimeMenuSecund�rio("Clientes");
				break;
			// se estado PEDIDOS imprime menu pedidos
			case PEDIDOS:
				imprimeMenuSecund�rio("Pedidos");
				break;
			// se estado PRODUTOS imprime menu produtos
			case PRODUTOS:
				imprimeMenuSecund�rio("Produtos");
				break;
			// se estado CATEGORIAS imprime menu categorias
			case CATEGORIAS:
				imprimeMenuSecund�rio("Categorias");
				break;
			default:
				imprimeMenuPrincipal();
			}
			
			System.out.println();
			System.out.println("0 - Sair");
			
			System.out.println();
			System.out.print("Escolha uma op��o: ");
	
			// obtem entrada do usu�rio
			opcao = entrada.nextInt();
			entrada.nextLine();
			
			System.out.println("Voce escolheu a op��o: " + opcao);
				
			// toma uma a��o conforme o que o usu�rio escolhe
			if (estadoAtual == Estado.PRINCIPAL) {
				switch (opcao) {
				case OP_CLIENTES:
					estadoAtual = Estado.CLIENTES;
					break;
				case OP_PEDIDOS:
					estadoAtual = Estado.PEDIDOS;
					break;
				case OP_PRODUTOS:
					estadoAtual = Estado.PRODUTOS;
					break;
				case OP_CATEGORIAS:
					estadoAtual = Estado.CATEGORIAS;
					break;
				}
			} else {
				if (estadoAtual == Estado.CLIENTES) {
                    menuEspecificoTexto = new MenuClienteTexto();
                } else if (estadoAtual == Estado.PEDIDOS) {
                    menuEspecificoTexto = new MenuPedidoTexto();
                } else if (estadoAtual == Estado.PRODUTOS) {
                	menuEspecificoTexto = new MenuProdutoTexto();
                } else {
                	menuEspecificoTexto = new MenuCategoriaTexto();
                }

				switch (opcao) {
					case OP_ADICIONAR:
						//adicionar um item
						menuEspecificoTexto.adicionar();
						break;
					case OP_EDITAR:
						//editar um item
						menuEspecificoTexto.editar();
						break;
					case OP_EXCLUIR:
						//excluir um item
						menuEspecificoTexto.excluir();
						break;
					case OP_LISTAR:
						//listar um item
						menuEspecificoTexto.listarTodos();
						break;
					case 0:
						break;
					default:
						System.out.println("Op��o inv�lida. Tente novamente!");
				}
			}
			
			
		} while (opcao != 0);// enquanto o usu�rio n�o sai do sistema
		
	}
	
}
