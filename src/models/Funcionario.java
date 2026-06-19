package models;

import java.util.UUID;

public record Funcionario(
    UUID pkFuncionario,
    String nomeCompleto,
    String nomeUsuario,
    String senha,
    String perfil
) {
}