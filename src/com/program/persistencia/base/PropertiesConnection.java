package com.program.persistencia.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Gabriel Honda on 23/06/2020
 * @project lp2_academico
 */
public class PropertiesConnection {

    public static void armazenarDados(DataConnection data) throws PersistenciaException {
        try(var output = new FileOutputStream("configuracaoBD.properties")) {
            var prop = data.toProperties();
            prop.store(output, null);
        }
        catch(FileNotFoundException e) {
            throw new PersistenciaException("Não foi encontrado o arquivo de configuração");
        }
        catch(IOException e) {
            throw new PersistenciaException("Não foi possível armazenar os dados de configuração");
        }
    }

    public static Properties carregarPropriedades() throws PersistenciaException {
        Properties propriedadesDeConexao = new Properties();
        try( var fs = new FileInputStream("configuracaoBD.properties")) {
            propriedadesDeConexao.load(fs);
        }
        catch(IOException e) {
            throw new PersistenciaException("Erro ao carregar as propriedades");
        }
        return propriedadesDeConexao;
    }

}
