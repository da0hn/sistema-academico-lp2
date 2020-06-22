package com.program.view.curso;

import com.program.negocio.Curso;
import com.program.negocio.base.NegocioException;
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

public class IncluirCursoController implements Initializable {

    //---------Classes de Negócio e Controle da Lógica----------
    private Curso cursoNegocio;
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
    private TextField campoNome;

    public IncluirCursoController() {
        try {
            this.cursoNegocio = new Curso();
        }
        catch(NegocioException ex) {
            MensagemUtil.mensagemAlerta("Camada de Negócio não iniciada!");
            this.sair();
        }
    }

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
        this.campoNome.clear();
        this.gridCampos.setDisable(false);
        this.campoNome.requestFocus();
        this.labelRodape.setText("Inclusão em andamento...");
    }

    private void processarInclusao() {
        CursoVO cursoVO = new CursoVO();

        cursoVO.setNome(this.campoNome.getText());

        try {
            this.cursoNegocio.inserir(cursoVO);
            this.botaoIncluir.setDisable(false);
            this.botaoSalvar.setDisable(true);
            this.gridCampos.setDisable(true);
            this.labelRodape.setText("Inclusão realizada com sucesso!");
        }
        catch(NegocioException ex) {
            MensagemUtil.mensagemErro("Erro de Inclusão \n\n" + ex.toString());
            this.botaoIncluir.setDisable(true);
            this.botaoSalvar.setDisable(false);
            this.campoNome.requestFocus();
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

}
