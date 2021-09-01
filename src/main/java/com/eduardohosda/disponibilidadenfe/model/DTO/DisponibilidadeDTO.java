package com.eduardohosda.disponibilidadenfe.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DisponibilidadeDTO {
    private String autorizador;
    private Integer quantidade;
}
