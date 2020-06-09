package com.program.vo;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class CursoVO {

    private int codigo;
    private String nome;

    public CursoVO() {}

    public CursoVO(int codigo, String nome) {
        this();
        this.codigo = codigo;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
