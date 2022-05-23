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
@JsonPropertyOrder({"id", "produtoNome", "valorProduto", "dataCadastro", "dataAtualizacao", "disponivel", "tipoProdutoVO", "cardapioId"})
public class ProdutoVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String produtoNome;

    private Double valorProduto;

    private LocalDate dataCadastro;

    private LocalDate dataAtualizacao;

    private Boolean disponivel;

    private String observacao;

    private String tipoProdutoVO;

    private Long cardapioId;

}
