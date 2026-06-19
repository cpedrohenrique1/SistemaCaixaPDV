package controllers;

import java.util.List;
import java.util.UUID;

import dtos.CarrinhoItemDTO;
import dtos.FecharVendaDTO;
import dtos.ItemVendaDetalheDTO;
import dtos.VendaDTO;
import services.VendaService;

public class VendaController {
    private final VendaService service = new VendaService();

    public void finalizarVenda(List<CarrinhoItemDTO> itens, String metodoPagamento, UUID idFuncionario) throws Exception {
        FecharVendaDTO dto = new FecharVendaDTO(itens, metodoPagamento, idFuncionario);
        service.processarFechamento(dto);
    }

    public List<VendaDTO> listarVendas() throws Exception {
        return service.listarVendas();
    }

    public List<ItemVendaDetalheDTO> listarItensPorVenda(UUID idVenda) throws Exception {
        return service.listarItensPorVenda(idVenda);
    }
}
