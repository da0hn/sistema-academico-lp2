package com.program.persistencia;


import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.EnumPerfilUsuario;
import com.program.vo.UsuarioVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO extends DAO {

    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoAlterarSenha;
    private PreparedStatement comandoBucarUsuarioPorCodigo;
    private PreparedStatement comandoBucarUsuarioPorLogin;
    private PreparedStatement comandoBuscarUsuarioPorNome;

    public UsuarioDAO(DatabaseConnection conexao) throws PersistenciaException {
        super(conexao);

        try {
            this.comandoIncluir = conexao.getConexao().prepareStatement(
                    "INSERT INTO usuario (matricula, login, senha, nome, perfil, email, telefone " +
                            ") VALUES (?,?,?,?,?,?,?)");
            this.comandoAlterar = conexao.getConexao().prepareStatement(
                    "UPDATE usuario SET matricula=?, login=?, nome=?, perfil=?, email=?, " +
                            "telefone=? WHERE codigo=?");
            this.comandoAlterarSenha = conexao.getConexao().prepareStatement(
                    "UPDATE usuario SET senha=? WHERE upper(login)=?");
            this.comandoExcluir = conexao.getConexao().prepareStatement(
                    "DELETE FROM usuario WHERE codigo=?");
            this.comandoBucarUsuarioPorCodigo = conexao.getConexao().prepareStatement(
                    "SELECT * FROM usuario WHERE codigo = ?");
            this.comandoBucarUsuarioPorLogin = conexao.getConexao().prepareStatement(
                    "SELECT * FROM usuario WHERE upper(login) = ?");
            this.comandoBuscarUsuarioPorNome = conexao.getConexao().prepareStatement(
                    "SELECT * FROM usuario WHERE UPPER(nome) LIKE ? ORDER BY NOME LIMIT 15");
        }
        catch(SQLException ex) {
            throw new PersistenciaException(
                    "Erro ao iniciar camada de persistência - " + ex.getMessage());
        }
    }

    public int incluir(UsuarioVO usuarioVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoIncluir.setString(1, usuarioVO.getMatricula());
            comandoIncluir.setString(2, usuarioVO.getLogin());
            comandoIncluir.setString(3, usuarioVO.getSenha());
            comandoIncluir.setString(4, usuarioVO.getNome());
            comandoIncluir.setString(5, usuarioVO.getPerfil().name());
            comandoIncluir.setString(6, usuarioVO.getEmail());
            comandoIncluir.setString(7, usuarioVO.getTelefone());
            retorno = comandoIncluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("DAO - Erro ao incluir usuario - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterar(UsuarioVO usuarioVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoAlterar.setString(1, usuarioVO.getMatricula());
            comandoAlterar.setString(2, usuarioVO.getLogin());
            comandoAlterar.setString(3, usuarioVO.getNome());
            comandoAlterar.setString(4, usuarioVO.getPerfil().name());
            comandoAlterar.setString(5, usuarioVO.getEmail());
            comandoAlterar.setString(6, usuarioVO.getTelefone());
            comandoAlterar.setInt(7, usuarioVO.getCodigo());
            retorno = comandoAlterar.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("DAO - Erro ao alterar o usuario - " + ex.getMessage());
        }
        return retorno;
    }

    public int excluir(int codigo) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoExcluir.setInt(1, codigo);
            retorno = comandoExcluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("DAO - Erro ao excluir o usuario - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterarSenha(String login, String novaSenha) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoAlterarSenha.setString(1, novaSenha);
            comandoAlterarSenha.setString(2, login.toUpperCase());
            retorno = comandoAlterarSenha.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException(
                    "DAO - Erro ao alterar a senha do usuario - " + ex.getMessage());
        }
        return retorno;
    }

    public UsuarioVO buscarPorCodigo(int codigo) {

        UsuarioVO usuarioVO = null;

        try {
            comandoBucarUsuarioPorCodigo.setInt(1, codigo);
            ResultSet rs = comandoBucarUsuarioPorCodigo.executeQuery();
            if(rs.next()) {
                usuarioVO = this.montarVO(rs);
            }
        }
        catch(SQLException ex) {
            System.err.println(ex);
        }
        return usuarioVO;
    }

    public List<UsuarioVO> buscarPorNome(String nome) {
        List<UsuarioVO> listaCurso = new ArrayList();

        try {
            comandoBuscarUsuarioPorNome.setString(1, "%" + nome.trim().toUpperCase() + "%");
            ResultSet rs = comandoBuscarUsuarioPorNome.executeQuery();
            while(rs.next()) {
                listaCurso.add(this.montarVO(rs));
            }
        }
        catch(SQLException ex) {
            System.err.println(ex);
        }
        return listaCurso;
    }

    public UsuarioVO buscarPorLogin(String login) {

        UsuarioVO usuarioVO = null;

        try {
            comandoBucarUsuarioPorLogin.setString(1, login.toUpperCase());
            ResultSet rs = comandoBucarUsuarioPorLogin.executeQuery();
            if(rs.next()) {
                usuarioVO = this.montarVO(rs);
            }
        }
        catch(SQLException ex) {
            System.err.println(ex);
        }
        return usuarioVO;
    }

    private UsuarioVO montarVO(ResultSet rs) throws SQLException {
        UsuarioVO usuarioVO = new UsuarioVO();

        usuarioVO.setCodigo(rs.getInt("codigo"));
        usuarioVO.setMatricula(rs.getString("matricula").trim());
        usuarioVO.setLogin(rs.getString("login").trim());
        usuarioVO.setSenha(rs.getString("senha").trim());
        usuarioVO.setNome(rs.getString("nome").trim());
        usuarioVO.setPerfil(EnumPerfilUsuario.valueOf(rs.getString("perfil").trim()));
        usuarioVO.setEmail(rs.getString("email"));
        usuarioVO.setTelefone(rs.getString("telefone"));

        return usuarioVO;
    }
}
