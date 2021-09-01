package com.eduardohosda.disponibilidadenfe.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="disponibilidade")
/*
* Afim de deixar o codigo mais limpo adicionei o Lombok, para gerar os getter e setter em tempo de compilação
* Para que não fique com erro na IDE basta instalar o plugin do lombok
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidade {

    //Entidade Disponibilidade alimentado pelo site nfe.fazenda.gov.br/portal/disponobilidade
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String autorizador;

    @Column
    private Boolean autorizacao;

    @Column(name="retorno_autorizacao")
    private Boolean retornoAutorizacao;

    @Column
    private Boolean inutilizacao;

    @Column(name="consulta_protocolo")
    private Boolean consultaProtocolo;

    @Column(name="status_servico")
    private Boolean statusServico;

    @Column(name="consulta_cadastro")
    private Boolean consultaCadastro;

    @Column(name="recepcao_evento")
    private Boolean recepcaoEvento;

    @Column(name="data_cadastro")
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDateTime.now());
    }


}
