package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.ProdutoController;
import controllers.VendaController;
import dtos.CarrinhoItemDTO;
import dtos.FuncionarioSessaoDTO;
import dtos.ProdutoExibicaoDTO;

public class CaixaView extends JFrame {
    private final ProdutoController produtoController = new ProdutoController();
    private final VendaController vendaController = new VendaController();
    private final FuncionarioSessaoDTO funcionarioLogado;

    private JTextField txtCodigo;
    private JTextField txtQtd;
    private JLabel lblTotal;
    private JLabel lblOperador;
    private JTable tabelaCarrinho;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbPagamento;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnLimpar;
    private JButton btnFecharVenda;

    private final List<CarrinhoItemDTO> carrinho = new ArrayList<>();
    private BigDecimal totalGeral = BigDecimal.ZERO;

    public CaixaView(FuncionarioSessaoDTO funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;

        setTitle("PDV Supermercado - Caixa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o tema padrão se o tema do sistema não estiver disponível.
        }

        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(244, 247, 251));

        root.add(criarCabecalho(), BorderLayout.NORTH);
        root.add(criarConteudo(), BorderLayout.CENTER);
        root.add(criarRodape(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel criarCabecalho() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Caixa - Registro de Vendas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(new Color(18, 30, 52));

        lblOperador = new JLabel("Operador: " + nomeOperador());
        lblOperador.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblOperador.setForeground(new Color(88, 97, 113));

        JPanel texto = new JPanel(new BorderLayout(0, 6));
        texto.setOpaque(false);
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(lblOperador, BorderLayout.SOUTH);

        header.add(texto, BorderLayout.WEST);
        return header;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(0, 16));
        conteudo.setOpaque(false);
        conteudo.add(criarPainelEntrada(), BorderLayout.NORTH);
        conteudo.add(criarTabelaCarrinho(), BorderLayout.CENTER);
        return conteudo;
    }

    private JPanel criarPainelEntrada() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 236)),
                new EmptyBorder(12, 12, 12, 12)));

        painel.add(rotulo("Cód. Barras:"));
        txtCodigo = new JTextField(14);
        painel.add(txtCodigo);

        painel.add(rotulo("Qtd:"));
        txtQtd = new JTextField("1", 4);
        painel.add(txtQtd);

        btnAdicionar = criarBotao("Inserir Item", new Color(29, 78, 216));
        btnRemover = criarBotao("Remover Item", new Color(180, 83, 9));
        btnLimpar = criarBotao("Limpar Venda", new Color(107, 114, 128));

        painel.add(btnAdicionar);
        painel.add(btnRemover);
        painel.add(btnLimpar);

        btnAdicionar.addActionListener(e -> adicionarItemAoCarrinho());
        btnRemover.addActionListener(e -> removerItemSelecionado());
        btnLimpar.addActionListener(e -> limparVenda());

        return painel;
    }

    private JScrollPane criarTabelaCarrinho() {
        String[] colunas = {"Código", "Descrição", "Qtd", "Preço Unit.", "Subtotal"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaCarrinho = new JTable(modeloTabela);
        tabelaCarrinho.setRowHeight(28);
        tabelaCarrinho.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabelaCarrinho);
        scroll.setBorder(BorderFactory.createTitledBorder("Itens da Venda"));
        return scroll;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout(16, 16));
        rodape.setOpaque(false);

        lblTotal = new JLabel(formatarMoeda(totalGeral));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTotal.setForeground(new Color(17, 94, 89));
        lblTotal.setBorder(new EmptyBorder(6, 0, 0, 0));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acoes.setOpaque(false);

        cbPagamento = new JComboBox<>(new String[]{"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"});
        cbPagamento.setPreferredSize(new Dimension(180, 34));

        btnFecharVenda = criarBotao("Finalizar Venda", new Color(4, 120, 87));
        btnFecharVenda.setPreferredSize(new Dimension(170, 36));
        btnFecharVenda.addActionListener(e -> finalizarVenda());

        acoes.add(new JLabel("Pagamento:"));
        acoes.add(cbPagamento);
        acoes.add(btnFecharVenda);

        rodape.add(lblTotal, BorderLayout.WEST);
        rodape.add(acoes, BorderLayout.EAST);
        return rodape;
    }

    private JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorderPainted(false);
        botao.setRolloverEnabled(false);
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botao.setBackground(cor);
                botao.setForeground(Color.WHITE);
            }

            
            public void mouseExited(java.awt.event.MouseEvent e) {
                botao.setBackground(cor);
                botao.setForeground(Color.WHITE);
            }
        });
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return botao;
    }

    private void adicionarItemAoCarrinho() {
        try {
            String codigo = txtCodigo.getText().trim();
            int quantidade = Integer.parseInt(txtQtd.getText().trim());

            if (codigo.isEmpty()) {
                throw new IllegalArgumentException("Informe o código do produto.");
            }
            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }

            ProdutoExibicaoDTO produto = produtoController.pesquisarProdutoCarrinho(codigo);
            CarrinhoItemDTO item = new CarrinhoItemDTO(
                    produto.pkProduto(),
                    produto.codigo(),
                    produto.nome(),
                    quantidade,
                    produto.precoUnitario());

            carrinho.add(item);
            modeloTabela.addRow(new Object[]{
                    item.codigoProduto(),
                    item.nomeProduto(),
                    item.quantidade(),
                    formatarMoeda(item.precoUnitario()),
                    formatarMoeda(item.subtotal())
            });

            totalGeral = totalGeral.add(item.subtotal());
            atualizarTotal();

            txtCodigo.setText("");
            txtQtd.setText("1");
            txtCodigo.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade informada deve ser um número inteiro.", "Erro", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Item não localizado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItemSelecionado() {
        int linha = tabelaCarrinho.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CarrinhoItemDTO item = carrinho.remove(linha);
        modeloTabela.removeRow(linha);
        totalGeral = totalGeral.subtract(item.subtotal());
        atualizarTotal();
    }

    private void limparVenda() {
        carrinho.clear();
        modeloTabela.setRowCount(0);
        totalGeral = BigDecimal.ZERO;
        atualizarTotal();
        txtCodigo.setText("");
        txtQtd.setText("1");
        txtCodigo.requestFocus();
    }

    private void finalizarVenda() {
        try {
            if (funcionarioLogado == null || funcionarioLogado.pkFuncionario() == null) {
                throw new IllegalStateException("Não foi possível identificar o operador do caixa.");
            }

            String metodo = (String) cbPagamento.getSelectedItem();
            vendaController.finalizarVenda(carrinho, metodo, funcionarioLogado.pkFuncionario());

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!\nCupom fiscal emitido.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparVenda();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao fechar venda: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTotal() {
        lblTotal.setText("TOTAL: " + formatarMoeda(totalGeral));
    }

    private String formatarMoeda(BigDecimal valor) {
        return String.format("R$ %.2f", valor.doubleValue());
    }

    private String nomeOperador() {
        if (funcionarioLogado == null) {
            return "Indisponível";
        }
        return funcionarioLogado.nomeCompleto() + " (" + funcionarioLogado.perfil() + ")";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaixaView(null).setVisible(true));
    }
}
