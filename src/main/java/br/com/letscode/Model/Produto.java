package br.com.letscode.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Produto {

    private String nomeProduto;
    private String preco;
    private String ID;
    private BigDecimal precoVista;
    private BigDecimal precoParcelado;
    private String formaPagamento;

}
