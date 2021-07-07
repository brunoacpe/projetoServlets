package br.com.letscode.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    private String nomeProduto;
    private BigDecimal preco;
    private String ID;
    private BigDecimal precoAvista;
    private BigDecimal precoParcelado;
    private String formaPagamento;

}
