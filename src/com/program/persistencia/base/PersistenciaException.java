package com.program.persistencia.base;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class PersistenciaException extends  Exception {
    public PersistenciaException() {
        super("Erro ocorrido na manipulação do banco de dados");
    }

    public PersistenciaException(String msg) {
        super(msg);
    }
}
