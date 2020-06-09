package com.program.vo;

import java.util.Objects;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class AlunoVO {

    private int matricula;
    private String nome;
    private String nomeMae;
    private String nomePai;
    private EnumSexo sexo;
    private EnderecoVO endereco;
    private CursoVO curso;

    public AlunoVO() {
        this.endereco = new EnderecoVO();
    }

    public AlunoVO(
            int matricula, String nome, String nomeMae,
            String nomePai, EnumSexo sexo, EnderecoVO endereco,
            CursoVO curso) {
        this.matricula = matricula;
        this.nome = nome;
        this.nomeMae = nomeMae;
        this.nomePai = nomePai;
        this.sexo = sexo;
        this.endereco = endereco;
        this.curso = curso;
    }

    public AlunoVO(int matricula, String nome, EnumSexo sexo) {
        this.matricula = matricula;
        this.nome = nome;
        this.sexo = sexo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public EnumSexo getSexo() {
        return sexo;
    }

    public void setSexo(EnumSexo sexo) {
        this.sexo = sexo;
    }

    public EnderecoVO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoVO enderecoVO) {
        this.endereco = enderecoVO;
    }

    public CursoVO getCurso() {
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return matricula + " - " + nome;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        AlunoVO alunoVO = (AlunoVO) o;
        return matricula == alunoVO.matricula;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }
}
