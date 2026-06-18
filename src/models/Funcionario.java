package models;

public record Funcionario(
    int pkFuncionario,
    String nomeCompleto,
    String nomeUsuario,
    String senha,
    String perfil
) {
}