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

/*
 * @project lp2_academico
 * @author Gabriel Honda on 10/06/2020
 */
public class UsuarioDAO extends DAO {

    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoAlterarSenha;
    private PreparedStatement comandoBuscarUsuarioPorCodigo;
    private PreparedStatement comandoBuscarUsuarioPorLogin;
    private PreparedStatement comandoBuscarUsuarioPorNome;


    public UsuarioDAO(DatabaseConnection connection) throws PersistenciaException {
        super(connection);
        try {
            this.comandoIncluir = connection.getConnection()
                    .prepareStatement("INSERT INTO usuario (matricula, login, senha, nome, " +
                            "perfil, email, telefone) VALUES (?,?,?,?,?,?,?)");
            this.comandoAlterar = connection.getConnection()
                    .prepareStatement("UPDATE usuario SET matricula=?, login=?, nome=?, perfil=?," +
                            " email=?, telefone=? WHERE codigo=?");
            this.comandoAlterarSenha = connection.getConnection()
                    .prepareStatement("UPDATE usuario SET senha=? WHERE upper(login)=?");
            this.comandoExcluir = connection.getConnection()
                    .prepareStatement("DELETE FROM usuario WHERE codigo=?");
            this.comandoBuscarUsuarioPorCodigo = connection.getConnection()
                    .prepareStatement("SELECT * FROM usuario WHERE codigo=?");
            this.comandoBuscarUsuarioPorLogin = connection.getConnection()
                    .prepareStatement("SELECT * FROM usuario WHERE upper(login)=?");
            this.comandoBuscarUsuarioPorNome = connection.getConnection()
                    .prepareStatement("SELECT * FROM usuario WHERE upper(nome) LIKE ? ORDER BY " +
                            "NOME LIMIT 15");
        }
        catch(SQLException ex) {
            throw new PersistenciaException(
                    " Erro ao iniciar a camada de persistencia - " + ex.getMessage());
        }
    }

    public int incluir(UsuarioVO usuario) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoIncluir.setString(1, usuario.getMatricula());
            comandoIncluir.setString(2, usuario.getLogin());
            comandoIncluir.setString(3, usuario.getSenha());
            comandoIncluir.setString(4, usuario.getNome());
            comandoIncluir.setString(5, usuario.getPerfil().name());
            comandoIncluir.setString(6, usuario.getEmail());
            comandoIncluir.setString(7, usuario.getTelefone());
            retorno = comandoIncluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("DAO - Erro ao incluir usuario - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterar(UsuarioVO usuario) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoAlterar.setString(1, usuario.getMatricula());
            comandoAlterar.setString(2, usuario.getLogin());
            comandoAlterar.setString(3, usuario.getSenha());
            comandoAlterar.setString(4, usuario.getNome());
            comandoAlterar.setString(5, usuario.getPerfil().name());
            comandoAlterar.setString(6, usuario.getEmail());
            comandoAlterar.setString(7, usuario.getTelefone());
            retorno = comandoAlterar.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("DAO - Erro ao alterar o usuário - " + ex.getMessage());
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
            throw new PersistenciaException("DAO - Erro ao excluir o usuário - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterarSenha(String login, String novaSenha) {
        int retorno = 0;
        try {
            comandoAlterarSenha.setString(1, novaSenha);
            comandoAlterarSenha.setString(2, login.toUpperCase());
            retorno = comandoAlterarSenha.executeUpdate();
        }
        catch(SQLException ex) {
            System.err.println(
                    "DAO - Erro ao alterar a senha do usuário - " + ex.getMessage());
        }
        return retorno;
    }

    public UsuarioVO buscarPorCodigo(int codigo) {
        UsuarioVO usuario = null;
        try {
            comandoBuscarUsuarioPorCodigo.setInt(1, codigo);
            var rs = comandoBuscarUsuarioPorCodigo.executeQuery();
            if(rs.next()) {
                usuario = this.buildVO(rs);
            }
        }
        catch(SQLException ex) {
            System.err.println(
                    "DAO - Erro ao buscar usuário por código - " + ex.getMessage());
        }
        return usuario;
    }

    public List<UsuarioVO> buscarPorNome(String nome) {
        var listaUsuario = new ArrayList<UsuarioVO>();
        try {
            comandoBuscarUsuarioPorNome.setString(1,
                    "%" + nome.trim().toUpperCase() + "%"
            );
            var rs = comandoBuscarUsuarioPorNome.executeQuery();
            while(rs.next()) {
                listaUsuario.add(buildVO(rs));
            }
        }
        catch(SQLException ex) {
            System.err.println(
                    "DAO - Erro ao buscar usuarios por nome - " + ex.getMessage());
        }
        return listaUsuario;
    }

    public UsuarioVO buscarPorLogin(String login) {
        UsuarioVO usuario = null;
        try {
            comandoBuscarUsuarioPorLogin.setString(1, login.toUpperCase());
            var rs = comandoBuscarUsuarioPorLogin.executeQuery();
            if(rs.next()) {
                usuario = buildVO(rs);
            }
        }
        catch(SQLException ex) {
            System.err.println("DAO - Erro ao buscar usuario por login - " + ex.getMessage());
        }
        return usuario;
    }

    private UsuarioVO buildVO(ResultSet rs) throws SQLException {
        var usuario = new UsuarioVO();
        usuario.setCodigo(rs.getInt("codigo"));
        usuario.setMatricula(rs.getString("matricula").trim());
        usuario.setLogin(rs.getString("login").trim());
        usuario.setSenha(rs.getString("senha").trim());
        usuario.setNome(rs.getString("nome").trim());
        usuario.setPerfil(EnumPerfilUsuario.valueOf(rs.getString("perfil").trim()));
        usuario.setTelefone(rs.getString("telefone").trim());
        return usuario;
    }
}
