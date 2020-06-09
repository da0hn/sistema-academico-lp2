package com.program.negocio;

import com.program.negocio.base.NegocioException;
import com.program.persistencia.CursoDAO;
import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;

import java.util.List;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class Curso {
    private CursoDAO cursoDAO;

    public Curso() throws NegocioException {
        try {
            this.cursoDAO = new CursoDAO(DatabaseConnection.getInstance());
        }
        catch(PersistenciaException ex) {
            throw new NegocioException(" Erro ao iniciar a persistencia - " + ex.getMessage());
        }
    }

    public void inserir(CursoVO curso) throws NegocioException {
        String mensagemErros = this.validarDados(curso);
        if(!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }
        try {
            if(cursoDAO.incluir(curso) == 0) {
                throw new NegocioException(" Inclusão não realizada!");
            }
            else {
                cursoDAO.confirmarTransacao();
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao incluir o curso - " + e.getMessage());
        }
    }

    public void alterar(CursoVO curso) throws NegocioException {
        String mensagemErros = this.validarDados(curso);
        if(!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }
        try {
            if(cursoDAO.alterar(curso) == 0) {
                throw new NegocioException(" Alteração não realizada");
            }
            else {
                cursoDAO.confirmarTransacao();
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao alterar o curso - " + e.getMessage());
        }
    }

    public void excluir(int codigo) throws NegocioException {
        try {
            if(cursoDAO.excluir(codigo) == 0) {
                throw new NegocioException(" Alteracao não realizada!");
            }
            else {
                cursoDAO.confirmarTransacao();
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao excluir o curso - " + e.getMessage());
        }
    }

    public List<CursoVO> pesquisaParteNome(String parteNome) throws NegocioException {
        try {
            return cursoDAO.buscarPorNome(parteNome);
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao pesquisar curso pelo nome - " + e.getMessage());
        }
    }

    public CursoVO pesquisaMatricula(int codigo) throws NegocioException {
        try {
            return cursoDAO.buscarPorCodigo(codigo);
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao pesquisar por codigo - " + e.getMessage());
        }
    }

    public String validarDados(CursoVO curso) {
        String mensagemErros = "";
        if(curso.getNome() == null || curso.getNome().isBlank() || curso.getNome().isEmpty()) {
            mensagemErros += "Nome do curso não pode ser vazio";
        }
        return mensagemErros;
    }

}
