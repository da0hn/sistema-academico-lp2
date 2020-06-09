package com.program.persistencia.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class DatabaseConnection {
    private Connection connection;
    private static DatabaseConnection INSTANCE;

    private DatabaseConnection() throws PersistenciaException {
        try {
            Class.forName("org.postgresql.Driver");
            final String url = "jdbc:postgresql://localhost:5432/academico";
            connection = DriverManager.getConnection(url, "postgres", "admin");
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao conectar o banco de dados - " + ex.toString());
        }
        catch(ClassNotFoundException ex) {
            throw new PersistenciaException("Driver do banco de dados não localizado - " + ex.toString());
        }
    }

    public static DatabaseConnection getInstance() throws PersistenciaException {
        if( INSTANCE == null) {
            INSTANCE = new DatabaseConnection();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void desconnect() throws PersistenciaException {
        try {
            connection.close();
        } catch(SQLException ex) {
            throw new PersistenciaException("Erro ao desconectar o banco de dados - " + ex.toString());
        }
    }
}
