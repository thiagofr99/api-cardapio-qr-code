package com.devthiagofurtado.cardapioqrcode.data.vo;

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
@JsonPropertyOrder({"id", "empresaNome", "cep", "numero", "complemento", "dataCadastro", "dataAtualizacao", "enabled", "imageUrl"})
public class EmpresaVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String empresaNome;

    private String cep;

    private int numero;

    private String complemento;

    private LocalDate dataCadastro;

    private LocalDate dataAtualizacao;

    private Boolean enabled;

    private String imageUrl;

}
