package services;

import dtos.CarrinhoItemDTO;
import dtos.FecharVendaDTO;
import models.ItemVenda;
import models.Venda;
import repositories.VendaRepository;

public class VendaService {
	private final VendaRepository repository = new VendaRepository();

    public void processarFechamento(FecharVendaDTO dto) throws Exception {
        if (dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("Não é possível fechar uma venda sem itens no carrinho!");
        }
        if (dto.getMetodoPagamento() == null || dto.getMetodoPagamento().isEmpty()) {
            throw new IllegalArgumentException("Selecione um método de pagamento válido.");
        }

        // Instancia a Entidade de Negócio Venda
        Venda venda = new Venda();
        venda.setMetodoPagamento(dto.getMetodoPagamento());
        venda.setFkFuncionario(dto.getIdFuncionarioLogado());

        double valorTotalAcumulado = 0.0;

        // Converte os DTOs temporários da View em entidades reais de ItemVenda
        for (CarrinhoItemDTO itemDTO : dto.getItens()) {
            ItemVenda item = new ItemVenda();
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            item.setFkProduto(itemDTO.getIdProduto());
            
            valorTotalAcumulado += itemDTO.getSubtotal();
            venda.adicionarItem(item);
        }

        venda.setTotal(valorTotalAcumulado);

        // Dispara a persistência atómica no repositório
        repository.salvarVendaCompleta(venda);
    }
}
