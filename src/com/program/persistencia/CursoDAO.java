package com.program.persistencia;

import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class CursoDAO extends DAO {

    private final PreparedStatement comandoIncluir;
    private final PreparedStatement comandoAlterar;
    private final PreparedStatement comandoExcluir;
    private final PreparedStatement comandoBuscarPorCodigo;
    private final PreparedStatement comandoBuscarPorNome;

    public CursoDAO(DatabaseConnection connection) throws PersistenciaException {
        super(connection);
        try {
            this.comandoIncluir = connection.getConexao()
                    .prepareStatement("INSERT INTO curso ( nome ) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            this.comandoAlterar = connection.getConexao()
                    .prepareStatement("UPDATE curso SET nome=? WHERE codigo=?");
            this.comandoExcluir = connection.getConexao()
                    .prepareStatement("DELETE FROM curso WHERE codigo=?");
            this.comandoBuscarPorCodigo = connection.getConexao()
                    .prepareStatement("SELECT * FROM curso WHERE codigo=?");
            this.comandoBuscarPorNome = connection.getConexao()
                    .prepareStatement(
                            "SELECT * FROM curso WHERE UPPER(nome) LIKE ? ORDER BY NOME LIMIT 10");
        }
        catch(SQLException ex) {
            throw new PersistenciaException(
                    " Erro ao iniciar a camada de persistencia - " + ex.toString());
        }
    }

    public int incluir(CursoVO cursoVO) throws PersistenciaException {
        int retorno;
        try {
            comandoIncluir.setString(1, cursoVO.getNome());
            retorno = comandoIncluir.executeUpdate();
            if( retorno > 0) {
                var rs = comandoIncluir.getGeneratedKeys();
                if(rs.next()) {
                    cursoVO.setCodigo(rs.getInt(1));
                }
//                connection.desconnect();
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro ao alterar o curso - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterar(CursoVO cursoVO) throws PersistenciaException {
        int retorno;
        try {
            comandoAlterar.setString(1, cursoVO.getNome());
            comandoAlterar.setInt(2, cursoVO.getCodigo());
            retorno = comandoAlterar.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro ao alterar o curso - " + ex.getMessage());
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
            throw new PersistenciaException(" Erro ao excluir curso - " + ex.getMessage());
        }
        return retorno;
    }

    public CursoVO buscarPorCodigo(int codigo) throws PersistenciaException {
        CursoVO cursoVO = null;
        try {
            comandoBuscarPorCodigo.setInt(1, codigo);
            var rs = comandoBuscarPorCodigo.executeQuery();
            if(rs.next()) {
                cursoVO = buildVO(rs);
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro na selecao por codigo - " + ex.getMessage());
        }
        return cursoVO;
    }

    public List<CursoVO> buscarPorNome(String nome) throws PersistenciaException {
        var listaCurso = new ArrayList<CursoVO>();
        try {
            comandoBuscarPorNome.setString(1, '%' + nome.trim().toUpperCase() + '%');
            var rs = comandoBuscarPorNome.executeQuery();
            while(rs.next()) {
                listaCurso.add(buildVO(rs));
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro na selecao por nome - " + ex.getMessage());
        }
        return listaCurso;
    }

    private CursoVO buildVO(ResultSet rs) throws SQLException {
        var cursoVO = new CursoVO();
        cursoVO.setCodigo(rs.getInt("codigo"));
        cursoVO.setNome(rs.getString("nome").trim());
        return cursoVO;
    }
}
