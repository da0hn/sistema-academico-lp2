package com.program.view.curso;

import com.program.negocio.Curso;
import com.program.negocio.base.NegocioException;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.view.base.OpCadastroEnum;
import com.program.vo.CursoVO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroCursoController extends BaseController implements Initializable {

    //---------Classes de Negocio e Controle da Logica----------
    private Curso cursoNegocio;
    private OpCadastroEnum opCadastro;
    private List<CursoVO> listaCurso;

    //---------Componentes Visuais---------
    @FXML
    private Button botaoIncluir;
    @FXML
    private Button botaoAlterar;
    @FXML
    private Button botaoExcluir;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoCancelar;
    @FXML
    private Button botaoSair;
    @FXML
    private Label labelRodape;
    @FXML
    private TabPane tabDados;
    @FXML
    private GridPane gridCampos;
    @FXML
    private TextField campoCodigo;
    @FXML
    private TextField campoNome;
    @FXML
    private TableView tabelaDados;
    @FXML
    private TextField campoPesquisaNome;

    public CadastroCursoController() throws Exception {
        this.cursoNegocio = new Curso();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.gridCampos.setDisable(true);
        this.campoCodigo.setDisable(true);
        this.opCadastro = OpCadastroEnum.CONSULTAR;

        this.TrataBotoes();
        this.iniciarDadosTableView();
    }

    //=====================Trata a Logica da Interface de Cadastro=========
    private void iniciarDadosTableView() {
        try {
            this.listaCurso = this.cursoNegocio.pesquisaParteNome(" ");
        }
        catch(NegocioException ex) {
            MensagemUtil.mensagemAlerta("Dados nao localizados!" + ex.getMessage());
        }

        TableColumn coluna1 = new TableColumn("Codigo");
        TableColumn coluna2 = new TableColumn("Nome");

        coluna1.setMinWidth(100);
        coluna2.setMinWidth(300);

        coluna1.setCellValueFactory(new PropertyValueFactory("codigo"));
        coluna2.setCellValueFactory(new PropertyValueFactory("nome"));

        this.tabelaDados.getColumns().addAll(coluna1, coluna2);
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaCurso));
    }

    public void atualizarDadosTableView() {
        this.tabelaDados.getItems().clear();
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaCurso));
        this.tabelaDados.refresh();
    }

    private CursoVO obterVOTableView() {
        CursoVO cursoVO = null;

        if(this.tabelaDados.getSelectionModel().getSelectedItem() != null) {
            TableViewSelectionModel selectionModel = this.tabelaDados.getSelectionModel();
            cursoVO = (CursoVO) selectionModel.getSelectedItem();
        }
        return cursoVO;
    }

    private void iniciarInclusao() {
        this.opCadastro = OpCadastroEnum.INCLUIR;
        this.TrataBotoes();
        this.limparCampos();
        this.tabDados.getSelectionModel().selectLast();
        this.gridCampos.setDisable(false);
        this.campoNome.requestFocus();
        this.labelRodape.setText("Inclusao em andamento...");
    }

    private void iniciarAlteracao() {
        CursoVO cursoVO = this.obterVOTableView();

        if(cursoVO != null) {
            try {
                cursoVO = this.cursoNegocio.pesquisaMatricula(cursoVO.getCodigo());
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemAlerta("Erro ao localizar o curso!" + ex.getMessage());
            }

            if(cursoVO != null) {
                this.opCadastro = OpCadastroEnum.ALTERAR;
                this.TrataBotoes();
                this.preencheCampos(cursoVO);
                this.tabDados.getSelectionModel().selectLast();
                this.gridCampos.setDisable(false);
                this.campoNome.requestFocus();
                this.labelRodape.setText("Alteracao em andamento...");
            }
            else {
                MensagemUtil.mensagemAlerta("Item nao localizado!");
            }
        }
        else {
            MensagemUtil.mensagemAlerta("Nenhum item selecionado!");
        }
    }

    private void processarInclusao() {
        CursoVO cursoVO = this.criarVODados();

        if(cursoVO != null) {
            try {
                this.cursoNegocio.inserir(cursoVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Inclusao realizada com sucesso!");
                this.listaCurso = this.cursoNegocio.pesquisaParteNome("");
                this.atualizarDadosTableView();
                this.tabDados.getSelectionModel().selectFirst();
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemErro("Erro de Inclusao \n\n" + ex.getMessage());
                this.opCadastro = OpCadastroEnum.INCLUIR;
                this.TrataBotoes();
                this.campoNome.requestFocus();
            }
        }
    }

    public void processarAlteracao() {
        CursoVO cursoVO = this.criarVODados();

        if(cursoVO != null) {
            try {
                this.cursoNegocio.alterar(cursoVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Alteracao realizada com sucesso!");
                this.listaCurso = this.cursoNegocio.pesquisaParteNome("");
                this.atualizarDadosTableView();
                this.tabDados.getSelectionModel().selectFirst();
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemErro("Erro de Alteracao \n\n" + ex.getMessage());
                this.opCadastro = OpCadastroEnum.ALTERAR;
                this.TrataBotoes();
                this.campoNome.requestFocus();
            }
        }
    }

    private void processarExclusao() {
        CursoVO cursoVO = this.obterVOTableView();

        if(cursoVO != null) {
            try {
                cursoVO = this.cursoNegocio.pesquisaMatricula(cursoVO.getCodigo());
                this.opCadastro = OpCadastroEnum.EXCLUIR;
                this.TrataBotoes();
                this.labelRodape.setText("Exclusao em andamento...");

                if(MensagemUtil.mensagemConfirmacao("Confirma a exclusao de " + cursoVO)) {
                    try {
                        this.cursoNegocio.excluir(cursoVO.getCodigo());
                        this.TrataBotoes();
                        this.labelRodape.setText("Exclusao realizada com sucesso!");
                        this.listaCurso = this.cursoNegocio.pesquisaParteNome("");
                        this.atualizarDadosTableView();
                    }
                    catch(NegocioException ex) {
                        MensagemUtil.mensagemErro("Erro de Exclusao \n\n" + ex.getMessage());
                    }
                    this.opCadastro = OpCadastroEnum.CONSULTAR;
                }
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemAlerta("Item nao localizado!");
            }

        }
        else {
            MensagemUtil.mensagemAlerta("Nenhum item selecionado!");
        }
    }

    private void processarCancelamento() {
        this.opCadastro = OpCadastroEnum.CANCELAR;
        this.TrataBotoes();
        this.tabDados.getSelectionModel().selectFirst();
        this.gridCampos.setDisable(true);
        this.labelRodape.setText("Operacao cancelada...");
    }

    private void processarFiltroPorNome(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.listaCurso.clear();

            try {
                this.listaCurso = this.cursoNegocio.pesquisaParteNome(
                        this.campoPesquisaNome.getText().trim());
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemErro(
                        "Erro durante a consulta de dados \n\n" + ex.getMessage());
            }
            this.atualizarDadosTableView();
        }
    }

    private void TrataBotoes() {
        if(this.opCadastro == OpCadastroEnum.INCLUIR || this.opCadastro == OpCadastroEnum.ALTERAR) {
            this.botaoIncluir.setDisable(true);
            this.botaoAlterar.setDisable(true);
            this.botaoExcluir.setDisable(true);
            this.botaoSalvar.setDisable(false);
            this.botaoCancelar.setDisable(false);
        }
        else if(this.opCadastro == OpCadastroEnum.SALVAR
                || this.opCadastro == OpCadastroEnum.CANCELAR
                || this.opCadastro == OpCadastroEnum.CONSULTAR) {
            this.botaoIncluir.setDisable(false);
            this.botaoAlterar.setDisable(false);
            this.botaoExcluir.setDisable(false);
            this.botaoSalvar.setDisable(true);
            this.botaoCancelar.setDisable(true);
        }
    }

    private void limparCampos() {
        this.campoCodigo.clear();
        this.campoNome.clear();
    }

    private void preencheCampos(CursoVO cursoVO) {
        this.campoCodigo.setText(String.valueOf(cursoVO.getCodigo()));
        this.campoNome.setText(cursoVO.getNome());
    }

    private CursoVO criarVODados() {
        CursoVO cursoVO;

        try {
            cursoVO = new CursoVO();

            if(this.opCadastro == OpCadastroEnum.ALTERAR) {
                cursoVO.setCodigo(Integer.parseInt(this.campoCodigo.getText()));
            }

            cursoVO.setNome(this.campoNome.getText());

        }
        catch(Exception ex) {
            cursoVO = null;
            MensagemUtil.mensagemErro("Dados inconsistentes!");
        }
        return cursoVO;
    }

    //======================Tratamento de Eventos=====================
    @FXML
    private void botaoIncluirAction(ActionEvent event) {
        this.iniciarInclusao();
    }

    @FXML
    private void botaoAlterarAction(ActionEvent event) {
        this.iniciarAlteracao();
    }

    @FXML
    private void botaoExcluirAction(ActionEvent event) {
        this.processarExclusao();
    }

    @FXML
    private void botaoSalvarAction(ActionEvent event) {
        if(this.opCadastro == OpCadastroEnum.INCLUIR) {
            this.processarInclusao();
        }
        else if(this.opCadastro == OpCadastroEnum.ALTERAR) {
            this.processarAlteracao();
        }
    }

    @FXML
    private void botaoCancelarAction(ActionEvent event) {
        this.processarCancelamento();
    }

    @FXML
    private void botaoSairAction(ActionEvent event) {
        this.sair();
    }

    @FXML
    private void campoPesquisaNomeOnKeyPressed(KeyEvent keyEvent) {
        this.processarFiltroPorNome(keyEvent);
    }
}
