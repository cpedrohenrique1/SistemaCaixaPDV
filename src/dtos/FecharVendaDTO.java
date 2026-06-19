package dtos;

import java.util.List;
import java.util.UUID;

public record FecharVendaDTO(List<CarrinhoItemDTO> itens, String metodoPagamento, UUID idFuncionarioLogado){
}
