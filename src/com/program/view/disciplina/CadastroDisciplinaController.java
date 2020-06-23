package com.program.view.disciplina;

import com.program.negocio.Curso;
import com.program.negocio.Disciplina;
import com.program.negocio.base.NegocioException;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.view.base.OpCadastroEnum;
import com.program.vo.CursoVO;
import com.program.vo.DisciplinaVO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Gabriel Honda on 22/06/2020
 * @project lp2_academicoO
 */
public class CadastroDisciplinaController extends BaseController implements Initializable {

    //---------Classes de Negócio e Controle da Lógica----------
    private Disciplina disciplinaNegocio;
    private Curso cursoNegocio;
    private OpCadastroEnum opCadastro;
    private List<DisciplinaVO> listaDisciplina;
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
    private TextField campoSemestre;
    @FXML
    private TableView tabelaDados;
    @FXML
    private TextField campoPesquisaNome;
    @FXML
    private ComboBox<CursoVO> comboCurso;

    public CadastroDisciplinaController() throws Exception {
        this.disciplinaNegocio = new Disciplina();
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

    //=====================Trata a Lógica da Interface de Cadastro=========
    private void iniciarDadosTableView() {
        try {
            this.listaDisciplina = this.disciplinaNegocio.pesquisaPorNome("");
            this.listaCurso = this.cursoNegocio.pesquisaParteNome("");
        }
        catch(NegocioException ex) {
            MensagemUtil.mensagemAlerta("Dados não localizados!!" + ex.getMessage());
        }
        TableColumn coluna1 = new TableColumn<>("Código");
        TableColumn coluna2 = new TableColumn<>("Nome");
        TableColumn coluna3 = new TableColumn<>("Semestre");
        TableColumn coluna4 = new TableColumn<>("Curso");
        coluna1.setMinWidth(100);
        coluna2.setMinWidth(300);
        coluna3.setMinWidth(80);
        coluna4.setMinWidth(200);
        coluna1.setCellValueFactory(new PropertyValueFactory("codigo"));
        coluna2.setCellValueFactory(new PropertyValueFactory("nome"));
        coluna3.setCellValueFactory(new PropertyValueFactory("semestre"));
        coluna4.setCellValueFactory(new PropertyValueFactory("curso"));
        this.tabelaDados.getColumns().addAll(coluna1, coluna2, coluna3, coluna4);
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaDisciplina));

        this.comboCurso.setItems(FXCollections.observableArrayList(this.listaCurso));

    }

    private void atualizarDadosTableView() {
        this.tabelaDados.getItems().clear();
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaDisciplina));
        this.tabelaDados.refresh();
    }

    private DisciplinaVO obterVOTableView() {
        DisciplinaVO disciplinaVO = null;
        if(this.tabelaDados.getSelectionModel().getSelectedItem() != null) {
            TableViewSelectionModel<DisciplinaVO> selectionModel = this.tabelaDados.getSelectionModel();
            disciplinaVO = selectionModel.getSelectedItem();
        }
        return disciplinaVO;
    }

    private void iniciarInclusao() {
        this.opCadastro = OpCadastroEnum.INCLUIR;
        this.TrataBotoes();
        this.limparCampos();
        this.tabDados.getSelectionModel().selectLast();
        this.gridCampos.setDisable(false);
        this.campoNome.requestFocus();
        this.labelRodape.setText("Inclusão em andamento...");
    }

    private void iniciarAlteracao() {
        DisciplinaVO disciplinaVO = this.obterVOTableView();
        if(disciplinaVO != null) {
            try {
                disciplinaVO = this.disciplinaNegocio.pesquisaPorCodigo(disciplinaVO.getCodigo());
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemAlerta("Erro ao localizar o Aluno" + ex.getMessage());
            }
            if(disciplinaVO != null) {
                this.opCadastro = OpCadastroEnum.ALTERAR;
                this.TrataBotoes();
                this.preecheCampos(disciplinaVO);
                this.tabDados.getSelectionModel().selectLast();
                this.gridCampos.setDisable(false);
                this.labelRodape.setText("Alteracao em andamento...");
            }
            else {
                MensagemUtil.mensagemAlerta("Item não localizado!!");
            }
        }
        else {
            MensagemUtil.mensagemAlerta("Nenhum item selecionado!!");
        }
    }

    private void processarInclusao() {
        DisciplinaVO disciplinaVO = this.criarVODados();
        if(disciplinaVO != null) {
            try {
                this.disciplinaNegocio.inserir(disciplinaVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Inclusao realizada com sucesso!");
                this.listaDisciplina = this.disciplinaNegocio.pesquisaPorNome("");
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

    private void processarAlteracao() {

        DisciplinaVO disciplinaVO = this.criarVODados();
        if(disciplinaVO != null) {
            try {
                this.disciplinaNegocio.alterar(disciplinaVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Alteracao realizada com sucesso!");
                this.listaDisciplina = this.disciplinaNegocio.pesquisaPorNome("");
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
        DisciplinaVO disciplinaVO = this.obterVOTableView();
        if(disciplinaVO != null) {
            try {
                disciplinaVO = this.disciplinaNegocio.pesquisaPorCodigo(disciplinaVO.getCodigo());
                this.opCadastro = OpCadastroEnum.EXCLUIR;
                this.TrataBotoes();
                this.labelRodape.setText("Exclusao em andamento...");
                if(MensagemUtil.mensagemConfirmacao("Confirma a exclusão de " + disciplinaVO)) {
                    try {
                        this.disciplinaNegocio.excluir(disciplinaVO.getCodigo());
                        this.TrataBotoes();
                        this.labelRodape.setText("Exclusao realizada com sucesso!");
                        this.listaDisciplina = this.disciplinaNegocio.pesquisaPorNome("");
                        this.atualizarDadosTableView();
                    }
                    catch(NegocioException ex) {
                        MensagemUtil.mensagemErro("Erro de Exclusao \n\n" + ex.getMessage());
                    }
                    this.opCadastro = OpCadastroEnum.CONSULTAR;
                }
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemAlerta("Item não localizado!!");
            }
        }
        else {
            MensagemUtil.mensagemAlerta("Nenhum item selecionado!!");
        }
    }

    private void processarCancelamento() {
        this.opCadastro = OpCadastroEnum.CANCELAR;
        this.TrataBotoes();
        this.tabDados.getSelectionModel().selectFirst();
        this.gridCampos.setDisable(true);
        this.labelRodape.setText("Operação Cancelada...");
    }

    private void processarFiltroPorNome(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.listaDisciplina.clear();
            try {
                this.listaDisciplina = this.disciplinaNegocio.pesquisaPorNome(
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
        this.campoSemestre.clear();
        this.comboCurso.getSelectionModel().select(0);

    }

    private void preecheCampos(DisciplinaVO disciplinaVO) {
        this.campoCodigo.setText(String.valueOf(disciplinaVO.getCodigo()));
        this.campoNome.setText(disciplinaVO.getNome());
        this.campoSemestre.setText(String.valueOf(disciplinaVO.getSemestre()));
        this.comboCurso.getSelectionModel().select(disciplinaVO.getCurso());
    }

    private DisciplinaVO criarVODados() {
        DisciplinaVO disciplinaVO;

        try {
            disciplinaVO = new DisciplinaVO();
            if(this.opCadastro == OpCadastroEnum.ALTERAR) {
                disciplinaVO.setCodigo(Integer.parseInt(this.campoCodigo.getText()));
            }
            disciplinaVO.setNome(this.campoNome.getText());
            disciplinaVO.setSemestre(Integer.parseInt((this.campoSemestre.getText())));
            disciplinaVO.setCurso(this.comboCurso.getSelectionModel().getSelectedItem());

        }
        catch(Exception ex) {
            disciplinaVO = null;
            MensagemUtil.mensagemErro("Dados inconsistentes");
        }
        return disciplinaVO;
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
