package dtos;

import java.util.UUID;

public record FuncionarioSessaoDTO(UUID pkFuncionario, String nomeCompleto, String perfil) {
}