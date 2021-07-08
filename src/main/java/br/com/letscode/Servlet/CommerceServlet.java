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
        StringBuilder conteudo = getBody(req);
        Produto produtoReq = gson.fromJson(conteudo.toString(),Produto.class);
        PrintWriter print = prepareResponse(resp);
        String resposta = "";
        if(produtoReq.getNomeProduto()==null||produtoReq.getPrecoVista()==null||produtoReq.getPrecoParcelado()==null){
            MensagemErro mensagemErro = new MensagemErro("Parâmetros inválidos.",HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(mensagemErro.getStatus());
            resposta = gson.toJson(mensagemErro);
        } else {
            HttpSession sessao = req.getSession(true);
            produtoServices.adicionar(produtoReq);
            List<Produto> produtoList = produtoServices.printAll();
            sessao.setAttribute(PRODUTOS_SESSION, produtoList);
            resposta = gson.toJson(produtoList);
        }
        print.write(resposta);
        print.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        HttpSession httpSession = req.getSession();
        List<Produto> produtoList = new ArrayList<>();
        if(Objects.nonNull(httpSession.getAttribute(PRODUTOS_SESSION))){
            produtoList.addAll((List<Produto>) httpSession.getAttribute(PRODUTOS_SESSION));
        }else{
            produtoList.addAll(produtoServices.printAll());
        }

        PrintWriter printWriter = prepareResponse(resp);
        if(id!=null&& Objects.nonNull(produtoList)){
            Optional<Produto> optionalProduto = produtoList.stream().filter(s -> s.getID().equals(id)).findFirst();
            if(optionalProduto.isPresent()){
                printWriter.write(gson.toJson(optionalProduto.get()));
            } else {

                resp.setStatus(404);
                printWriter.write(gson.toJson(new MensagemErro("Conteúdo não encontrado.",404)));
            }

        } else {
            printWriter.write(gson.toJson(produtoList));
        }
        printWriter.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ID = req.getParameter("id");
        String formaPagamento = req.getParameter("formaPagamento");
        PrintWriter printWriter = prepareResponse(resp);
        String resposta;
        if(Objects.isNull(ID)||Objects.isNull(formaPagamento)){
            resposta = erroMessage(resp);
        } else {
            List<Produto> produto = produtoServices.getPorID(ID);
            produtoServices.remover(ID);
            produtoServices.sairEstoque(produto.get(0),formaPagamento);
            resposta = gson.toJson(new MensagemErro("Produto removido", 204));
            req.getSession().setAttribute(PRODUTOS_SESSION,produtoServices.printAll());
        }

    }

    private PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter print = response.getWriter();
        return print;
    }


    private StringBuilder getBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String line="";
        StringBuilder conteudo = new StringBuilder();

        while(null!= (line= br.readLine())){
            conteudo.append(line);
        }
        return conteudo;
    }

    private String erroMessage(HttpServletResponse response) {

        response.setStatus(400);
        return gson.toJson(new MensagemErro("identificador não informado",400));

    }


}
