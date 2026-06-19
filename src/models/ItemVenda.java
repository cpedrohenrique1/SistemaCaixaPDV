package models;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemVenda(UUID pkItemVenda, int quantidade, BigDecimal precoUnitario, UUID fkVenda, UUID fkProduto, String nomeProduto) {
    public BigDecimal subtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
