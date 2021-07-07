package Services;

import Model.Produto;

import java.io.IOException;

public interface ProdutoServices {

    Produto adicionar(Produto produto) throws IOException;
    Produto remover(Produto produto);
}
