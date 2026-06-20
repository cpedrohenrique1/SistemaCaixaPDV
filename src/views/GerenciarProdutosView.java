package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import models.Produto;

public class GerenciarProdutosView extends JFrame {
    private final ProdutoController controller = new ProdutoController();

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtBuscaCodigo;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnSalvar;
    private JButton btnAtualizar;
    private JButton btnNovo;
    private JButton btnRecarregar;
    private JButton btnPesquisar;

    private UUID idSelecionado;

    public GerenciarProdutosView() {
        setTitle("PDV Supermercado - Gerenciamento de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 760));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        carregarProdutos();
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

        JLabel titulo = new JLabel("Cadastro e Gerenciamento de Produtos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(18, 30, 52));

        JLabel subtitulo = new JLabel("Inclua, consulte e atualize produtos do sistema");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(88, 97, 113));

        JPanel texto = new JPanel(new BorderLayout(0, 6));
        texto.setOpaque(false);
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(subtitulo, BorderLayout.SOUTH);

        header.add(texto, BorderLayout.WEST);
        return header;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(16, 16));
        conteudo.setOpaque(false);
        conteudo.add(criarFormulario(), BorderLayout.NORTH);
        conteudo.add(criarTabela(), BorderLayout.CENTER);
        return conteudo;
    }

    private JPanel criarFormulario() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 236)),
                new EmptyBorder(12, 12, 12, 12)));

        form.add(rotulo("Código:"));
        txtCodigo = new JTextField(14);
        form.add(txtCodigo);

        form.add(rotulo("Nome:"));
        txtNome = new JTextField(18);
        form.add(txtNome);

        form.add(rotulo("Preço:"));
        txtPreco = new JTextField(10);
        form.add(txtPreco);

        btnSalvar = criarBotao("Salvar", new Color(29, 78, 216));
        btnAtualizar = criarBotao("Atualizar", new Color(4, 120, 87));
        btnNovo = criarBotao("Novo", new Color(107, 114, 128));

        btnSalvar.addActionListener(e -> salvarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnNovo.addActionListener(e -> limparFormulario());

        form.add(btnSalvar);
        form.add(btnAtualizar);
        form.add(btnNovo);

        return form;
    }

    private JPanel criarTabela() {
        JPanel area = new JPanel(new BorderLayout(0, 10));
        area.setOpaque(false);

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        busca.setOpaque(false);
        busca.add(rotulo("Buscar código:"));
        txtBuscaCodigo = new JTextField(15);
        busca.add(txtBuscaCodigo);
        btnPesquisar = criarBotao("Pesquisar", new Color(180, 83, 9));
        btnRecarregar = criarBotao("Recarregar", new Color(88, 97, 113));
        busca.add(btnPesquisar);
        busca.add(btnRecarregar);

        btnPesquisar.addActionListener(e -> pesquisarProduto());
        btnRecarregar.addActionListener(e -> carregarProdutos());

        area.add(busca, BorderLayout.NORTH);

        String[] colunas = {"ID", "Código", "Nome", "Preço"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherFormularioDaLinhaSelecionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Produtos cadastrados"));
        area.add(scroll, BorderLayout.CENTER);
        return area;
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel observacao = new JLabel("Selecione um produto na tabela para editar suas informações.");
        observacao.setFont(new Font("SansSerif", Font.ITALIC, 13));
        observacao.setForeground(new Color(99, 110, 126));

        footer.add(observacao, BorderLayout.WEST);
        return footer;
    }

    private JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
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

    private void salvarProduto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));

            controller.registrarProduto(codigo, nome, preco);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparFormulario();
            carregarProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um preço válido.", "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            if (idSelecionado == null) {
                throw new IllegalArgumentException("Selecione um produto na tabela para atualizar.");
            }

            String codigo = txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));

            controller.atualizarProduto(idSelecionado, codigo, nome, preco);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            carregarProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um preço válido.", "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarProduto() {
        String codigo = txtBuscaCodigo.getText().trim();
        if (codigo.isEmpty()) {
            carregarProdutos();
            return;
        }

        try {
            Produto produto = controller.pesquisarProdutoPorCodigo(codigo);
            modelo.setRowCount(0);
            if (produto != null) {
                modelo.addRow(new Object[]{
                        produto.pkProduto(),
                        produto.codigoProduto(),
                        produto.nomeProduto(),
                        produto.precoProduto()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro na busca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = controller.listarCatalogoCompleto();
            modelo.setRowCount(0);
            for (Produto produto : produtos) {
                modelo.addRow(new Object[]{
                        produto.pkProduto(),
                        produto.codigoProduto(),
                        produto.nomeProduto(),
                        produto.precoProduto()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormularioDaLinhaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }

        Object id = modelo.getValueAt(linha, 0);
        idSelecionado = UUID.fromString(id.toString());
        txtCodigo.setText(modelo.getValueAt(linha, 1).toString());
        txtNome.setText(modelo.getValueAt(linha, 2).toString());
        txtPreco.setText(modelo.getValueAt(linha, 3).toString().replace("R$ ", ""));
    }

    private void limparFormulario() {
        idSelecionado = null;
        txtCodigo.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtBuscaCodigo.setText("");
        tabela.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciarProdutosView().setVisible(true));
    }
}
