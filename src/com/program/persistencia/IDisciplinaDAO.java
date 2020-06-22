package com.program.persistencia;

import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;
import com.program.vo.DisciplinaVO;

import java.util.List;

/**
 * @author Gabriel Honda on 21/06/2020
 * @project lp2_academico
 */
public interface IDisciplinaDAO {
    int alterar(DisciplinaVO disciplinaVO) throws PersistenciaException;
    int excluir(int codigo) throws PersistenciaException;
    int incluir(DisciplinaVO disciplinaVO) throws PersistenciaException;
    DisciplinaVO buscarPorCodigo(int codigo) throws PersistenciaException;
    List<DisciplinaVO> buscarPorCurso(CursoVO cursoVO) throws PersistenciaException;
    List<DisciplinaVO> buscarPorNome(String nome) throws PersistenciaException;
}
