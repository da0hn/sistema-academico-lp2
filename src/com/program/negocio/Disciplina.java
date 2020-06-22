package com.program.negocio;

import com.program.negocio.base.NegocioException;
import com.program.persistencia.DisciplinaDAO;
import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;
import com.program.vo.DisciplinaVO;

import java.util.List;

/**
 * @author Gabriel Honda on 22/06/2020
 * @project lp2_academico
 *
 * */
public class Disciplina implements IDisciplina {

    private DisciplinaDAO disciplinaDAO;

    public Disciplina() throws NegocioException {
        try {
            this.disciplinaDAO = new DisciplinaDAO(DatabaseConnection.getInstance());
        }
        catch(PersistenciaException e) {
            throw new NegocioException("Erro ao iniciar a Persistencia - " + e.getMessage());
        }
    }

    @Override
    public void alterar(DisciplinaVO disciplinaVO) throws NegocioException {
        String mensagemErros = this.validarDados(disciplinaVO);
        if(!mensagemErros.isEmpty()) throw new NegocioException(mensagemErros);
        try {
            if(disciplinaDAO.alterar(disciplinaVO) == 0) {
                throw new NegocioException("Alteração " +
                        "não realizada");
            }
            disciplinaDAO.confirmarTransacao();
        }
        catch(PersistenciaException e) {
            throw new NegocioException("Erro ao alterar a disciplina - " + e.getMessage());
        }
    }

    @Override
    public void excluir(int codigo) throws NegocioException {
        try {
            if(disciplinaDAO.excluir(codigo) == 0) {
                throw new NegocioException("Exclusão não " +
                        "realizada");
            }
            disciplinaDAO.confirmarTransacao();
        }
        catch(PersistenciaException e) {
            throw new NegocioException("Erro ao excluir o aluno - " + e.getMessage());
        }
    }

    @Override
    public void inserir(DisciplinaVO disciplinaVO) throws NegocioException {
        String mensagemErros = this.validarDados(disciplinaVO);
        if(!mensagemErros.isEmpty()) throw new NegocioException(mensagemErros);
        try {
            if(disciplinaDAO.incluir(disciplinaVO) == 0) {
                throw new NegocioException("Inclusão não realizada!");
            }
            disciplinaDAO.confirmarTransacao();
        }
        catch(PersistenciaException e) {
            throw new NegocioException("Erro ao incluir o aluno - " + e.getMessage());
        }
    }

    @Override
    public DisciplinaVO pesquisaPorCodigo(int codigo) throws NegocioException {
        try {
            return disciplinaDAO.buscarPorCodigo(codigo);
        }
        catch(PersistenciaException ex) {
            throw new NegocioException(
                    "Erro ao pesquisar disciplina por codigo - " + ex.getMessage());
        }
    }

    @Override
    public List<DisciplinaVO> pesquisaPorCurso(CursoVO cursoVO) throws NegocioException {
        try {
            return disciplinaDAO.buscarPorCurso(cursoVO);
        }
        catch(PersistenciaException ex) {
            throw new NegocioException(
                    "Erro ao pesquisar por curso - " + ex.getMessage());
        }
    }

    @Override
    public List<DisciplinaVO> pesquisaPorNome(String parteNome) throws NegocioException {
        try {
            return disciplinaDAO.buscarPorNome(parteNome);
        }
        catch(PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar aluno pelo nome - " + ex.getMessage());
        }
    }

    @Override
    public String validarDados(DisciplinaVO disciplinaVO) {
        String mensagemErros = "";

        if(disciplinaVO.getNome() == null || disciplinaVO.getNome().length() == 0) {
            mensagemErros += "\nNome da disciplina não pode estar vazia";
        }
        if(disciplinaVO.getSemestre() == null || disciplinaVO.getSemestre() == 0) {
            mensagemErros += "\nSemestre não pode estar vazio ou ser igual a zero";
        }
        if(disciplinaVO.getCurso() == null) {
            mensagemErros += "\nA disciplina precisa estar relacionada a um curso";
        }
        return mensagemErros;
    }
}
