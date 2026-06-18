package controllers;

import java.util.List;

import dtos.CarrinhoItemDTO;
import dtos.FecharVendaDTO;
import services.VendaService;

public class VendaController {
	private final VendaService service = new VendaService();

    public void finalizarVenda(List<CarrinhoItemDTO> itens, String metodoPagamento, int idFuncionario) throws Exception {
        FecharVendaDTO dto = new FecharVendaDTO(itens, metodoPagamento, idFuncionario);
        service.processarFechamento(dto);
    }
}
