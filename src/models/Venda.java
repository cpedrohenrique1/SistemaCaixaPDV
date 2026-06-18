package models;

import java.util.ArrayList;
import java.util.List;

public class Venda {
	private int pkVenda;
    private String dataVenda;
    private double total;
    private String metodoPagamento;
    private int fkFuncionario;
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {}

    public Venda(int pkVenda, String dataVenda, double total, String metodoPagamento, int fkFuncionario) {
        this.pkVenda = pkVenda;
        this.dataVenda = dataVenda;
        this.total = total;
        this.metodoPagamento = metodoPagamento;
        this.fkFuncionario = fkFuncionario;
    }

    // Getters e Setters
    public int getPkVenda() { return pkVenda; }
    public void setPkVenda(int pkVenda) { this.pkVenda = pkVenda; }

    public String getDataVenda() { return dataVenda; }
    public void setDataVenda(String dataVenda) { this.dataVenda = dataVenda; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public int getFkFuncionario() { return fkFuncionario; }
    public void setFkFuncionario(int fkFuncionario) { this.fkFuncionario = fkFuncionario; }

    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
    
    public void adicionarItem(ItemVenda item) { this.itens.add(item); }
}
