package com.program.negocio;

import com.program.acesso.AcessoUtil;
import com.program.negocio.base.NegocioException;
import com.program.persistencia.UsuarioDAO;
import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.EnumPerfilUsuario;
import com.program.vo.UsuarioVO;

import java.util.List;

public class Usuario {

    private UsuarioDAO usuarioDAO;

    public Usuario() throws NegocioException {
        try {
            this.usuarioDAO = new UsuarioDAO(DatabaseConnection.getInstance());
        }
        catch(PersistenciaException ex) {
            throw new NegocioException("Erro ao iniciar a Persistencia - " + ex.getMessage());
        }
    }

    public void incluir(UsuarioVO usuarioVO) throws NegocioException {
        String mensagemErros = this.validarDados(usuarioVO);

        if(!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }

        try {
            if(usuarioDAO.buscarPorLogin(usuarioVO.getLogin()) != null) {
                mensagemErros += "Ja existe um usuario com esse Login";
                throw new NegocioException(mensagemErros);
            }

            usuarioVO.setSenha(usuarioVO.getLogin().toLowerCase()); //senha inicial de usuário
            if(usuarioDAO.incluir(usuarioVO) == 1) {
                usuarioDAO.confirmarTransacao();
            }
            else {
                throw new NegocioException("Inclusao nao realizada!");
            }
        }
        catch(PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir o usuario - " + ex.getMessage());
        }
    }

    public void alterar(UsuarioVO usuarioVO) throws NegocioException {
        String mensagemErros = this.validarDados(usuarioVO);

        if(!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }

        if(usuarioVO.getSenha() != null && usuarioVO.getSenha().equals(usuarioVO.getLogin())) {
            mensagemErros += "Senha nao pode ser igual ao login";
            throw new NegocioException(mensagemErros);
        }

        try {
            UsuarioVO usuarioTemp = usuarioDAO.buscarPorCodigo(usuarioVO.getCodigo());
            if(usuarioTemp == null) {
                mensagemErros += "Nao existe um usuario com esse Codigo";
                throw new NegocioException(mensagemErros);
            }

            usuarioTemp = usuarioDAO.buscarPorLogin(usuarioVO.getLogin());
            if(usuarioTemp != null && !usuarioTemp.equals(usuarioVO)) {
                mensagemErros += "Ja existe um usuario com esse Login";
                throw new NegocioException(mensagemErros);
            }

            if(usuarioDAO.alterar(usuarioVO) == 1) {
                usuarioDAO.confirmarTransacao();
            }
            else {
                throw new NegocioException("Alteracao nao realizada!");
            }
        }
        catch(PersistenciaException ex) {
            throw new NegocioException("Erro ao alterar o usuario - " + ex.getMessage());
        }
    }

    public void excluir(UsuarioVO usuarioVO) throws NegocioException {
        try {
            if(usuarioDAO.buscarPorCodigo(usuarioVO.getCodigo()) == null) {
                throw new NegocioException("Usuario nao identificado!");
            }

            if(usuarioVO.getNome().equalsIgnoreCase("Administrador")) {
                throw new NegocioException("Usuario Administrador nao pode ser excluido!");
            }

            //fazer a veriricação se o usuario tem relacionamento com registros de atendimento

            if(usuarioDAO.excluir(usuarioVO.getCodigo()) == 0) {
                throw new NegocioException("Exclusao nao realizada!");
            }
            else {
                usuarioDAO.confirmarTransacao();
            }
        }
        catch(PersistenciaException ex) {
            throw new NegocioException("Erro ao excluir o usuario - " + ex.getMessage());
        }
    }

    public List<UsuarioVO> pesquisaPorNome(String parteNome) {
        return usuarioDAO.buscarPorNome(parteNome);
    }

    public UsuarioVO pesquisaPorCodigo(int codigo) {
        return usuarioDAO.buscarPorCodigo(codigo);
    }

    public UsuarioVO pesquisaPorLogin(String login) {
        return usuarioDAO.buscarPorLogin(login);
    }

    public String validarDados(UsuarioVO usuarioVO) {
        String mensagemErros = "";

        if(usuarioVO.getLogin() == null || usuarioVO.getMatricula().length() < 3) {
            mensagemErros += "Login do usuario nao pode ser menor que 5 caracteres!";
        }

        if(usuarioVO.getMatricula() == null || usuarioVO.getMatricula().length() == 0) {
            mensagemErros += "\nMatricula do usuario nao pode ser vazia!";
        }

        if(usuarioVO.getNome() == null || usuarioVO.getNome().length() == 0) {
            mensagemErros += "\nNome do usuario nao pode ser vazio!";
        }

        if(usuarioVO.getEmail() == null || usuarioVO.getEmail().length() == 0) {
            mensagemErros += "\nE-mail do usuario nao pode ser vazio!";
        }

        if(usuarioVO.getPerfil() == null) {
            mensagemErros += "\nPerfil do usuario nao pode ser vazio!";
        }

        return mensagemErros;
    }

    public String verificaPrimeiroAcesso() throws NegocioException {
        String resp = "";
        if(AcessoUtil.getUsuarioLogado() == null) {
            //não existe nenhum usuario cadastrado
            if(this.pesquisaPorNome("").isEmpty()) {
                try {
                    UsuarioVO usuarioVO = new UsuarioVO();
                    usuarioVO.setMatricula("000000");
                    usuarioVO.setLogin("adm");
                    usuarioVO.setNome("Administrador");
                    usuarioVO.setPerfil(EnumPerfilUsuario.ADMINISTRADOR);
                    //senha padrão de inclusão
                    usuarioVO.setSenha(usuarioVO.getLogin().toLowerCase());
                    usuarioVO.setEmail("adm@adm");
                    this.incluir(usuarioVO);
                    resp = "Um usuario Administrador (login: adm, senha adm) foi gerado. \nFavor " +
                            "realizar Login para alterar senha inicial";
                }
                catch(NegocioException ex) {
                    throw new NegocioException(
                            "Usuario Administrador nao gerado - " + ex.getMessage());
                }
            }
        }
        return resp;
    }

    public void alterarSenha(String longin, String novaSenha,
            String confirmacao) throws NegocioException {

        if(novaSenha.equals(confirmacao)) {
            UsuarioVO usuarioVO = usuarioDAO.buscarPorLogin(longin);
            if(usuarioVO != null) {
                try {
                    if(usuarioDAO.alterarSenha(longin, novaSenha) == 1) {
                        usuarioDAO.confirmarTransacao();
                    }
                    else {
                        throw new NegocioException("Senha nao alterada");
                    }
                }
                catch(PersistenciaException ex) {
                    throw new NegocioException("Erro ao alterar a senha - " + ex);
                }
            }
            else {
                throw new NegocioException("Login não localizado");
            }
        }
        else {
            throw new NegocioException("A Senha e a Confirmacao devem ser iguais");
        }
    }

    public UsuarioVO login(String login, String senha) {
        UsuarioVO usuarioVO = null;
        usuarioVO = usuarioDAO.buscarPorLogin(login);
        if(usuarioVO != null) {
            if(senha.equals(usuarioVO.getSenha())) {
                return usuarioVO;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

}
