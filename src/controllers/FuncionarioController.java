package controllers;

import dtos.FuncionarioSessaoDTO;
import dtos.LoginDTO;
import services.FuncionarioService;

public class FuncionarioController {
    private final FuncionarioService service = new FuncionarioService();

    public FuncionarioSessaoDTO processarLogin(String usuario, String senha) throws Exception {
        LoginDTO dto = new LoginDTO(usuario, senha);
        return service.realizarLogin(dto);
    }
}
