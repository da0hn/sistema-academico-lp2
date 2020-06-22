package com.program.view.acesso;


import com.program.acesso.AcessoUtil;
import com.program.negocio.Usuario;
import com.program.negocio.base.NegocioException;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.vo.UsuarioVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Gabriel Honda on 18/06/2020
 * @project lp2_academico
 */
public class LoginUsuarioController extends BaseController implements Initializable {

    private Usuario usuarioNegocio;

    @FXML
    private TextField campoLogin;

    @FXML
    private TextField campoSenha;

    public LoginUsuarioController() throws Exception {
        try {
            this.usuarioNegocio = new Usuario();
        }
        catch(NegocioException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private void executarLogin() {

        String login = this.campoLogin.getText().trim();
        String senha = this.campoSenha.getText().trim();

        if(usuarioNegocio != null) {
            UsuarioVO usuarioVO = usuarioNegocio.login(login, senha);
            if(usuarioVO != null) {
                //se é o primeiro acesso ou senha padrao inicial de usuário
                if(usuarioVO.getSenha().equals(usuarioVO.getLogin())) {
                    this.trocarSenhaInicial();
                }
                else {
                    AcessoUtil.setUsuarioLogado(usuarioVO);
                }
                this.sair();
            }
            else {
                AcessoUtil.setUsuarioLogado(null);
                MensagemUtil.mensagemErro("Login ou Senha Inválido");
            }
        }
        else {
            MensagemUtil.mensagemErro("Camada de negócio/persistência não iniciada");
        }

    }

    private void trocarSenhaInicial() {
        Stage stage = new Stage(StageStyle.UNDECORATED);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("AlterarSenhaUsuarioFXML.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            AlterarSenhaUsuarioController controller =
                    (AlterarSenhaUsuarioController) fxmlLoader.getController();
            controller.setStage(stage);
            controller.setLoginRecebido(this.campoLogin.getText());
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch(IOException ex) {
            MensagemUtil.mensagemErro(
                    "Erro ao iniciar a tela de Mudança de Usuários \n\n" + ex.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.campoLogin.requestFocus();
    }

    @FXML
    private void botaoAcessarAction(ActionEvent event) {
        this.executarLogin();
    }

    @FXML
    private void botaoCancelarAction(ActionEvent event) {
        AcessoUtil.setUsuarioLogado(null);
        this.sair();
    }

    @FXML
    private void campoLoginOnKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.campoSenha.requestFocus();
        }
    }

    @FXML
    private void campoSenhaOnKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.executarLogin();
        }
    }

}
