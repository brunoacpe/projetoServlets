package br.com.letscode.DAO;

import br.com.letscode.Model.Produto;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDAOImpl implements ProdutoDAO{

    private String caminho = ".\\src\\main\\java\\br\\com\\letscode\\EstoqueFile\\estoque.txt";
    private Path path;


    @PostConstruct
    public void init(){
        path = Paths.get(caminho);
    }

    @Override
    public Produto adicionar(Produto produto) throws IOException {

        try(BufferedWriter bf = Files.newBufferedWriter(path)){
            bf.write(formatar(produto));
        }

        return produto;
    }

    @Override
    public Optional<Produto> getPorNome(String nome) {
        List<Produto> produtos = getAll();
        return produtos.stream().filter(n -> n.getNomeProduto().equalsIgnoreCase(nome)).findFirst();
    }

    @Override
    public Optional<Produto> getPorID(String ID) {
        List<Produto> produtos = getAll();
        return produtos.stream().filter(n -> n.getID().equalsIgnoreCase(ID)).findFirst();
    }


    @Override
    public List<Produto> getAll() {
        List<Produto> produtos =  new ArrayList<>();
        try(BufferedReader br = Files.newBufferedReader(path)){
            produtos = br.lines().map(this::converterLinhaEmProduto).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    @Override
    public String formatar(Produto produto) {
        return String.format("%s;%s;%f;%s",produto.getID(),produto.getNomeProduto(),produto.getPreco(),produto.getFormaPagamento());
    }

    public Produto converterLinhaEmProduto(String linha){
        StringTokenizer st = new StringTokenizer(linha,";");
        Produto produto = new Produto();
        produto.setID(st.nextToken());
        produto.setNomeProduto(st.nextToken());
        produto.setPreco(new BigDecimal(st.nextToken()));
        produto.setFormaPagamento(st.nextToken());
        return produto;
    }
    @Override
    public Produto remover(Produto produto) throws IOException {

        List<String>  x = new ArrayList<>();

        String id = produto.getID();
        String line;
        try(BufferedReader br = Files.newBufferedReader(path)){
            while(!(line = br.readLine()).equals("")){
                    if(!line.contains(id)){
                        x.add(line);
                    }
                }
            }

        Files.delete(path);
        PrintWriter writer = new PrintWriter( ".\\src\\main\\java\\br\\com\\letscode\\EstoqueFile\\estoque.txt", StandardCharsets.UTF_8);
        for(String s:x){
            writer.write(s);
        }
        return produto;
    }
}
