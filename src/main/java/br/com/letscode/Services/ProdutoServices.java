package br.com.letscode.Services;

import br.com.letscode.Model.Produto;

import java.io.IOException;

public interface ProdutoServices {

    Produto adicionar(Produto produto) throws IOException;
    Produto remover(String ID) throws IOException;
}
