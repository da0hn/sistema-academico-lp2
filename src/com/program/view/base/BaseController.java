package com.program.view.base;

import com.program.acesso.AcessoUtil;
import com.program.vo.EnumPerfilUsuario;
import javafx.stage.Stage;

public class BaseController {

    private Stage stage;


    public BaseController() throws Exception {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void sair() {
        this.stage.close();
    }

    public static boolean verificaPermissaoAcesso(EnumPerfilUsuario perfilNecessario) {
        if(AcessoUtil.getUsuarioLogado() != null) {
            if(AcessoUtil.getUsuarioLogado().getPerfil() == perfilNecessario) {
                return true;
            }
            else {
                MensagemUtil.mensagemErro("Usuário não autoriazado para essa ação");
                return false;
            }
        }
        else {
            MensagemUtil.mensagemErro("Usuário não Identificado");
            return false;
        }
    }
}
