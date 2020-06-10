package com.program.vo;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 10/06/2020
 */
public enum EnumPerfilUsuario {

    ADMINISTRADOR(" Administrador"),
    GESTAO(" Gestão"),
    ATENDENTE(" Atendente");

    private final String descricao;

    EnumPerfilUsuario(final String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
