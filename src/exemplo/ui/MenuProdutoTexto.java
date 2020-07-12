package exemplo.ui;

import exemplo.dao.ProdutoDao;
import exemplo.modelo.Produto;

import java.util.List;

public class MenuProdutoTexto extends MenuEspecificoTexto {
    private ProdutoDao dao;

    public MenuProdutoTexto() {
        super();
        dao = new ProdutoDao();
    }

    private int obterIdProduto() {
        System.out.print("Escolha o id do produto: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        return id;
    }

    private Produto obterDadosProduto(Produto produto) {
        Produto p;

        if (produto == null) {
            p = new Produto();
        } else {
            p = produto;
        }

        System.out.print("Digite o nome do produto: ");
        String nome = entrada.nextLine();
        
        System.out.print("Digite o preço do produto: ");
        double preco = entrada.nextDouble();
        entrada.nextLine();
        
        System.out.print("Digite a quantidade estocada do produto: ");
        int estoque = entrada.nextInt();
        entrada.nextLine();

        p.setNome(nome);
        p.setPreco(preco);
        p.setEstoque(estoque);

        return p;
    }

    @Override
    public void adicionar() {
        System.out.println("Adicionar Produto");
        System.out.println();

        // obter dados do produto
        Produto novoProduto = obterDadosProduto(null);

        // inserir no banco a produto -> DAO
        dao.insert(novoProduto);
    }

    @Override
    public void editar() {
        System.out.println("Editar Produto");
        System.out.println();

        // listar os produtos
        imprimirProdutos();

        // pedir um id de produto
        int id = obterIdProduto();

        Produto produtoAModificar = dao.getById(id);

        // obter os dados do produto
        Produto novoProduto = obterDadosProduto(produtoAModificar);

        // atualizar produto no banco
        novoProduto.setId(produtoAModificar.getId());
        dao.update(novoProduto);
    }

    @Override
    public void excluir() {
        System.out.println("Excluir Produto");
        System.out.println();

        // listar os produtos
        imprimirProdutos();
        // pedir um id de produto
        int id = obterIdProduto();

        // remover produto do banco
        dao.delete(id);
    }

    @Override
    public void listarTodos() {
        System.out.println("Lista de Produtos");
        System.out.println();

        imprimirProdutos();
    }

    private void imprimirProdutos() {
        // obter produtos do banco
        List<Produto> produtos = dao.getAll();

        // imprimir produtos
        System.out.println("id\tNome\tPreço\tEstoque");

        for (Produto p : produtos) {
            System.out.println(p.getId() + "\t" + p.getNome() + "\t" + p.getPreco() + "\t" + p.getEstoque());
        }
    }
}
