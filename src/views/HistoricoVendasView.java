package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.VendaController;
import dtos.ItemVendaDetalheDTO;
import dtos.VendaDTO;

public class HistoricoVendasView extends JFrame {
    private final VendaController controller = new VendaController();

    private JTable tabelaVendas;
    private JTable tabelaItens;
    private DefaultTableModel modeloVendas;
    private DefaultTableModel modeloItens;
    private JLabel lblTotalVenda;
    private JLabel lblVendaSelecionada;
    private JButton btnRecarregar;

    public HistoricoVendasView() {
        setTitle("PDV Supermercado - Histórico de Vendas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 760));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        initComponents();
        carregarVendas();
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

        JLabel titulo = new JLabel("Registro de Vendas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(18, 30, 52));

        JLabel subtitulo = new JLabel("Selecione uma venda para visualizar os itens vendidos e seus valores");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(88, 97, 113));

        JPanel texto = new JPanel(new BorderLayout(0, 6));
        texto.setOpaque(false);
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(subtitulo, BorderLayout.SOUTH);

        btnRecarregar = criarBotao("Recarregar", new Color(29, 78, 216));
        btnRecarregar.addActionListener(e -> carregarVendas());

        header.add(texto, BorderLayout.WEST);
        header.add(btnRecarregar, BorderLayout.EAST);
        return header;
    }

    private JSplitPane criarConteudo() {
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, criarPainelVendas(), criarPainelItens());
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private JPanel criarPainelVendas() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setOpaque(false);

        String[] colunas = {"ID", "Data", "Operador", "Pagamento", "Total"};
        modeloVendas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaVendas = new JTable(modeloVendas);
        tabelaVendas.setRowHeight(28);
        tabelaVendas.getTableHeader().setReorderingAllowed(false);
        tabelaVendas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaVendas.getSelectedRow() >= 0) {
                carregarItensDaVendaSelecionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaVendas);
        scroll.setBorder(BorderFactory.createTitledBorder("Vendas registradas"));
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelItens() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setOpaque(false);

        lblVendaSelecionada = new JLabel("Nenhuma venda selecionada");
        lblVendaSelecionada.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblVendaSelecionada.setForeground(new Color(25, 38, 60));

        String[] colunas = {"Código", "Produto", "Qtd", "Preço Unit.", "Subtotal"};
        modeloItens = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaItens = new JTable(modeloItens);
        tabelaItens.setRowHeight(28);
        tabelaItens.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setBorder(BorderFactory.createTitledBorder("Itens vendidos"));

        painel.add(lblVendaSelecionada, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setOpaque(false);

        lblTotalVenda = new JLabel("TOTAL PAGO: R$ 0,00");
        lblTotalVenda.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotalVenda.setForeground(new Color(17, 94, 89));

        footer.add(lblTotalVenda);
        return footer;
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

    private void carregarVendas() {
        try {
            List<VendaDTO> vendas = controller.listarVendas();
            modeloVendas.setRowCount(0);
            modeloItens.setRowCount(0);
            lblVendaSelecionada.setText("Nenhuma venda selecionada");
            lblTotalVenda.setText("TOTAL PAGO: R$ 0,00");

            for (VendaDTO venda : vendas) {
                modeloVendas.addRow(new Object[]{
                        venda.pkVenda(),
                        venda.dataVenda(),
                        venda.nomeFuncionario(),
                        venda.metodoPagamento(),
                        formatarMoeda(venda.totalVenda())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar vendas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarItensDaVendaSelecionada() {
        int linha = tabelaVendas.getSelectedRow();
        if (linha < 0) {
            return;
        }

        UUID idVenda = UUID.fromString(modeloVendas.getValueAt(linha, 0).toString());
        String dataVenda = modeloVendas.getValueAt(linha, 1).toString();
        String totalVenda = modeloVendas.getValueAt(linha, 4).toString();

        try {
            List<ItemVendaDetalheDTO> itens = controller.listarItensPorVenda(idVenda);
            modeloItens.setRowCount(0);

            for (ItemVendaDetalheDTO item : itens) {
                modeloItens.addRow(new Object[]{
                        item.codigoProduto(),
                        item.nomeProduto(),
                        item.quantidade(),
                        formatarMoeda(item.precoUnitario()),
                        formatarMoeda(item.subtotal())
                });
            }

            lblVendaSelecionada.setText("Venda " + idVenda + " - " + dataVenda);
            lblTotalVenda.setText("TOTAL PAGO: " + totalVenda);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar itens", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        return String.format("R$ %.2f", valor.doubleValue());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HistoricoVendasView().setVisible(true));
    }
}
