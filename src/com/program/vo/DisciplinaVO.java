package com.program.vo;

import java.util.Objects;

/**
 * @author Gabriel Honda on 21/06/2020
 * @project lp2_academico
 */
public class DisciplinaVO {
    private Integer codigo;
    private String nome;
    private Integer semestre;
    private CursoVO curso;

    public DisciplinaVO(){
        this.curso = new CursoVO();
    }

    public DisciplinaVO(String nome, Integer semestre, CursoVO curso) {
        this.nome = nome;
        this.semestre = semestre;
        this.curso = curso;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public CursoVO getCurso() {
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        DisciplinaVO that = (DisciplinaVO) o;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nome;
    }
}
