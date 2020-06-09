package com.program.negocio.base;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class NegocioException extends Exception {
    public NegocioException() {
        super(" Erro ocorrido na camada de negócio!");
    }

    public NegocioException(String msg) {
        super(msg);
    }
}
