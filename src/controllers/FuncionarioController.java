package controllers;

import java.util.List;

import dtos.FuncionarioSessaoDTO;
import dtos.LoginDTO;
import models.Funcionario;
import services.FuncionarioService;

public class FuncionarioController {
    private final FuncionarioService service = new FuncionarioService();

    public FuncionarioSessaoDTO processarLogin(String usuario, String senha) throws Exception {
        LoginDTO dto = new LoginDTO(usuario, senha);
        return service.realizarLogin(dto);
    }

    public void cadastrarFuncionario(Funcionario funcionario) throws Exception {
        service.cadastrarFuncionario(funcionario);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws Exception {
        service.atualizarFuncionario(funcionario);
    }

    public List<Funcionario> listarFuncionarios() throws Exception {
        return service.listarFuncionarios();
    }

    public Funcionario buscarPorUsuario(String nomeUsuario) throws Exception {
        return service.buscarPorUsuario(nomeUsuario);
    }
}
