package com.program.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPrincipal extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        Stage stageTemp = new Stage(StageStyle.UNIFIED);
        Stage stageTemp = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("MenuPrincipalFXML.fxml"));
        Scene scene = new Scene(parent);
        stageTemp.setScene(scene);
        stageTemp.setTitle("Menu Principal");
        stageTemp.setResizable(false);
        stageTemp.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
