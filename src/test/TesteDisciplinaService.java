package test;

import com.program.negocio.Curso;
import com.program.negocio.Disciplina;
import com.program.negocio.base.NegocioException;
import com.program.vo.DisciplinaVO;

import java.util.List;

/**
 * @author Gabriel Honda on 22/06/2020
 * @project lp2_academicoO
 */
public class TesteDisciplinaService {
    public static void main(String[] args) throws NegocioException {
        Disciplina disciplina = new Disciplina();
        List<DisciplinaVO> disciplinaVOS = disciplina.pesquisaPorNome("");
        disciplinaVOS.forEach(System.out::println);
        System.out.println(disciplinaVOS.size());
    }
}
