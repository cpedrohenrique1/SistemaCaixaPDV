package dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemVendaDetalheDTO(
        UUID pkItemVenda,
        UUID pkProduto,
        String codigoProduto,
        String nomeProduto,
        int quantidade,
        BigDecimal precoUnitario) {

    public BigDecimal subtotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}
