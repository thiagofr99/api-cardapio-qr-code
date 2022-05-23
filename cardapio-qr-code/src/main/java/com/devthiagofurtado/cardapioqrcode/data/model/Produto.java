package com.devthiagofurtado.cardapioqrcode.data.model;

import com.devthiagofurtado.cardapioqrcode.data.enums.TipoProdutoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "produto_nome")
    private String produtoNome;

    @NotNull
    @Column(name = "valor_produto")
    private Double valorProduto;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    @NotNull
    @Column(name = "disponivel")
    private Boolean disponivel;

    @Column(name = "observacao")
    private String observacao;

    @NotNull
    @Column(name = "tipo_produto")
    @Enumerated(EnumType.STRING)
    private TipoProdutoVO tipoProdutoVO;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "cardapio_id")
    private Cardapio cardapio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Produto produto = (Produto) o;
        return id != null && Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
