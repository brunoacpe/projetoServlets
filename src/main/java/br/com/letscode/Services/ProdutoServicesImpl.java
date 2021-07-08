package br.com.letscode.Services;

import br.com.letscode.DAO.ProdutoDAO;
import br.com.letscode.Model.Produto;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoServicesImpl implements ProdutoServices{

    @Inject
    private ProdutoDAO produtoDAO;

    @Override
    public Produto adicionar(Produto produto) throws IOException {
        produto.setID(UUID.randomUUID().toString());
        String formaPagamento = produto.getFormaPagamento();
        return produtoDAO.adicionar(produto);
    }

    @Override
    public Produto remover(String ID) throws IOException {
        return produtoDAO.remover(ID);
    }

    @Override
    public List<Produto> printAll(){
        return produtoDAO.getAll();
    }

    @Override
    public List<Produto> getPorNome(String nome) {
        return produtoDAO.getPorNome(nome);
    }

    @Override
    public Optional<Produto> getPorID(String id) {
        return produtoDAO.getPorID(id);
    }
}
