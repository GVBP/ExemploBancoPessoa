package exemplo.ui;

import exemplo.dao.ClienteDao;
import exemplo.modelo.Cliente;

import java.util.List;

public class MenuClienteTexto extends MenuEspecificoTexto {
    private ClienteDao dao;

    public MenuClienteTexto() {
        super();
        dao = new ClienteDao();
    }

    private int obterIdCliente() {
        System.out.print("Escolha o id do cliente: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        return id;
    }

    private Cliente obterDadosCliente(Cliente cliente) {
        Cliente c;

        if (cliente == null) {
            c = new Cliente();
        } else {
            c = cliente;
        }

        System.out.print("Digite o cpf do cliente: ");
        String cpf = entrada.nextLine();
        
        System.out.print("Digite o nome do cliente: ");
        String nome = entrada.nextLine();

        System.out.print("Digite o endereço do cliente: ");
        String endereco = entrada.nextLine();
        entrada.nextLine();

        c.setCpf(cpf);
        c.setNome(nome);
        c.setEndereco(endereco);

        return c;
    }

    @Override
    public void adicionar() {
        System.out.println("Adicionar Cliente");
        System.out.println();

        // obter dados do cliente
        Cliente novoCliente = obterDadosCliente(null);

        // inserir no banco a cliente -> DAO
        dao.insert(novoCliente);
    }

    @Override
    public void editar() {
        System.out.println("Editar Cliente");
        System.out.println();

        // listar os clientes
        imprimirClientes();

        // pedir um id de cliente
        int id = obterIdCliente();

        Cliente clienteAModificar = dao.getById(id);

        // obter os dados do cliente
        Cliente novoCliente = obterDadosCliente(clienteAModificar);

        // atualizar cliente no banco
        novoCliente.setId(clienteAModificar.getId());
        dao.update(novoCliente);
    }

    @Override
    public void excluir() {
        System.out.println("Excluir Cliente");
        System.out.println();

        // listar os clientes
        imprimirClientes();
        // pedir um id de cliente
        int id = obterIdCliente();

        // remover cliente do banco
        dao.delete(id);
    }

    @Override
    public void listarTodos() {
        System.out.println("Lista de Clientes");
        System.out.println();

        imprimirClientes();
    }

    private void imprimirClientes() {
        // obter clientes do banco
        List<Cliente> clientes = dao.getAll();

        // imprimir clientes
        System.out.println("id\tCpf\tNome\tEndereço");

        for (Cliente c : clientes) {
            System.out.println(c.getId() + "\t" + c.getCpf() + "\t" + c.getNome() + "\t" + c.getEndereco());
        }
    }
}
