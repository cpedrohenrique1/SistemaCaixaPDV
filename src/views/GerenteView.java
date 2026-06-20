package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class GerenteView extends JFrame {

    private JButton btnNovoUsuario;
    private JButton btnCadastrarProduto;
    private JButton btnHistoricoVendas;

    public GerenteView() {
        setTitle("PDV Supermercado - Painel do Gerente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(0, 24));
        root.setBorder(new EmptyBorder(24, 24, 24, 24));
        root.setBackground(new Color(246, 248, 252));

        root.add(criarCabecalho(), BorderLayout.NORTH);
        root.add(criarDashboard(), BorderLayout.CENTER);
        root.add(criarRodape(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel criarCabecalho() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Painel Administrativo do Gerente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(new Color(25, 38, 60));

        JLabel subtitulo = new JLabel("Acesse rapidamente os cadastros e as rotinas administrativas do PDV");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(88, 97, 113));

        JPanel texto = new JPanel(new BorderLayout(0, 6));
        texto.setOpaque(false);
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(subtitulo, BorderLayout.SOUTH);

        header.add(texto, BorderLayout.WEST);
        return header;
    }

    private JPanel criarDashboard() {
        JPanel dashboard = new JPanel(new GridLayout(2, 2, 18, 18));
        dashboard.setOpaque(false);

        btnNovoUsuario = criarBotaoAcao("Gerenciar usuários", "Abre a tela de cadastro de usuários", new Color(30, 64, 175));
        btnCadastrarProduto = criarBotaoAcao("Gerenciar produtos", "Abre a tela de cadastro e edição de produtos", new Color(6, 95, 70));
        btnHistoricoVendas = criarBotaoAcao("Registro de vendas", "Mostra vendas finalizadas e seus itens", new Color(75, 85, 99));

        btnNovoUsuario.addActionListener(e -> new GerenciarUsuariosView().setVisible(true));
        btnCadastrarProduto.addActionListener(e -> new GerenciarProdutosView().setVisible(true));
        btnHistoricoVendas.addActionListener(e -> new HistoricoVendasView().setVisible(true));

        dashboard.add(btnNovoUsuario);
        dashboard.add(btnCadastrarProduto);
        dashboard.add(btnHistoricoVendas);

        return dashboard;
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel observacao = new JLabel("As telas abertas por esta View são independentes e não modais.");
        observacao.setFont(new Font("SansSerif", Font.ITALIC, 13));
        observacao.setForeground(new Color(99, 110, 126));

        footer.add(observacao, BorderLayout.WEST);
        return footer;
    }

    private JButton criarBotaoAcao(String titulo, String descricao, Color cor) {
        String html = "<html><div style='text-align:center; padding:8px;'>"
                + "<span style='font-size:16px; color:#ffffff;'><b>" + titulo + "</b></span><br><br>"
                + "<span style='font-size:11px; color:#e5e7eb;'>" + descricao + "</span>"
                + "</div></html>";

        JButton botao = new JButton(html);
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        botao.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        botao.setHorizontalAlignment(SwingConstants.CENTER);
        botao.setVerticalAlignment(SwingConstants.CENTER);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor.darker()),
                new EmptyBorder(18, 18, 18, 18)));
        botao.setPreferredSize(new Dimension(280, 150));
        return botao;
    }
}
