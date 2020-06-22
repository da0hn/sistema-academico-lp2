package com.program.persistencia;

import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;
import com.program.vo.DisciplinaVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriel Honda on 21/06/2020
 * @project lp2_academico
 */
public class DisciplinaDAO extends DAO implements IDisciplinaDAO {

    private final PreparedStatement comandoIncluir;
    private final PreparedStatement comandoAlterar;
    private final PreparedStatement comandoExcluir;
    private final PreparedStatement comandoBuscarPorCodigo;
    private final PreparedStatement comandoBuscarPorNome;
    private final PreparedStatement comandoBuscarPorCurso;
    private CursoDAO cursoDAO;

    public DisciplinaDAO(DatabaseConnection conexao) throws PersistenciaException {
        super(conexao);
        this.cursoDAO = new CursoDAO(conexao);
        try {
            this.comandoIncluir = conexao.getConexao().prepareStatement(
                    "INSERT INTO disciplina (nome, semestre, curso) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            this.comandoAlterar = conexao.getConexao().prepareStatement(
                    "UPDATE disciplina SET nome=?, semestre=?, curso=? WHERE codigo=?");
            this.comandoExcluir = conexao.getConexao().prepareStatement(
                    "DELETE FROM disciplina WHERE codigo=?");
            this.comandoBuscarPorCodigo = conexao.getConexao().prepareStatement(
                    "SELECT * FROM disciplina WHERE codigo=?");
            this.comandoBuscarPorNome = conexao.getConexao().prepareStatement(
                    "SELECT * FROM disciplina WHERE UPPER(nome) LIKE ? ORDER BY NOME LIMIT 10");
            this.comandoBuscarPorCurso = conexao.getConexao().prepareStatement("SELECT * FROM " +
                    "disciplina WHERE curso=?");
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao iniciar cada de persistência - " + ex.getMessage());
        }
    }

    @Override
    public int alterar(DisciplinaVO disciplinaVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoAlterar.setString(1, disciplinaVO.getNome());
            comandoAlterar.setInt(2, disciplinaVO.getSemestre());
            comandoAlterar.setInt(3, disciplinaVO.getCurso().getCodigo());
            comandoAlterar.setInt(4, disciplinaVO.getCodigo());
            retorno = comandoAlterar.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao alterar a disciplina - " + ex.getMessage());
        }
        return retorno;
    }

    @Override
    public int excluir(int codigo) throws PersistenciaException {
        int retorno = 0;
        try{
            comandoExcluir.setInt(1, codigo);
            retorno = comandoExcluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao excluir a disciplina - " + ex.getMessage());
        }
        return retorno;
    }

    @Override
    public int incluir(DisciplinaVO disciplinaVO) throws PersistenciaException {
        int retorno;
        try{
            comandoIncluir.setString(1, disciplinaVO.getNome());
            comandoIncluir.setInt(2, disciplinaVO.getSemestre());
            comandoIncluir.setInt(3, disciplinaVO.getCurso().getCodigo());
            retorno = comandoIncluir.executeUpdate();
            if( retorno > 0) {
                var rs = comandoIncluir.getGeneratedKeys();
                if(rs.next()) {
                    disciplinaVO.setCodigo(rs.getInt(1));
                }
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao incluir nova disciplina - " + ex.getMessage());
        }
        return retorno;
    }

    @Override
    public DisciplinaVO buscarPorCodigo(int codigo) throws PersistenciaException {
        DisciplinaVO disciplinaVO = null;
        try {
            var rs = comandoBuscarPorCodigo.executeQuery();
            if(rs.next()) {
                disciplinaVO = buildVO(rs);
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro na busca de disciplina por código - " + ex.getMessage());
        }
        return disciplinaVO;
    }

    private DisciplinaVO buildVO(ResultSet rs) throws SQLException, PersistenciaException {
        var disciplina = new DisciplinaVO();
        disciplina.setCodigo(rs.getInt("codigo"));
        disciplina.setNome(rs.getString("nome"));
        disciplina.setSemestre(rs.getInt("semestre"));

        CursoVO curso = cursoDAO.buscarPorCodigo(rs.getInt("codigo"));
        disciplina.setCurso(curso);

        return disciplina;
    }

    @Override
    public List<DisciplinaVO> buscarPorCurso(CursoVO cursoVO) throws PersistenciaException {
        var disciplinas = new ArrayList<DisciplinaVO>();
        try {
            comandoBuscarPorCurso.setInt(1, cursoVO.getCodigo());
            var rs = comandoBuscarPorCurso.executeQuery();
            while(rs.next()) {
                disciplinas.add(buildVO(rs));
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao buscar por curso - " + ex.getMessage());
        }
        return disciplinas;
    }

    @Override
    public List<DisciplinaVO> buscarPorNome(String nome) throws PersistenciaException {
        var disciplinas = new ArrayList<DisciplinaVO>();
        try {
            comandoBuscarPorNome.setString(1, nome);
            var rs = comandoBuscarPorNome.executeQuery();
            while(rs.next()) {
                disciplinas.add(buildVO(rs));
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao buscar por nome - " + ex.getMessage());
        }
        return disciplinas;
    }
}
