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
        //Gerando o ID random.
        //TODO -- Fazer a validação do tipo de pagamento para diminuir ou aumentar o preço.
        produto.setID(UUID.randomUUID().toString());
        String formaPagamento = produto.getFormaPagamento();

        return produtoDAO.adicionar(produto);
    }

    @Override
    public Produto remover(String ID) throws IOException {
        //TODO-- Implementar este método
        return produtoDAO.remover(ID);
    }
}
