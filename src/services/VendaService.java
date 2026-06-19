package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dtos.CarrinhoItemDTO;
import dtos.FecharVendaDTO;
import dtos.ItemVendaDetalheDTO;
import dtos.VendaDTO;
import models.ItemVenda;
import models.Venda;
import repositories.VendaRepository;

public class VendaService {
    private final VendaRepository repository = new VendaRepository();

    public void processarFechamento(FecharVendaDTO dto) throws Exception {
        validarFechamento(dto);

        BigDecimal totalAcumulado = BigDecimal.ZERO;
        List<ItemVenda> itensVenda = new ArrayList<>();

        for (CarrinhoItemDTO itemDTO : dto.itens()) {
            validarItemCarrinho(itemDTO);

            ItemVenda item = new ItemVenda(
                    null,
                    itemDTO.quantidade(),
                    itemDTO.precoUnitario(),
                    null,
                    itemDTO.pkProduto(),
                    itemDTO.nomeProduto());

            totalAcumulado = totalAcumulado.add(itemDTO.subtotal());
            itensVenda.add(item);
        }

        Venda venda = new Venda(null, null, totalAcumulado, dto.metodoPagamento().trim(), dto.idFuncionarioLogado(), itensVenda);
        repository.salvarVendaCompleta(venda);
    }

    public List<VendaDTO> listarVendas() throws Exception {
        return repository.listarVendas();
    }

    public List<ItemVendaDetalheDTO> listarItensPorVenda(UUID idVenda) throws Exception {
        if (idVenda == null) {
            throw new IllegalArgumentException("Selecione uma venda para visualizar os itens.");
        }

        return repository.listarItensPorVenda(idVenda);
    }

    private void validarFechamento(FecharVendaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da venda são obrigatórios.");
        }
        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new IllegalArgumentException("Não é possível fechar uma venda sem itens no carrinho!");
        }
        if (dto.metodoPagamento() == null || dto.metodoPagamento().trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione um método de pagamento válido.");
        }
        if (dto.idFuncionarioLogado() == null) {
            throw new IllegalArgumentException("Não foi possível identificar o operador do caixa.");
        }
    }

    private void validarItemCarrinho(CarrinhoItemDTO itemDTO) {
        if (itemDTO == null || itemDTO.pkProduto() == null) {
            throw new IllegalArgumentException("Todos os itens da venda devem possuir produto válido.");
        }
        if (itemDTO.quantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade de cada item deve ser maior que zero.");
        }
        if (itemDTO.precoUnitario() == null || itemDTO.precoUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço unitário de cada item deve ser maior que zero.");
        }
    }
}
