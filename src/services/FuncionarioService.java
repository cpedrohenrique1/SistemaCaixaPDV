package services;

import java.util.List;

import dtos.FuncionarioSessaoDTO;
import dtos.LoginDTO;
import models.Funcionario;
import repositories.FuncionarioRepository;
import utils.Criptografia;

public class FuncionarioService {
    private final FuncionarioRepository repository = new FuncionarioRepository();

    public FuncionarioSessaoDTO realizarLogin(LoginDTO loginDTO) throws Exception {
        if (loginDTO.username().trim().isEmpty() || loginDTO.password().trim().isEmpty()) {
            throw new IllegalArgumentException("Usuário e senha são obrigatórios!");
        }

        Funcionario f = repository.buscarPorUsuario(loginDTO.username());

        String senhaHash = Criptografia.hashSenha(loginDTO.password());
        if (f == null || !f.senha().equals(senhaHash)) {
            throw new Exception("Usuário ou senha incorretos!");
        }
        return new FuncionarioSessaoDTO(f.pkFuncionario(), f.nomeCompleto(), f.perfil());
    }

    public void cadastrarFuncionario(Funcionario funcionario) throws Exception {
        validarFuncionario(funcionario, true);

        if (repository.buscarPorUsuario(funcionario.nomeUsuario()) != null) {
            throw new Exception("Já existe um usuário com este nome de acesso.");
        }

        repository.salvar(new Funcionario(
                funcionario.pkFuncionario(),
                funcionario.nomeCompleto(),
                funcionario.nomeUsuario(),
                Criptografia.hashSenha(funcionario.senha()),
                funcionario.perfil()));
    }

    public void atualizarFuncionario(Funcionario funcionario) throws Exception {
        validarFuncionario(funcionario, false);

        Funcionario existente = repository.buscarPorUsuario(funcionario.nomeUsuario());
        if (existente != null && !existente.pkFuncionario().equals(funcionario.pkFuncionario())) {
            throw new Exception("Já existe outro usuário com este nome de acesso.");
        }

        Funcionario atual = repository.buscarPorId(funcionario.pkFuncionario());
        String senhaParaSalvar = (funcionario.senha() == null || funcionario.senha().trim().isEmpty())
                ? (atual != null ? atual.senha() : null)
                : Criptografia.hashSenha(funcionario.senha());

        repository.atualizar(new Funcionario(
                funcionario.pkFuncionario(),
                funcionario.nomeCompleto(),
                funcionario.nomeUsuario(),
                senhaParaSalvar,
                funcionario.perfil()));
    }

    public List<Funcionario> listarFuncionarios() throws Exception {
        return repository.listarTodos();
    }

    public Funcionario buscarPorUsuario(String nomeUsuario) throws Exception {
        return repository.buscarPorUsuario(nomeUsuario);
    }

    private void validarFuncionario(Funcionario funcionario, boolean exigirSenha) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Dados do funcionário são obrigatórios.");
        }
        if (funcionario.nomeCompleto() == null || funcionario.nomeCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome completo é obrigatório.");
        }
        if (funcionario.nomeUsuario() == null || funcionario.nomeUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome de usuário é obrigatório.");
        }
        if (funcionario.perfil() == null || funcionario.perfil().trim().isEmpty()) {
            throw new IllegalArgumentException("O perfil é obrigatório.");
        }
        if (exigirSenha && (funcionario.senha() == null || funcionario.senha().trim().isEmpty())) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
    }
}
