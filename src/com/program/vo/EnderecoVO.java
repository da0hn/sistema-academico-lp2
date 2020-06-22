package com.program.vo;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class EnderecoVO {
    private String logradouro;
    private int numero;
    private String bairro;
    private String cidade;
    private EnumUF enumUf;

    public EnderecoVO() {
        this.logradouro = " ";
        this.numero = 0;
        this.bairro = " ";
        this.cidade = " ";
        this.enumUf = EnumUF.MT;
    }

    public EnderecoVO(String logradouro, int numero, String bairro, String cidade, EnumUF enumUf) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.enumUf = enumUf;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public EnumUF getEnumUf() {
        return enumUf;
    }

    public void setEnumUf(EnumUF enumUf) {
        this.enumUf = enumUf;
    }

    @Override
    public String toString() {
        return "EnderecoVO{" +
                "logradouro='" + logradouro + '\'' +
                ", numero=" + numero +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", uf=" + enumUf +
                '}';
    }
}
