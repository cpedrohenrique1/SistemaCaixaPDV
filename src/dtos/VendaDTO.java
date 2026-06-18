package dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record VendaDTO(UUID pkProduto, LocalDateTime vendaDate) {
}
