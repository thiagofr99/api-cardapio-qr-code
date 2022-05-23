package com.devthiagofurtado.cardapioqrcode.data.enums;

import com.devthiagofurtado.cardapioqrcode.util.EnumSerializerCustom;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonSerialize(using = EnumSerializerCustom.class)
public enum TipoProdutoVO {

    BEBIDA(1, "Bebidas"),
    MASSA(2, "Massas"),
    CARNE(3, "Carnes"),
    PROMOCAO(4,"Promoção");

    private TipoProdutoVO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    private Integer codigo;
    private String descricao;

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoProdutoVO retornar(Integer codigo) {
        TipoProdutoVO v = null;
        for (TipoProdutoVO s : TipoProdutoVO.values()) {
            if (Objects.equals(s.getCodigo(), codigo)) {
                v = s;
                break;
            }
        }
        return v;
    }

    public static List<TipoProdutoVO> listar() {

        return Arrays.asList(TipoProdutoVO.values());
    }

    public static TipoProdutoVO buscarPorValorEnum(String nome){
        return Arrays.stream(TipoProdutoVO.values()).filter(t-> t.name().equals(nome)).collect(Collectors.toList()).get(0);
    }

}
