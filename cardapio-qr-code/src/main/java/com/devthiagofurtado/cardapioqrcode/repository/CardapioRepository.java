package com.devthiagofurtado.cardapioqrcode.repository;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {

    @Query("SELECT c FROM Cardapio c WHERE c.empresa = :empresa")
    Page<Cardapio> findAllByEmpresa(Empresa empresa, Pageable pageable);
}
