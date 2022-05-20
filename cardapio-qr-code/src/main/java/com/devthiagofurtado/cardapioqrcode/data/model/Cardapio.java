package com.devthiagofurtado.cardapioqrcode.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cardapio")
public class Cardapio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cardapio_nome", unique = true)
    private String cardapioNome;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    @Column(name = "upload")
    private Boolean upload;

    @Column(name = "url_cardapio")
    private String urlCardapio;

    @Column(name = "url_qrcode")
    private String urlQrcode;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cardapio cardapio = (Cardapio) o;
        return id != null && Objects.equals(id, cardapio.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
