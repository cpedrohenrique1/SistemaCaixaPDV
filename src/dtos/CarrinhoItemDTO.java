package dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CarrinhoItemDTO(UUID pkProduto, String codigoProduto, String nomeProduto, int quantidade, BigDecimal precoUnitario) {
    public BigDecimal subtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
