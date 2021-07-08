package br.com.letscode.Servlet;


import br.com.letscode.Model.MensagemErro;
import br.com.letscode.Model.Produto;
import br.com.letscode.Services.ProdutoServices;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@WebServlet("/commerceProdutos")
public class CommerceServlet extends HttpServlet {

    @Inject
    private ProdutoServices produtoServices;
    public static final String PRODUTOS_SESSION = "produtos";
    private Gson gson;

    @PostConstruct
    public void init(){
        gson = new Gson();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO-- ERRO AQUI;
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BufferedReader br = req.getReader();
        String line = "";
        StringBuilder conteudo = new StringBuilder();
        while((line= br.readLine())!=null){
            conteudo.append(line);
        }
        Produto produtoReq = gson.fromJson(conteudo.toString(),Produto.class);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter print = resp.getWriter();
        String resposta = "";
        
        if(produtoReq.getNomeProduto()==null||produtoReq.getPreco()==null||produtoReq.getFormaPagamento()==null){
            MensagemErro mensagemErro = new MensagemErro("Parâmetros inválidos.",HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(mensagemErro.getStatus());
            resposta = gson.toJson(mensagemErro);
        } else {

            HttpSession httpSession = req.getSession(true);
            List<Produto> produtos;
            if((produtos =  (List<Produto>) httpSession.getAttribute(PRODUTOS_SESSION))==null){
                produtos = new ArrayList<>();
            }
            produtoServices.adicionar(produtoReq);
            produtos.add(produtoReq);
            httpSession.setAttribute(PRODUTOS_SESSION,produtos);
            resposta = gson.toJson(produtos);
        }

        print.write(resposta);
        print.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pesquisaNome = req.getParameter("nome");
        HttpSession httpSession =req.getSession();
        List<Produto> produtoList = (List<Produto>) httpSession.getAttribute(PRODUTOS_SESSION);
        PrintWriter print = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        if(pesquisaNome!=null&& Objects.nonNull(produtoList)){
            Optional<Produto> optionalProduto = produtoList.stream().filter(n -> n.getNomeProduto().equals(pesquisaNome)).findFirst();
            if(optionalProduto.isPresent()){
                print.write(gson.toJson(optionalProduto.get()));
            } else {
                MensagemErro mensagemErro = new MensagemErro("Conteúdo não encontrado.", 404);
                resp.setStatus(404);
                print.write(gson.toJson(mensagemErro));
            }
        } else {

            print.write(gson.toJson(produtoList));

        }
        print.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        HttpSession httpSession = req.getSession();
        List<Produto> produtoList = (List<Produto>) httpSession.getAttribute(PRODUTOS_SESSION);
        PrintWriter print = resp.getWriter();

    }


}
