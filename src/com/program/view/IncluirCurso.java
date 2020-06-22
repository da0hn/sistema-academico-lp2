package com.program.view;

import com.program.view.base.MensagemUtil;
import com.program.view.curso.IncluirCursoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class IncluirCurso extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Stage stageTemp = new Stage(StageStyle.DECORATED);
        Parent parent = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("curso/IncluirCursoFXML.fxml"));
            parent = (Parent) fxmlLoader.load();
        }
        catch(IOException ex) {
            MensagemUtil.mensagemErro(
                    "Erro ao iniciar a tela de inclusão de Curso \n\n" + ex.getMessage());
        }

        IncluirCursoController controller = (IncluirCursoController) fxmlLoader.getController();
        controller.setPalcoOrigem(stage);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Incluir Curso");
        stage.setResizable(false);
        stage.show();
    }
}



        