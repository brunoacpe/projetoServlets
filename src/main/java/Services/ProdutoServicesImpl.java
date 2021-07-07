package Services;

import DAO.ProdutoDAO;
import Model.Produto;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
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
        return produtoDAO.adicionar(produto);
    }

    @Override
    public Produto remover(Produto produto) {
        //TODO-- Implementar este método
        return produtoDAO.remover(produto);
    }
}
