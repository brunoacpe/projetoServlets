package Servlet;


import Model.MensagemErro;
import Model.Produto;
import Services.ProdutoServices;
import com.google.gson.Gson;
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
@WebServlet(name="commerceWeb",urlPatterns = "/commerce")
public class CommerceServlet extends HttpServlet {

    @Inject
    private ProdutoServices produtoServices;
    public static final String PRODUTOS_SESSION = "produtos";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 -- Aqui estamos criando um objeto Produto por meio da requisição.
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BufferedReader br = req.getReader();
        String line = "";
        StringBuilder conteudo = new StringBuilder();
        Gson gson = new Gson();
        while((line= br.readLine())!=null){
            conteudo.append(line);
        }
        Produto produtoReq = gson.fromJson(conteudo.toString(),Produto.class);

        //2 -- Setando o tipo do conteúdo que tera na página.
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        //3 -- Criando a mensagem de resposta
        PrintWriter print = resp.getWriter();
        String resposta = "";
        //Caso algum parametro esteja null :
        if(produtoReq.getNomeProduto()==null||produtoReq.getPreco()==null||produtoReq.getFormaPagamento()==null){
            MensagemErro mensagemErro = new MensagemErro("Parâmetros inválidos.",HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(mensagemErro.getStatus());
            resposta = gson.toJson(mensagemErro);
        } else {
            //4 -- Abrindo uma sessão para salvar requests.
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
        Gson gson = new Gson();
        String pesquisaNome = req.getParameter("nome");
        HttpSession httpSession =req.getSession();
        List<Produto> produtoList = (List<Produto>) httpSession.getAttribute(PRODUTOS_SESSION);
        PrintWriter print = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        if(pesquisaNome!=null&& Objects.nonNull(produtoList)){
            Optional<Produto> optionalProduto = produtoList.stream().filter(n -> n.getNomeProduto()==pesquisaNome).findFirst();
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
}
