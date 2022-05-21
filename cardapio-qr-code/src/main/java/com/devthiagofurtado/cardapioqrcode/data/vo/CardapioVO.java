package com.devthiagofurtado.cardapioqrcode.data.vo;

import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "cardapioNome", "dataCadastro", "dataAtualizacao", "upload", "urlCardapio", "empresaId"})
public class CardapioVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String cardapioNome;

    private LocalDate dataCadastro;

    private LocalDate dataAtualizacao;

    private Boolean upload;

    private String urlCardapio;

    private String urlQrcode;

    private Long empresaId;

}
