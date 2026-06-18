package dtos;

import java.util.List;

public class FecharVendaDTO {
	private List<CarrinhoItemDTO> itens;
    private String metodoPagamento;
    private int idFuncionarioLogado;

    public FecharVendaDTO(List<CarrinhoItemDTO> itens, String metodoPagamento, int idFuncionarioLogado) {
        this.itens = itens;
        this.metodoPagamento = metodoPagamento;
        this.idFuncionarioLogado = idFuncionarioLogado;
    }

    public List<CarrinhoItemDTO> getItens() { return itens; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public int getIdFuncionarioLogado() { return idFuncionarioLogado; }
}
