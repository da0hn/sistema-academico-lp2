package com.program.vo;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public enum EnumSexo {

    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private final String descricao;

    EnumSexo(final String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return this.descricao;
    }
}
