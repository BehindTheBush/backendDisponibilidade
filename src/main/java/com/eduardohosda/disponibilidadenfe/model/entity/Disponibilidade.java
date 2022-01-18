package com.eduardohosda.disponibilidadenfe.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="disponibilidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidade {

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
