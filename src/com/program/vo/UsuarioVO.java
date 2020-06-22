package com.program.vo;

import java.util.Objects;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 10/06/2020
 */
public class UsuarioVO {
    private int codigo;
    private String login;
    private String senha;
    private String matricula;
    private String nome;
    private EnumPerfilUsuario perfil;
    private String email;
    private String telefone;

    public UsuarioVO() {
        this.codigo = 0;
        this.login = " ";
        this.senha = " ";
        this.matricula = " ";
        this.nome = " ";
        this.perfil = null;
        this.email = " ";
        this.telefone = " ";
    }

    public UsuarioVO(int codigo, String matricula, String nome) {
        this.codigo = codigo;
        this.matricula = matricula;
        this.nome = nome;
    }

    public UsuarioVO(int codigo, String login, String senha, String matricula, String nome,
            EnumPerfilUsuario perfil, String email, String telefone) {
        this.codigo = codigo;
        this.login = login;
        this.senha = senha;
        this.matricula = matricula;
        this.nome = nome;
        this.perfil = perfil;
        this.email = email;
        this.telefone = telefone;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EnumPerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(EnumPerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        UsuarioVO usuarioVO = (UsuarioVO) o;
        return codigo == usuarioVO.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
