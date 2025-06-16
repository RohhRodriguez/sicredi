package com.sicredi.desafio.cadastros.pauta.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PautaDTO {
    @NotNull private String nome;
    @NotNull private String descricao;
    @NotNull private String codigoAssembleia;
}
