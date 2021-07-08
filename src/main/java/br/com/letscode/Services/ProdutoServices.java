package br.com.letscode.Services;

import br.com.letscode.Model.Produto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProdutoServices {

    Produto adicionar(Produto produto) throws IOException;
    Produto remover(String ID) throws IOException;
    List<Produto> printAll();
    List<Produto> getPorNome(String nome);
    List<Produto> getPorID(String id);

    Produto sairEstoque(Produto produto, String formaPagamento);

}
