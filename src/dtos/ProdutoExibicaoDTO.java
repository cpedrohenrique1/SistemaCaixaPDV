package dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoExibicaoDTO(UUID pkProduto, String codigo, String nome, BigDecimal precoUnitario) {
}
