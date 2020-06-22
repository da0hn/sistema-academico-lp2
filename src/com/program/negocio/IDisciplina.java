package com.program.negocio;

import com.program.negocio.base.NegocioException;
import com.program.vo.CursoVO;
import com.program.vo.DisciplinaVO;

import java.util.List;

/**
 * @author Gabriel Honda on 22/06/2020
 * @project lp2_academico
 */
public interface IDisciplina {
    void alterar(DisciplinaVO disciplinaVO) throws NegocioException;
    void excluir(int codigo) throws NegocioException;
    void inserir(DisciplinaVO disciplinaVO) throws NegocioException;
    DisciplinaVO pesquisaPorCodigo(int codigo) throws NegocioException;
    List<DisciplinaVO> pesquisaPorCurso(CursoVO cursoVO) throws NegocioException;
    List<DisciplinaVO> pesquisaPorNome(String parteNome) throws NegocioException;
    String validarDados(DisciplinaVO disciplinaVO);
}
