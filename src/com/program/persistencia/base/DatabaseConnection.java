package com.program.persistencia.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
public class DatabaseConnection {
    private static DatabaseConnection INSTANCE;
    private Connection connection;

    private DatabaseConnection() throws PersistenciaException {
        try {
            Properties propriedades = carregarPropriedades();
            Class.forName(propriedades.getProperty("DriverBD"));
            var url = propriedades.getProperty("UrlBD");
            var user = propriedades.getProperty("UsuarioBD");
            var password = propriedades.getProperty("SenhaBD");
            connection = DriverManager.getConnection(url, user, password);
        }
        catch(SQLException ex) {
            throw new PersistenciaException("Erro ao conectar o banco de dados - " + ex.toString());
        }
        catch(ClassNotFoundException ex) {
            throw new PersistenciaException(
                    "Driver do banco de dados não localizado - " + ex.toString());
        }
    }

    public static DatabaseConnection getInstance() throws PersistenciaException {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseConnection();
        }
        return INSTANCE;
    }

    private static Properties carregarPropriedades() throws PersistenciaException {
        Properties propriedadesDeConexao = new Properties();
        try( var fs = new FileInputStream("configuracaoBD.properties")) {
            propriedadesDeConexao.load(fs);
        }
        catch(IOException e) {
            throw new PersistenciaException("Erro ao carregar as propriedades");
        }
        return propriedadesDeConexao;
    }

    public Connection getConexao() {
        return this.connection;
    }

    public void desconnect() throws PersistenciaException {
        try {
            connection.close();
        }
        catch(SQLException ex) {
            throw new PersistenciaException(
                    "Erro ao desconectar o banco de dados - " + ex.toString());
        }
    }
}
