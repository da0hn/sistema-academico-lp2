package com.program.view.acesso;


import com.program.acesso.AcessoUtil;
import com.program.negocio.Usuario;
import com.program.negocio.base.NegocioException;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.vo.UsuarioVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Gabriel Honda on 18/06/2020
 * @project lp2_academico
 */
public class AlterarSenhaUsuarioController extends BaseController implements Initializable {

    private Usuario usuarioNegocio;

    @FXML
    private TextField campoLogin;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private PasswordField campoConfirmarSenha;

    public AlterarSenhaUsuarioController() throws Exception {
        try {
            this.usuarioNegocio = new Usuario();
        }
        catch(NegocioException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.campoSenha.requestFocus();
    }

    private void mudarSenha() {

        String login = this.campoLogin.getText().trim();
        String senha = this.campoSenha.getText().trim();
        String confirmarSenha = this.campoConfirmarSenha.getText().trim();

        try {
            usuarioNegocio.alterarSenha(login, senha, confirmarSenha);
            UsuarioVO usuarioLogado = usuarioNegocio.login(login, senha);
            if(usuarioLogado != null) {
                AcessoUtil.setUsuarioLogado(usuarioLogado);
            }
            this.sair();
        }
        catch(NegocioException ex) {
            MensagemUtil.mensagemErro(ex.getMessage());
        }

    }

    public void setLoginRecebido(String loginRecebido) {
        this.campoLogin.setText(loginRecebido);
    }

    @FXML
    private void botaoAlterarSenhaAction(ActionEvent event) {
        this.mudarSenha();
    }

    @FXML
    private void botaoCancelarAction(ActionEvent event) {
        this.sair();
    }

    @FXML
    private void campoSenhaOnKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.campoConfirmarSenha.requestFocus();
        }
    }

}
