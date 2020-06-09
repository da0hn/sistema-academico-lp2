package com.program.view.base;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MensagemUtil {

    private static final Alert INFORMATION_MESSAGE;
    private static final Alert CONFIRMATION_MESSAGE;
    private static final Alert ERROR_MESSAGE;
    private static final Alert WARNING_MESSAGE;
    public static final boolean CONFIRMOU = true;
    public static final boolean CANCELOU = false;
    
    static {
        INFORMATION_MESSAGE = new Alert(Alert.AlertType.INFORMATION);
        INFORMATION_MESSAGE.setTitle("Menssagem de informação");
        INFORMATION_MESSAGE.setResizable(true);
        ERROR_MESSAGE = new Alert(Alert.AlertType.ERROR);
        ERROR_MESSAGE.setTitle("Menssagem de Erro");
        ERROR_MESSAGE.setResizable(true);
        WARNING_MESSAGE = new Alert(Alert.AlertType.WARNING);
        WARNING_MESSAGE.setTitle("Menssagem de Alerta");
        WARNING_MESSAGE.setResizable(true);
        CONFIRMATION_MESSAGE = new Alert(Alert.AlertType.CONFIRMATION);
        CONFIRMATION_MESSAGE.setTitle("Menssagem de Confirmação");
        CONFIRMATION_MESSAGE.setResizable(true);
    }

    public static void mensagemInformacao(String detalheMensagem) {
        INFORMATION_MESSAGE.setHeaderText("Informação");
        INFORMATION_MESSAGE.setContentText(detalheMensagem);
        INFORMATION_MESSAGE.showAndWait();
    }

    public static void mensagemErro(String detalheMensagem) {
        ERROR_MESSAGE.setHeaderText("Erro");
        ERROR_MESSAGE.setContentText(detalheMensagem);
        ERROR_MESSAGE.showAndWait();
    }

    public static void mensagemAlerta(String detalheMensagem) {
        WARNING_MESSAGE.setHeaderText("Atenção situação de alerta");
        WARNING_MESSAGE.setContentText(detalheMensagem);
        WARNING_MESSAGE.showAndWait();
    }

    public static boolean mensagemConfirmacao(String detalheMensagem) {
        CONFIRMATION_MESSAGE.setHeaderText("Responda a questão");
        CONFIRMATION_MESSAGE.setContentText(detalheMensagem);
        Optional<ButtonType> result = CONFIRMATION_MESSAGE.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            return MensagemUtil.CONFIRMOU;
        } else {
            return MensagemUtil.CANCELOU;
        }
    }

}
