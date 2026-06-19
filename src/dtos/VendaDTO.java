package dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record VendaDTO(UUID pkVenda, String dataVenda, BigDecimal totalVenda, String metodoPagamento, String nomeFuncionario) {
}
