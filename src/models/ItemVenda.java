package models;

public class ItemVenda {
	private int pkItemVenda;
    private int quantidade;
    private double precoUnitario;
    private int fkVenda;
    private int fkProduto;
    // Atributo auxiliar apenas para listagem na View (não vai para a tabela ItemVenda)
    private String nomeProduto; 

    public ItemVenda() {}

    public ItemVenda(int quantidade, double precoUnitario, int fkProduto, String nomeProduto) {
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.fkProduto = fkProduto;
        this.nomeProduto = nomeProduto;
    }

    // Getters e Setters
    public int getPkItemVenda() { return pkItemVenda; }
    public void setPkItemVenda(int pkItemVenda) { this.pkItemVenda = pkItemVenda; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public int getFkVenda() { return fkVenda; }
    public void setFkVenda(int fkVenda) { this.fkVenda = fkVenda; }

    public int getFkProduto() { return fkProduto; }
    public void setFkProduto(int fkProduto) { this.fkProduto = fkProduto; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    
    public double getSubtotal() { return this.quantidade * this.precoUnitario; }
}
