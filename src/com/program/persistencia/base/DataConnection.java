package com.program.persistencia.base;

import java.util.Properties;

/**
 * @author Gabriel Honda on 23/06/2020
 * @project lp2_academico
 */
public class DataConnection {
    private final String senha;
    private final String login;
    private final String url;
    private final String driver;

    public DataConnection(String login, String senha, String url, String driver) {
        this.senha = senha;
        this.login = login;
        this.url = url;
        this.driver = driver;
    }
    public String getSenha() {
        return senha;
    }
    public String getLogin() {
        return login;
    }
    public String getUrl() {
        return url;
    }
    public String getDriver() {
        return driver;
    }
    public Properties toProperties() {
        var prop = new Properties();
        prop.setProperty("LoginBD", login);
        prop.setProperty("SenhaBD", senha);
        prop.setProperty("UrlBD", url);
        prop.setProperty("DriverBD", driver);
        return prop;
    }
}
