package com.eduardohosda.disponibilidadenfe.model.repository;

import com.eduardohosda.disponibilidadenfe.model.entity.Disponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {

    //List<Disponibilidade> findByAutorizador(String autorizador);
    List<Disponibilidade> findByDataCadastroBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Disponibilidade> findFirst15ByOrderByDataCadastroDesc();
    Disponibilidade findFirstByAutorizadorOrderByDataCadastroDesc(String autorizador);

}
