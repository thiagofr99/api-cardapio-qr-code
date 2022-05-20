package com.devthiagofurtado.cardapioqrcode.repository;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {

    //@Query("SELECT e FROM Cardapio c WHERE c.cardapioNome LIKE CONCAT('%',:nome,'%') AND c.upload IS TRUE")
    //Page<Empresa> findAllByEmpresaName(String nome, Pageable pageable);
}
