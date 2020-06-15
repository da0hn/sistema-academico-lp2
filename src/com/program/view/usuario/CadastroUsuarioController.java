package com.program.view.usuario;

import com.program.negocio.Usuario;
import com.program.negocio.base.NegocioException;
import com.program.view.base.BaseController;
import com.program.view.base.MensagemUtil;
import com.program.view.base.OpCadastroEnum;
import com.program.vo.EnumPerfilUsuario;
import com.program.vo.UsuarioVO;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroUsuarioController extends BaseController implements Initializable {

    //---------Classes de Negócio e Controle da Lógica----------
    private Usuario usuarioNegocio;
    private OpCadastroEnum opCadastro;
    private List<UsuarioVO> listaUsuario;

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
    private TableView<UsuarioVO> tabelaDados;

    @FXML
    private TextField campoCodigo;
    @FXML
    private TextField campoMatricula;
    @FXML
    private TextField campoLogin;
    @FXML
    private TextField campoNome;
    @FXML
    private ComboBox<EnumPerfilUsuario> campoPerfil;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoTelefone;

    @FXML
    private TextField campoPesquisaNome;

    public CadastroUsuarioController() throws Exception {
        try {
            this.usuarioNegocio = new Usuario();
        }
        catch(NegocioException ex) {
            throw new Exception(ex.getMessage());
        }
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
        this.listaUsuario = this.usuarioNegocio.pesquisaPorNome(" ");

        TableColumn coluna1 = new TableColumn("Código");
        TableColumn coluna2 = new TableColumn("Matrícula");
        TableColumn coluna3 = new TableColumn("Nome");
        TableColumn coluna4 = new TableColumn("Perfil");
        TableColumn coluna5 = new TableColumn("Telefone");
        TableColumn coluna6 = new TableColumn("E-mail");

        coluna1.setMinWidth(80);
        coluna2.setMinWidth(100);
        coluna3.setMinWidth(200);
        coluna4.setMinWidth(130);
        coluna5.setMinWidth(100);
        coluna6.setMinWidth(300);

        coluna1.setCellValueFactory(new PropertyValueFactory("codigo"));
        coluna2.setCellValueFactory(new PropertyValueFactory("matricula"));
        coluna3.setCellValueFactory(new PropertyValueFactory("nome"));
        coluna4.setCellValueFactory(new PropertyValueFactory("perfil"));
        coluna5.setCellValueFactory(new PropertyValueFactory("telefone"));
        coluna6.setCellValueFactory(new PropertyValueFactory("email"));

        this.tabelaDados.getColumns().addAll(coluna1, coluna2, coluna3, coluna4, coluna5, coluna6);
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaUsuario));

        this.campoPerfil.setItems(FXCollections.observableArrayList(EnumPerfilUsuario.values()));
    }

    public void atualizarDadosTableView() {
        this.tabelaDados.getItems().clear();
        this.tabelaDados.setItems(FXCollections.observableArrayList(this.listaUsuario));
        this.tabelaDados.refresh();
    }

    private UsuarioVO obterVOTableView() {
        UsuarioVO usuarioVO = null;

        if(this.tabelaDados.getSelectionModel().getSelectedItem() != null) {
            TableViewSelectionModel<UsuarioVO> selectionModel =
                    this.tabelaDados.getSelectionModel();
            usuarioVO = selectionModel.getSelectedItem();
        }
        return usuarioVO;
    }

    private void iniciarInclusao() {
        this.opCadastro = OpCadastroEnum.INCLUIR;
        this.TrataBotoes();
        this.limparCampos();
        this.tabDados.getSelectionModel().selectLast();
        this.gridCampos.setDisable(false);
        this.campoMatricula.requestFocus();
        this.labelRodape.setText("Inclusão em andamento...");
    }

    private void iniciarAlteracao() {
        UsuarioVO usuarioVO = this.obterVOTableView();

        if(usuarioVO != null) {
            usuarioVO = this.usuarioNegocio.pesquisaPorCodigo(usuarioVO.getCodigo());

            if(usuarioVO != null) {
                this.opCadastro = OpCadastroEnum.ALTERAR;
                this.TrataBotoes();
                this.preencheCampos(usuarioVO);
                this.tabDados.getSelectionModel().selectLast();
                this.gridCampos.setDisable(false);
                this.campoMatricula.requestFocus();
                this.labelRodape.setText("Alteração em andamento...");
            }
            else {
                MensagemUtil.mensagemAlerta("Item não localizado!");
            }
        }
        else {
            MensagemUtil.mensagemAlerta("Nenhum item selecionado!");
        }
    }

    private void processarInclusao() {
        UsuarioVO usuarioVO = this.criarVODados();

        if(usuarioVO != null) {
            try {
                this.usuarioNegocio.incluir(usuarioVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Inclusão realizada com sucesso!");
                this.listaUsuario = this.usuarioNegocio.pesquisaPorNome("");
                this.atualizarDadosTableView();
                this.tabDados.getSelectionModel().selectFirst();
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemErro("Erro de Inclusão \n\n" + ex.getMessage());
                this.opCadastro = OpCadastroEnum.INCLUIR;
                this.TrataBotoes();
                this.campoMatricula.requestFocus();
            }
        }
    }

    public void processarAlteracao() {
        UsuarioVO usuarioVO = this.criarVODados();

        if(usuarioVO != null) {
            try {
                this.usuarioNegocio.alterar(usuarioVO);
                this.opCadastro = OpCadastroEnum.SALVAR;
                this.TrataBotoes();
                this.gridCampos.setDisable(true);
                this.labelRodape.setText("Alteração realizada com sucesso!");
                this.listaUsuario = this.usuarioNegocio.pesquisaPorNome("");
                this.atualizarDadosTableView();
                this.tabDados.getSelectionModel().selectFirst();
            }
            catch(NegocioException ex) {
                MensagemUtil.mensagemErro("Erro de Alteração \n\n" + ex.getMessage());
                this.opCadastro = OpCadastroEnum.ALTERAR;
                this.TrataBotoes();
                this.campoMatricula.requestFocus();
            }
        }
    }

    private void processarExclusao() {
        UsuarioVO usuarioVO = this.obterVOTableView();

        if(usuarioVO != null) {
            usuarioVO = this.usuarioNegocio.pesquisaPorCodigo(usuarioVO.getCodigo());
            this.opCadastro = OpCadastroEnum.EXCLUIR;
            this.TrataBotoes();
            this.labelRodape.setText("Exclusão em andamento...");

            if(MensagemUtil.mensagemConfirmacao("Confirma a exclusão de " + usuarioVO)) {
                try {
                    this.usuarioNegocio.excluir(usuarioVO);
                    this.TrataBotoes();
                    this.labelRodape.setText("Exclusão realizada com sucesso!");
                    this.listaUsuario = this.usuarioNegocio.pesquisaPorNome("");
                    this.atualizarDadosTableView();
                }
                catch(NegocioException ex) {
                    MensagemUtil.mensagemErro("Erro de Exclusão \n\n" + ex.getMessage());
                }
                this.opCadastro = OpCadastroEnum.CONSULTAR;
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
        this.labelRodape.setText("Operação cancelada...");
    }

    private void processarFiltroPorNome(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.listaUsuario.clear();

            this.listaUsuario = this.usuarioNegocio.pesquisaPorNome(
                    this.campoPesquisaNome.getText().trim());
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
        this.campoMatricula.clear();
        this.campoLogin.clear();
        this.campoNome.clear();
        this.campoPerfil.getSelectionModel().select(0);
        this.campoEmail.clear();
        this.campoTelefone.clear();
    }

    private void preencheCampos(UsuarioVO usuarioVO) {
        this.campoCodigo.setText(String.valueOf(usuarioVO.getCodigo()));
        this.campoMatricula.setText(usuarioVO.getMatricula());
        this.campoLogin.setText(usuarioVO.getLogin());
        this.campoNome.setText(usuarioVO.getNome());
        this.campoPerfil.getSelectionModel().select(usuarioVO.getPerfil());
        this.campoEmail.setText(usuarioVO.getEmail());
        this.campoTelefone.setText(usuarioVO.getTelefone());
    }

    private UsuarioVO criarVODados() {
        UsuarioVO usuarioVO;

        try {
            usuarioVO = new UsuarioVO();

            if(this.opCadastro == OpCadastroEnum.ALTERAR) {
                usuarioVO.setCodigo(Integer.parseInt(this.campoCodigo.getText()));
            }

            usuarioVO.setMatricula(this.campoMatricula.getText());
            usuarioVO.setLogin(this.campoLogin.getText());
            usuarioVO.setNome(this.campoNome.getText());
            usuarioVO.setPerfil(
                    this.campoPerfil.getSelectionModel().getSelectedItem());
            usuarioVO.setEmail(this.campoEmail.getText());
            usuarioVO.setTelefone(this.campoTelefone.getText());

        }
        catch(Exception ex) {
            usuarioVO = null;
            MensagemUtil.mensagemErro("Dados inconsistentes!");
        }
        return usuarioVO;
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
