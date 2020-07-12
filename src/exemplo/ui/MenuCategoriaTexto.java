package exemplo.ui;

import exemplo.dao.CategoriaDao;
import exemplo.modelo.Categoria;

import java.util.List;

public class MenuCategoriaTexto extends MenuEspecificoTexto {
    private CategoriaDao dao;

    public MenuCategoriaTexto() {
        super();
        dao = new CategoriaDao();
    }

    private int obterIdCategoria() {
        System.out.print("Escolha o id da categoria: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        return id;
    }

    private Categoria obterDadosCategoria(Categoria categoria) {
        Categoria c;

        if (categoria == null) {
            c = new Categoria();
        } else {
            c = categoria;
        }

        System.out.print("Digite o nome da categoria: ");
        String nome = entrada.nextLine();
        entrada.nextLine();

        c.setNome(nome);

        return c;
    }

    @Override
    public void adicionar() {
        System.out.println("Adicionar Categoria");
        System.out.println();

        // obter dados do categoria
        Categoria novoCategoria = obterDadosCategoria(null);

        // inserir no banco a categoria -> DAO
        dao.insert(novoCategoria);
    }

    @Override
    public void editar() {
        System.out.println("Editar Categoria");
        System.out.println();

        // listar os categorias
        imprimirCategorias();

        // pedir um id de categoria
        int id = obterIdCategoria();

        Categoria categoriaAModificar = dao.getById(id);

        // obter os dados do categoria
        Categoria novoCategoria = obterDadosCategoria(categoriaAModificar);

        // atualizar categoria no banco
        novoCategoria.setId(categoriaAModificar.getId());
        dao.update(novoCategoria);
    }

    @Override
    public void excluir() {
        System.out.println("Excluir Categoria");
        System.out.println();

        // listar os categorias
        imprimirCategorias();
        // pedir um id de categoria
        int id = obterIdCategoria();

        // remover categoria do banco
        dao.delete(id);
    }

    @Override
    public void listarTodos() {
        System.out.println("Lista de Categorias");
        System.out.println();

        imprimirCategorias();
    }

    private void imprimirCategorias() {
        // obter categorias do banco
        List<Categoria> categorias = dao.getAll();

        // imprimir categorias
        System.out.println("id\tNome");

        for (Categoria c : categorias) {
            System.out.println(c.getId() + "\t" + c.getNome());
        }
    }
}
