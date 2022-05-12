package com.devthiagofurtado.cardapioqrcode.repository;

import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query("SELECT e FROM Empresa e WHERE e.empresaNome LIKE CONCAT('%',:nome,'%') AND e.enabled IS TRUE")
    Page<Empresa> findAllByEmpresaName(String nome, Pageable pageable);
}
