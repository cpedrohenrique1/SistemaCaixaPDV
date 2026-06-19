package repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import daos.FuncionarioDAO;
import models.Funcionario;

public class FuncionarioRepository {
    private final FuncionarioDAO dao = new FuncionarioDAO();

    public Funcionario buscarPorUsuario(String nomeUsuario) throws SQLException {
        return dao.buscarPorUsuario(nomeUsuario);
    }

    public Funcionario buscarPorId(UUID idFuncionario) throws SQLException {
        return dao.buscarPorId(idFuncionario);
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void salvar(Funcionario funcionario) throws SQLException {
        dao.salvar(funcionario);
    }

    public void atualizar(Funcionario funcionario) throws SQLException {
        if (funcionario.pkFuncionario() == null) {
            throw new IllegalArgumentException("O identificador do funcionário é obrigatório para atualização.");
        }

        dao.atualizar(funcionario);
    }
}
