/*
 * @project lp2_academico
 * @author Gabriel Honda on 09/06/2020
 */
module academico {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.program.vo;
    opens com.program.view.aluno;
    opens com.program.view.base;
    opens com.program.view.curso;
    opens com.program.view;
}