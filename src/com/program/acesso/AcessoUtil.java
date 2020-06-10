package com.program.acesso;

import com.program.vo.UsuarioVO;

/*
 * @project lp2_academico
 * @author Gabriel Honda on 10/06/2020
 */
public class AcessoUtil {
    private static UsuarioVO usuarioLogado;

    public static UsuarioVO getUsuarioLogado(){
        return AcessoUtil.usuarioLogado;
    }

    public static void setUsuarioLogado(UsuarioVO usuarioLogado) {
        AcessoUtil.usuarioLogado = usuarioLogado;
    }
}
