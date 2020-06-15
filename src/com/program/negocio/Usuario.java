package com.program.negocio;

import com.program.acesso.AcessoUtil;
import com.program.negocio.base.NegocioException;
import com.program.persistencia.UsuarioDAO;
import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.EnumPerfilUsuario;
import com.program.vo.UsuarioVO;

import java.util.List;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 10/06/2020
 */
public class Usuario {

    private UsuarioDAO usuarioDAO;

    public Usuario() throws NegocioException {
        try {
            this.usuarioDAO = new UsuarioDAO(DatabaseConnection.getInstance());
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao iniciar a Persistencia - " + e.getMessage());
        }
    }

    public void incluir(UsuarioVO usuario) throws NegocioException {
        String mensagensDeErro = this.validarDados(usuario);
        if(!mensagensDeErro.isEmpty()) {
            throw new NegocioException(mensagensDeErro);
        }
        try {
            if(usuarioDAO.buscarPorLogin(usuario.getLogin()) != null) {
                mensagensDeErro += " Ja existe um usuário com esse Login";
                throw new NegocioException(mensagensDeErro);
            }
            // senha inicial do usuário
            usuario.setSenha(usuario.getLogin().toLowerCase());
            if(usuarioDAO.incluir(usuario) == 1) {
                usuarioDAO.confirmarTransacao();
            }
            else {
                usuarioDAO.cancelarTransacao();
                throw new NegocioException(" Inclusão não realizada!");
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao incluir o usuário - " + e.getMessage());
        }
    }

    public void alterar(UsuarioVO usuario) throws NegocioException {
        String mensagensDeErro = this.validarDados(usuario);
        if(!mensagensDeErro.isEmpty()) {
            throw new NegocioException(mensagensDeErro);
        }
        if(usuario.getSenha() != null && usuario.getSenha().equals(usuario.getLogin())) {
            mensagensDeErro += " Senha não pode ser igual ao login";
            throw new NegocioException(mensagensDeErro);
        }
        try {
            var usuarioTemp = usuarioDAO.buscarPorCodigo(usuario.getCodigo());
            if(usuarioTemp == null) {
                mensagensDeErro += " Não existe um usuário com esse Código";
                throw new NegocioException(mensagensDeErro);
            }
            usuarioTemp = usuarioDAO.buscarPorLogin(usuario.getLogin());
            if(usuarioTemp != null && !usuarioTemp.equals(usuario)) {
                mensagensDeErro += " Já existe um usuario com esse login";
                throw new NegocioException(mensagensDeErro);
            }
            if(usuarioDAO.alterar(usuario) == 1) {
                usuarioDAO.confirmarTransacao();
            }
            else {
                usuarioDAO.cancelarTransacao();
                throw new NegocioException(" Alteração não realizada!");
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao alterar o usuario - " + e.getMessage());
        }
    }

    public void excluir(UsuarioVO usuario) throws NegocioException {
        try {
            if(usuarioDAO.buscarPorCodigo(usuario.getCodigo()) == null) {
                throw new NegocioException(" Usuario não identificado!");
            }
            if(usuario.getNome().equalsIgnoreCase("Administrador")) {
                throw new NegocioException("Usuario Administrador não pode ser excluido!");
            }
            // fazer a verificacao se o usuario tem relacionamento com registros de atendimentos
            if(usuarioDAO.excluir(usuario.getCodigo()) == 0) {
                usuarioDAO.cancelarTransacao();
                throw new NegocioException("Exclusão não realizada");
            }
            else {
                usuarioDAO.confirmarTransacao();
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException(" Erro ao excluir o usuario - " + e.getMessage());
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

    private String validarDados(UsuarioVO usuario) {
        var builder = new StringBuilder();
        if(usuario.getLogin() == null || usuario.getLogin().length() < 5) {
            builder.append("Login do usuário não pode ser menor que 5 caracteres!");
        }
        if(usuario.getMatricula() == null || usuario.getMatricula().length() == 0) {
            builder.append("\nMatricula do usuario não pode ser vazia!");
        }
        if(usuario.getNome() == null || usuario.getNome().length() == 0) {
            builder.append("\nNome do usuário não pode ser vazio!");
        }
        if(usuario.getEmail() == null || usuario.getEmail().length() == 0) {
            builder.append("\nE-mail do usuario não pde ser vazio!");
        }
        if(usuario.getPerfil() == null) {
            builder.append("\nPerfil do usuario não pode ser vazio!");
        }
        return builder.toString();
    }

    public String verificaPrimeiroAcesso() throws NegocioException {
        String resp = "";
        if(AcessoUtil.getUsuarioLogado() == null) {
            // não existe nenhum usuario cadastrado no banco
            if(this.pesquisaPorNome("").isEmpty()) {
                try {
                    var usuario = new UsuarioVO();
                    usuario.setMatricula("000000");
                    usuario.setLogin("adm");
                    usuario.setNome("Administrador");
                    usuario.setPerfil(EnumPerfilUsuario.ADMINISTRADOR);
                    // senha padrão de inclusão
                    usuario.setSenha(usuario.getLogin().toLowerCase());
                    usuario.setEmail("adm@adm.com");
                    this.incluir(usuario);
                    resp = "Um usuario Administrador (login: adm, senha: adm) foi gerado" +
                            "realizar Login para alterar senha inicial.";
                }
                catch(NegocioException e) {
                    throw new NegocioException(
                            "Usuario administrador não foi gerado - " + e.getMessage());
                }
            }
        }
        return resp;
    }

    public void alterarSenha(String login, String novaSenha,
            String confirmacao) throws NegocioException {
        try {
            if(novaSenha.equals(confirmacao)) {
                var usuario = usuarioDAO.buscarPorLogin(login);
                if(usuario != null) {
                    if(usuarioDAO.alterarSenha(login, novaSenha) == 1) {
                        usuarioDAO.confirmarTransacao();
                    }
                    else {
                        usuarioDAO.cancelarTransacao();
                        throw new NegocioException("Senha não alterada");
                    }
                }
                else {
                    throw new NegocioException("Login não localizado");
                }
            }
            else {
                throw new NegocioException("A senha e a confirmação devem ser iguais");
            }
        }
        catch(PersistenciaException e) {
            throw new NegocioException("Erro ao tentar alterar a senha - " + e.getMessage());
        }
    }

    public UsuarioVO login(String login, String senha) throws NegocioException {
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
