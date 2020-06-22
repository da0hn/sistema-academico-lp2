package test;

import com.program.persistencia.CursoDAO;
import com.program.persistencia.base.DatabaseConnection;
import com.program.persistencia.base.PersistenciaException;
import com.program.vo.CursoVO;

/**
 * @author Gabriel Honda on 22/06/2020
 * @project lp2_academico
 */
public class TesteDisciplinaDAO {
    public static void main(String[] args) {
        try {
            CursoDAO cursoDAO = new CursoDAO(DatabaseConnection.getInstance());
            cursoDAO.incluir(new CursoVO("Engenharia de Controle e Automação"));
            cursoDAO.confirmarTransacao();
        }
        catch(PersistenciaException e) {
            e.printStackTrace();
        }
    }
}
