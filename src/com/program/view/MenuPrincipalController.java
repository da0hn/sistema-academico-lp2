package com.program.view;

import com.program.view.acesso.Login;
import com.program.view.aluno.CadastroAlunoController;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.view.curso.CadastroCursoController;
import com.program.view.disciplina.CadastroDisciplinaController;
import com.program.view.propriedadesconexao.SalvarPropriedadesController;
import com.program.view.usuario.CadastroUsuarioController;
import com.program.vo.EnumPerfilUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipalController implements Initializable {

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Login.executarLogin();
    }

    @FXML
    private void menuItemCadastroAlunoAction(ActionEvent event) {
        if(BaseController.verificaPermissaoAcesso(EnumPerfilUsuario.GESTAO)) {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("aluno/CadastroAlunoFXML.fxml"));
                parent = (Parent) fxmlLoader.load();
            }
            catch(IOException ex) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de cadastro de Aluno \n\n" + ex.getMessage());
            }

            CadastroAlunoController controller =
                    (CadastroAlunoController) fxmlLoader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Cadastro de Alunos");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    private void menuItemCadastroCursoAction(ActionEvent event) {
        if(BaseController.verificaPermissaoAcesso(EnumPerfilUsuario.GESTAO)) {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;

            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("curso/CadastroCursoFXML.fxml"));
                parent = (Parent) fxmlLoader.load();
            }
            catch(IOException ex) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de cadastro de Cursos \n\n" + ex.getMessage());
            }

            CadastroCursoController controller =
                    (CadastroCursoController) fxmlLoader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Cadastro de Cursos");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    private void menuItemCadastroUsuarioAction(ActionEvent event) {
        if(BaseController.verificaPermissaoAcesso(EnumPerfilUsuario.ADMINISTRADOR)) {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;

            try {
                fxmlLoader = new FXMLLoader(
                        getClass().getResource("usuario/CadastroUsuarioFXML.fxml"));
                parent = (Parent) fxmlLoader.load();
            }
            catch(IOException ex) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de cadastro de Usuários \n\n" + ex.getMessage());
            }

            CadastroUsuarioController controller =
                    (CadastroUsuarioController) fxmlLoader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Cadastro de Usuários");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    private void menuItemConfiguracaoConexaoAction(ActionEvent event) {
        if(BaseController.verificaPermissaoAcesso(EnumPerfilUsuario.ADMINISTRADOR)) {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(
                        getClass().getResource("propriedadesconexao/SalvarPropriedadesFXML.fxml"));
                parent = fxmlLoader.load();
            }
            catch(IOException ex) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de configuração das propriedades de conexão " +
                                "\n\n" + ex.getMessage());
            }

            SalvarPropriedadesController controller = fxmlLoader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Configuração das propriedades de conexão");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    private void menuItemCadastroDisciplinaAction(ActionEvent event) {
        if(BaseController.verificaPermissaoAcesso(EnumPerfilUsuario.GESTAO)) {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = null;
            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(
                        getClass().getResource("disciplina/CadastroDisciplinaFXML.fxml"));
                parent = fxmlLoader.load();
            }
            catch(IOException ex) {
                MensagemUtil.mensagemErro(
                        "Erro ao iniciar a tela de cadastro de Disciplina \n\n" + ex.getMessage());
            }

            CadastroDisciplinaController controller = fxmlLoader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Cadastro Disciplinas");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    private void menuItemCloseAction(ActionEvent event) {
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
