package services;

import dtos.FuncionarioSessaoDTO;
import dtos.LoginDTO;
import models.Funcionario;
import repositories.FuncionarioRepository;
import utils.Criptografia;

public class FuncionarioService {
    private final FuncionarioRepository repository = new FuncionarioRepository();

    public FuncionarioSessaoDTO realizarLogin(LoginDTO loginDTO) throws Exception {
        // Validação básica de campos vazios
        if (loginDTO.username().trim().isEmpty() || loginDTO.password().trim().isEmpty()) {
            throw new IllegalArgumentException("Usuário e senha são obrigatórios!");
        }

        Funcionario f = repository.buscarPorUsuario(loginDTO.username());

        // Verifica se o usuário existe e se o hash da senha confere
        String senhaHash = Criptografia.hashSenha(loginDTO.password());
        if (f == null || !f.senha().equals(senhaHash)) {
            throw new Exception("Usuário ou senha incorretos!");
        }

        // Retorna apenas os dados necessários para a sessão da interface gráfica
        return new FuncionarioSessaoDTO(f.pkFuncionario(), f.nomeCompleto(), f.perfil());
    }
}
