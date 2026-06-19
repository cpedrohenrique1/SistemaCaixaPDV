package models;

import java.math.BigDecimal;
import java.util.UUID;

public record Produto(UUID pkProduto, String codigoProduto, String nomeProduto, BigDecimal precoProduto) {
}