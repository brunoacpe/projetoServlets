package br.com.letscode.DAO;

import br.com.letscode.Model.Produto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProdutoDAO {

    Produto adicionar(Produto produto) throws IOException;
    List<Produto> getPorNome(String nome);
    Optional<Produto> getPorID(String ID);
    List<Produto> getAll();
    String formatar(Produto produto);
    Produto remover(String ID) throws IOException;
}
