package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controllers.ProdutoController;
import controllers.VendaController;
import dtos.CarrinhoItemDTO;
import dtos.ProdutoExibicaoDTO;

public class CaixaView extends JFrame{
	private JTextField txtCodigo, txtQtd;
    private JLabel lblTotal;
    private JTable tabelaCarrinho;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbPagamento;
    private JButton btnAdicionar, btnFecharVenda;

    private final ProdutoController produtoController = new ProdutoController();
    private final VendaController vendaController = new VendaController();
    
    // Lista local em memória para armazenar os múltiplos itens comprados antes do fechamento
    private final List<CarrinhoItemDTO> carrinho = new ArrayList<>();
    private double valorTotalGeral = 0.0;
    private int idFuncionarioLogado = 1; // Substituir pelo ID vindo da sessão real do Login

    public CaixaView() {
        setTitle("PDV Supermercado - Ponto de Venda");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Painel Superior: Entrada de dados do Produto
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelSuperior.add(new JLabel("Cód. Barras:"));
        txtCodigo = new JTextField(12);
        painelSuperior.add(txtCodigo);

        painelSuperior.add(new JLabel("Qtd:"));
        txtQtd = new JTextField("1", 4);
        painelSuperior.add(txtQtd);

        btnAdicionar = new JButton("Inserir Item");
        painelSuperior.add(btnAdicionar);
        add(painelSuperior, BorderLayout.NORTH);

        // Painel Central: Tabela contendo a listagem do carrinho
        String[] colunas = {"Código", "Descrição", "Qtd", "Preço Unit.", "Subtotal"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaCarrinho = new JTable(modeloTabela);
        add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);

        // Painel Inferior: Totais e Finalização da Venda
        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        
        lblTotal = new JLabel("TOTAL: R$ 0,00  ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setForeground(Color.BLUE);
        painelInferior.add(lblTotal, BorderLayout.NORTH);

        painelAcoes.add(new JLabel("Pagamento:"));
        cbPagamento = new JComboBox<>(new String[]{"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"});
        painelAcoes.add(cbPagamento);

        btnFecharVenda = new JButton("Finalizar Cupom");
        btnFecharVenda.setBackground(new Color(34, 139, 34));
        btnFecharVenda.setForeground(Color.WHITE);
        painelAcoes.add(btnFecharVenda);
        painelInferior.add(painelAcoes, BorderLayout.SOUTH);
        
        add(painelInferior, BorderLayout.SOUTH);

        // Eventos dos botões
        btnAdicionar.addActionListener(e -> adicionarItemAoCarrinho());
        btnFecharVenda.addActionListener(e -> finalizarCompraTotal());
    }

    private void adicionarItemAoCarrinho() {
        try {
            String codigo = txtCodigo.getText().trim();
            int quantidade = Integer.parseInt(txtQtd.getText().trim());

            // 1. Busca os dados do produto no banco pelo controller de Produto
            ProdutoExibicaoDTO prod = produtoController.pesquisarProdutoCarrinho(codigo);

            // 2. Instancia o DTO do carrinho e coloca na lista em memória temporária
            CarrinhoItemDTO novoItem = new CarrinhoItemDTO(
                prod.pkProduto(), prod.codigo(), prod.nome(), quantidade, prod.precoUnitario()
            );
            carrinho.add(novoItem);

            // 3. Atualiza os componentes Visuais da JTable
            modeloTabela.addRow(new Object[]{
                novoItem.getCodigo(), novoItem.getNome(), novoItem.getQuantidade(), 
                String.format("R$ %.2f", novoItem.getPrecoUnitario()), 
                String.format("R$ %.2f", novoItem.getSubtotal())
            });

            // Reajusta o somatório total da tela
            valorTotalGeral += novoItem.getSubtotal();
            lblTotal.setText(String.format("TOTAL: R$ %.2f", valorTotalGeral));

            // Limpa os campos para o próximo item
            txtCodigo.setText("");
            txtQtd.setText("1");
            txtCodigo.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade informada deve ser um número inteiro.", "Erro", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Item não localizado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarCompraTotal() {
        try {
            String metodo = cbPagamento.getSelectedItem().toString();

            // Envia a lista acumulada de múltiplos itens para o controller de vendas processar
            vendaController.finalizarVenda(carrinho, metodo, idFuncionarioLogado);

            JOptionPane.showMessageDialog(this, "Venda registada com sucesso!\nCupom Fiscal Emitido.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpa o estado da tela para o próximo cliente
            carrinho.clear();
            modeloTabela.setRowCount(0);
            valorTotalGeral = 0.0;
            lblTotal.setText("TOTAL: R$ 0,00");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao fechar venda: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaixaView().setVisible(true));
    }
}
