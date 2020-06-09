package com.program.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.AlunoVO;
import com.program.vo.EnumSexo;
import com.program.vo.EnumUF;

public class AlunoDAO extends DAO {

    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoBucarPorMatricula;
    private PreparedStatement comandoBuscarPorNome;
    private CursoDAO cursoDAO;


    public AlunoDAO(DatabaseConnection conexao) throws PersistenciaException {
        super(conexao);
        this.cursoDAO = new CursoDAO(conexao);

        try {
            this.comandoIncluir = conexao.getConnection().prepareStatement("INSERT INTO Aluno ( nome, nomemae, nomepai, sexo, "
                                                                                   + "logradouro, numero, bairro, cidade, uf, curso )VALUES (?, ?, ?, ?, ?, ?, ? ,?, ?, ?)");
            this.comandoAlterar = conexao.getConnection().prepareStatement(
                    "UPDATE Aluno SET nome=?, nomemae=?, nomepai=?, sexo=?,"
                            + "logradouro=?, numero=?, bairro=?, cidade=?, uf=?, curso=? WHERE matricula=?");
            this.comandoExcluir = conexao.getConnection().prepareStatement("DELETE FROM Aluno WHERE matricula=?");
            this.comandoBucarPorMatricula = conexao.getConnection().prepareStatement("SELECT * FROM Aluno WHERE matricula = ?");
            this.comandoBuscarPorNome = conexao.getConnection().prepareStatement("SELECT * FROM aluno WHERE UPPER(nome) LIKE ? ORDER BY NOME LIMIT 10");

        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao iniciar camada de persistência - " + ex.getMessage());
        }
    }

    public int incluir(AlunoVO alunoVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoIncluir.setString(1, alunoVO.getNome());
            comandoIncluir.setString(2, alunoVO.getNomeMae());
            comandoIncluir.setString(3, alunoVO.getNomePai());
            comandoIncluir.setInt(4, alunoVO.getSexo().ordinal());
            comandoIncluir.setString(5, alunoVO.getEndereco().getLogradouro());
            comandoIncluir.setInt(6, alunoVO.getEndereco().getNumero());
            comandoIncluir.setString(7, alunoVO.getEndereco().getBairro());
            comandoIncluir.setString(8, alunoVO.getEndereco().getCidade());
            comandoIncluir.setString(9, alunoVO.getEndereco().getUf().name());
            comandoIncluir.setInt(10, alunoVO.getCurso().getCodigo());
            retorno = comandoIncluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao incluir novo aluno - " + ex.getMessage());
        }
        return retorno;
    }

    public int alterar(AlunoVO alunoVO) throws PersistenciaException {
        int retorno = 0;
        try {

            comandoAlterar.setString(1, alunoVO.getNome());
            comandoAlterar.setString(2, alunoVO.getNomeMae());
            comandoAlterar.setString(3, alunoVO.getNomePai());
            comandoAlterar.setInt(4, alunoVO.getSexo().ordinal());
            comandoAlterar.setString(5, alunoVO.getEndereco().getLogradouro());
            comandoAlterar.setInt(6, alunoVO.getEndereco().getNumero());
            comandoAlterar.setString(7, alunoVO.getEndereco().getBairro());
            comandoAlterar.setString(8, alunoVO.getEndereco().getCidade());
            comandoAlterar.setString(9, alunoVO.getEndereco().getUf().name());
            comandoAlterar.setInt(10, alunoVO.getCurso().getCodigo());
            comandoAlterar.setInt(11, alunoVO.getMatricula());

            retorno = comandoAlterar.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao alterar o aluno - " + ex.getMessage());
        }
        return retorno;
    }

    public int excluir(int matricula) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoExcluir.setInt(1, matricula);

            retorno = comandoExcluir.executeUpdate();
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao excluir o aluno - " + ex.getMessage());
        }
        return retorno;
    }

    public AlunoVO buscarPorMatricula(int matricula) throws PersistenciaException {

        AlunoVO alu = null;

        try {
            comandoBucarPorMatricula.setInt(1, matricula);
            ResultSet rs = comandoBucarPorMatricula.executeQuery();
            if(rs.next()) {
                alu = buildVO(rs);
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro na seleção por matrícula - " + ex.getMessage());
        }
        return alu;
    }

    public List<AlunoVO> buscarPorNome(String nome) throws PersistenciaException {
        List<AlunoVO> listaAluno = new ArrayList<>();
        try {
            comandoBuscarPorNome.setString(1, "%" + nome.trim().toUpperCase() + "%");
            ResultSet rs = comandoBuscarPorNome.executeQuery();
            while(rs.next()) {
                listaAluno.add(buildVO(rs));
            }
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro na seleção por nome - " + ex.getMessage());
        }
        return listaAluno;
    }

    private AlunoVO buildVO(ResultSet rs) throws SQLException, PersistenciaException {
        var aluno = new AlunoVO();
        aluno.setMatricula(rs.getInt("matricula"));
        aluno.setNome(rs.getString("nome").trim());
        aluno.setNomeMae(rs.getString("nomemae"));
        aluno.setNomePai(rs.getString("nomepai"));
        aluno.setSexo(EnumSexo.values()[rs.getInt("sexo")]);
        aluno.getEndereco().setLogradouro(rs.getString("logradouro"));
        aluno.getEndereco().setNumero(rs.getInt("numero"));
        aluno.getEndereco().setBairro(rs.getString("bairro"));
        aluno.getEndereco().setCidade(rs.getString("cidade"));
        aluno.getEndereco().setUf(EnumUF.valueOf(rs.getString("uf")));
        aluno.setCurso(this.cursoDAO.buscarPorCodigo(rs.getInt("curso")));
        return aluno;
    }
}
