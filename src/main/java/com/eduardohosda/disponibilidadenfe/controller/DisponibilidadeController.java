package com.eduardohosda.disponibilidadenfe.controller;

import com.eduardohosda.disponibilidadenfe.model.DTO.DisponibilidadeDTO;
import com.eduardohosda.disponibilidadenfe.model.entity.Disponibilidade;
import com.eduardohosda.disponibilidadenfe.model.repository.DisponibilidadeRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/disponibilidades")
@CrossOrigin("*")
public class DisponibilidadeController {

    @Autowired
    private final DisponibilidadeRepository disponibilidadeRepository;

    public DisponibilidadeController(DisponibilidadeRepository disponibilidadeRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
    }

    /*
     * Job para verificar as disponibilidades da nfe
     */
    @Scheduled(fixedDelay = 300000)
    public void verificaDisponibilidade() throws IOException {
        //acessar o site da fazenda
        Document doc = Jsoup.connect("http://www.nfe.fazenda.gov.br/portal/disponibilidade.aspx").get();

        //selecionar a tabela
        Elements tabela = doc.select("table.tabelaListagemDados").select("tr");

        //varrer todas as linhas da tabela
        for (Element linha: tabela) {

            //validação para garantia de armazenar apenas os dados da tabela e não o cabeçalho
            if(linha.attributes().get("class").equals("linhaImparCentralizada") ||
                    linha.attributes().get("class").equals("linhaParCentralizada")) {
                //Nova disponibilidade
                Disponibilidade disponibilidade = new Disponibilidade();

                //atribuir autorizador
                disponibilidade.setAutorizador(linha.child(0).text());

                //validar imagens
                if(linha.child(1).child(0).tagName().equals("img"))
                    disponibilidade.setAutorizacao(linha.child(1).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(2).child(0).tagName().equals("img"))
                    disponibilidade.setRetornoAutorizacao(linha.child(2).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(3).child(0).tagName().equals("img"))
                    disponibilidade.setInutilizacao(linha.child(3).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(4).child(0).tagName().equals("img"))
                    disponibilidade.setConsultaProtocolo(linha.child(4).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(5).child(0).tagName().equals("img"))
                    disponibilidade.setStatusServico(linha.child(5).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(7).child(0).tagName().equals("img"))
                    disponibilidade.setConsultaCadastro(linha.child(7).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));
                if(linha.child(8).child(0).tagName().equals("img"))
                    disponibilidade.setRecepcaoEvento(linha.child(8).child(0).attributes().get("src").equals("imagens/bola_verde_P.png"));

                //Salvar disponibilidade
                disponibilidadeRepository.save(disponibilidade);
            }
        }
    }

    @GetMapping("/status")
    public List<Disponibilidade> disponibilidadeAtual(){
        //Personalizado para buscar as ultimos 15 registros
        List <Disponibilidade> disponibilidades = disponibilidadeRepository.findFirst15ByOrderByDataCadastroDesc();

        return disponibilidades;
    }

    @GetMapping("/status/estado")
    public Disponibilidade disponibilidadeAtualPorEstado(
            @RequestParam("autorizador") String autorizador
    ){
        //personalizado para pegar o ultimo registro do autorizador
        Disponibilidade disponibilidade = disponibilidadeRepository.findFirstByAutorizadorOrderByDataCadastroDesc(autorizador);
        return disponibilidade;
    }

    @GetMapping("/status/data")
    public List<Disponibilidade> disponibilidadePorData(
            @RequestParam("data_inicial")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam("data_final")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal
    ){
        //Personalizado para buscar valores entre datas
        List <Disponibilidade> disponibilidades = disponibilidadeRepository.findByDataCadastroBetween(dataInicial, dataFinal);
        return disponibilidades;
    }

    @GetMapping("/status/indisponibilidade")
    public DisponibilidadeDTO indisponibilidade(){
        //busca tudo
        List <Disponibilidade> disponibilidades = disponibilidadeRepository.findAll();
        //cria um map com um atomicInteger para gerar autoincrement
        Map <String, AtomicInteger> totalizadores = new HashMap<>();
        //Instanciando autorizadores com atomicinteger zerados
        //-------------------------------------------------------------
        totalizadores.put("AM",new AtomicInteger());
        totalizadores.put("BA",new AtomicInteger());
        totalizadores.put("CE",new AtomicInteger());
        totalizadores.put("GO",new AtomicInteger());
        totalizadores.put("MG",new AtomicInteger());
        totalizadores.put("MS",new AtomicInteger());
        totalizadores.put("MT",new AtomicInteger());
        totalizadores.put("PE",new AtomicInteger());
        totalizadores.put("PR",new AtomicInteger());
        totalizadores.put("RS",new AtomicInteger());
        totalizadores.put("SP",new AtomicInteger());
        totalizadores.put("SVAN",new AtomicInteger());
        totalizadores.put("SVRS",new AtomicInteger());
        totalizadores.put("SVC-AN",new AtomicInteger());
        totalizadores.put("SVC-RS",new AtomicInteger());
        //-------------------------------------------------------------
        //vare todas as disponibilidades
        for(Disponibilidade dispo : disponibilidades){
            //Validação para ver se o status foi falso em algum momento, em algum dos serviços e incrementa.
            if(dispo.getAutorizacao() != null && !dispo.getAutorizacao()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getRetornoAutorizacao() != null && !dispo.getRetornoAutorizacao()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getInutilizacao() != null && !dispo.getInutilizacao()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getConsultaProtocolo() != null && !dispo.getConsultaProtocolo()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getStatusServico() != null && !dispo.getStatusServico()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getConsultaCadastro() != null && !dispo.getConsultaCadastro()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }
            if(dispo.getRecepcaoEvento() != null && !dispo.getRecepcaoEvento()){
                totalizadores.get(dispo.getAutorizador()).incrementAndGet();
            }

        }

        String maior = null;
        Integer numero = 0;

        // varre o map, para buscar o maior valor e trazer o autorizador com maior indisponibilidade
        for(var entry:totalizadores.entrySet()){
            //Valida se é a primeira execução para setar os valores padrões
            if(maior != null){
                if(entry.getValue().intValue() > numero){
                    maior = entry.getKey();
                    numero = entry.getValue().intValue();
                }
            }else{
                maior = entry.getKey();
                numero = entry.getValue().intValue();
            }
        }
        DisponibilidadeDTO dto = new DisponibilidadeDTO();

        dto.setAutorizador(maior);
        dto.setQuantidade(numero);
        return dto;

    }


}
