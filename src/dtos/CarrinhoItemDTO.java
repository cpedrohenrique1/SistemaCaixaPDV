package dtos;

public class CarrinhoItemDTO {
	private int idProduto;
    private String codigo;
    private String nome;
    private int quantidade;
    private double precoUnitario;

    public CarrinhoItemDTO(int idProduto, String codigo, String nome, int quantidade, double precoUnitario) {
        this.idProduto = idProduto;
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getIdProduto() { return idProduto; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public double getSubtotal() { return quantidade * precoUnitario; }
}
