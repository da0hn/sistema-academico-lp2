package com.program.persistencia;

import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;

import java.sql.SQLException;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class DAO {
    protected DatabaseConnection connection;

    public DAO(DatabaseConnection connection) throws PersistenciaException {
        this.connection = connection;
        try {
            this.connection.getConexao().setAutoCommit(false);
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro ao configurar a conexão!");
        }
    }

    public void confirmarTransacao() throws PersistenciaException {
        try {
            this.connection.getConexao().commit();
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Transação não confirmada");
        }
    }

    public void cancelarTransacao() throws PersistenciaException {
        try {
            this.connection.getConexao().rollback();
        }
        catch(SQLException ex) {
            throw new PersistenciaException(" Erro ao cancelar a transação!");
        }
    }

    public DatabaseConnection getConnection() {
        return this.connection;
    }

    public void setConnection(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void desconnectDatabase() throws PersistenciaException {
        connection.desconnect();
    }
}
