package com.program.view.propriedadesconexao;

import com.program.negocio.Curso;
import com.program.negocio.base.NegocioException;
import com.program.persistencia.base.DataConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.persistencia.base.PropertiesConnection;
import com.program.view.base.MensagemUtil;
import com.program.vo.CursoVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SalvarPropriedadesController implements Initializable {

    //---------Classes de Negócio e Controle da Lógica----------
    private Stage stage;

    //---------Componentes Visuais---------
    @FXML
    private Button botaoIncluir;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Label labelRodape;
    @FXML
    private GridPane gridCampos;
    @FXML
    private TextField campoUrl;
    @FXML
    private TextField campoDriver;
    @FXML
    private TextField campoLogin;
    @FXML
    private TextField campoSenha;

    public void setPalcoOrigem(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.gridCampos.setDisable(true);
        this.botaoIncluir.setDisable(false);
        this.botaoSalvar.setDisable(true);
    }

    private void iniciarInclusao() {
        this.botaoIncluir.setDisable(true);
        this.botaoSalvar.setDisable(false);
        this.campoUrl.clear();
        this.campoDriver.clear();
        this.campoLogin.clear();
        this.campoSenha.clear();
        this.gridCampos.setDisable(false);
        this.campoUrl.requestFocus();
        this.labelRodape.setText("Inclusão em andamento...");
    }

    private void processarInclusao() {
        DataConnection data = new DataConnection(
                this.campoLogin.getText(),
                this.campoSenha.getText(),
                this.campoUrl.getText(),
                this.campoDriver.getText()
        );

        try {
            PropertiesConnection.armazenarDados(data);
            this.botaoIncluir.setDisable(false);
            this.botaoSalvar.setDisable(true);
            this.gridCampos.setDisable(true);
            this.labelRodape.setText("Inclusão realizada com sucesso!");
        }
        catch(PersistenciaException ex) {
            MensagemUtil.mensagemErro("Erro de Inclusão \n\n" + ex.toString());
            this.botaoIncluir.setDisable(true);
            this.botaoSalvar.setDisable(false);
            this.campoUrl.requestFocus();
        }
    }

    private void sair() {
        this.stage.close();
    }

    //======================Tratamento de Eventos=====================
    @FXML
    private void botaoIncluirAction(ActionEvent event) {
        this.iniciarInclusao();
    }

    @FXML
    private void botaoSalvarAction(ActionEvent event) {
        this.processarInclusao();
    }

    @FXML
    private void botaoSairAction(ActionEvent event) {
        this.sair();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
