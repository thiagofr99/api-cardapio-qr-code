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
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "empresa_nome", unique = true)
    private String empresaNome;

    @Column(name = "cep_endereco", length = 8)
    private String cep;

    @Column(name = "numero_endereco")
    private int numero;

    @Column(name = "complemento_endereco", length = 30)
    private String complemento;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "url_image")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Empresa empresa = (Empresa) o;
        return id != null && Objects.equals(id, empresa.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
