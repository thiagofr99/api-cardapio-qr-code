package com.devthiagofurtado.cardapioqrcode.repository;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.cardapio =:cardapio")
    List<Produto> findAllByCardapio(Cardapio cardapio);

}
