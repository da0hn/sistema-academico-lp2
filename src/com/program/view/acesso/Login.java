package com.program.view.acesso;

import com.program.negocio.Usuario;
import com.program.negocio.base.NegocioException;
import com.program.view.base.MensagemUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Gabriel Honda on 18/06/2020
 * @project lp2_academico
 */
public class Login {

    private static boolean possuiUsuario;

    public static boolean verificiarExistenciaUsuario() {
        try {
            var usuarioNegocio = new Usuario();
            String resp = usuarioNegocio.verificaPrimeiroAcesso();
            if(!resp.isEmpty()) {
                MensagemUtil.mensagemInformacao(resp);
            }
            Login.possuiUsuario = true;
        }
        catch(NegocioException e) {
            Login.possuiUsuario = false;
            MensagemUtil.mensagemAlerta("Erro no primeiro acesso - " + e.getMessage());
        }
        return Login.possuiUsuario;
    }


    public static void executarLogin() {
        if(Login.possuiUsuario) {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(Login.class.getResource("LoginUsuarioFXML.fxml"));
                parent = (Parent) fxmlLoader.load();
                LoginUsuarioController controller =
                        (LoginUsuarioController) fxmlLoader.getController();
                controller.setStage(stage);
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            }
            catch(IOException e) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de login de usuarios " + e.getMessage());
            }
        }
        else {
            MensagemUtil.mensagemErro("Erro de inicialização do primeiro usuário");
        }
    }
}
