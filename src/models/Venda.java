package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Venda (UUID pkVenda, String dataVenda, BigDecimal totalVenda, String metodoPagamento, UUID fkFuncionario, List<ItemVenda> itens){
	public Venda {
		itens = (itens == null) ? new ArrayList<>() : new ArrayList<>(itens);
	}
	
    public void adicionarItem(ItemVenda item) { 
    	this.itens.add(item); 
	}
}
