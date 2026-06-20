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

import controllers.FuncionarioController;
import models.Funcionario;

public class GerenciarUsuariosView extends JFrame {
    private final FuncionarioController controller = new FuncionarioController();

    private JTextField txtNomeCompleto;
    private JTextField txtNomeUsuario;
    private JTextField txtSenha;
    private JComboBox<String> cbPerfil;
    private JTextField txtBuscaUsuario;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnSalvar;
    private JButton btnAtualizar;
    private JButton btnNovo;
    private JButton btnRecarregar;
    private JButton btnPesquisar;

    private UUID idSelecionado;

    public GerenciarUsuariosView() {
        setTitle("PDV Supermercado - Gerenciamento de Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 760));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        carregarUsuarios();
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

        JLabel titulo = new JLabel("Cadastro e Gerenciamento de Usuários");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(18, 30, 52));

        JLabel subtitulo = new JLabel("Crie, consulte e atualize usuários do sistema de forma independente");
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

        form.add(rotulo("Nome completo:"));
        txtNomeCompleto = new JTextField(18);
        form.add(txtNomeCompleto);

        form.add(rotulo("Usuário:"));
        txtNomeUsuario = new JTextField(12);
        form.add(txtNomeUsuario);

        form.add(rotulo("Senha:"));
        txtSenha = new JTextField(12);
        form.add(txtSenha);

        form.add(rotulo("Perfil:"));
        cbPerfil = new JComboBox<>(new String[]{"GERENTE", "CAIXA"});
        cbPerfil.setPreferredSize(new Dimension(120, 30));
        form.add(cbPerfil);

        btnSalvar = criarBotao("Salvar", new Color(29, 78, 216));
        btnAtualizar = criarBotao("Atualizar", new Color(4, 120, 87));
        btnNovo = criarBotao("Novo", new Color(107, 114, 128));

        btnSalvar.addActionListener(e -> salvarUsuario());
        btnAtualizar.addActionListener(e -> atualizarUsuario());
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
        busca.add(rotulo("Buscar usuário:"));
        txtBuscaUsuario = new JTextField(15);
        busca.add(txtBuscaUsuario);
        btnPesquisar = criarBotao("Pesquisar", new Color(180, 83, 9));
        btnRecarregar = criarBotao("Recarregar", new Color(88, 97, 113));
        busca.add(btnPesquisar);
        busca.add(btnRecarregar);

        btnPesquisar.addActionListener(e -> pesquisarUsuario());
        btnRecarregar.addActionListener(e -> carregarUsuarios());

        area.add(busca, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Usuário", "Perfil"};
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
        scroll.setBorder(BorderFactory.createTitledBorder("Usuários cadastrados"));
        area.add(scroll, BorderLayout.CENTER);
        return area;
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel observacao = new JLabel("Use salvar para novo cadastro e atualizar após selecionar um registro na tabela.");
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

    private void salvarUsuario() {
        try {
            Funcionario funcionario = new Funcionario(
                    null,
                    txtNomeCompleto.getText().trim(),
                    txtNomeUsuario.getText().trim(),
                    txtSenha.getText().trim(),
                    cbPerfil.getSelectedItem().toString());

            controller.cadastrarFuncionario(funcionario);
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            limparFormulario();
            carregarUsuarios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarUsuario() {
        try {
            if (idSelecionado == null) {
                throw new IllegalArgumentException("Selecione um usuário na tabela para atualizar.");
            }

            Funcionario funcionario = new Funcionario(
                    idSelecionado,
                    txtNomeCompleto.getText().trim(),
                    txtNomeUsuario.getText().trim(),
                    txtSenha.getText().trim(),
                    cbPerfil.getSelectedItem().toString());

            controller.atualizarFuncionario(funcionario);
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            carregarUsuarios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarUsuario() {
        String usuario = txtBuscaUsuario.getText().trim();
        if (usuario.isEmpty()) {
            carregarUsuarios();
            return;
        }

        try {
            Funcionario funcionario = controller.buscarPorUsuario(usuario);
            modelo.setRowCount(0);
            if (funcionario != null) {
                modelo.addRow(new Object[]{
                        funcionario.pkFuncionario(),
                        funcionario.nomeCompleto(),
                        funcionario.nomeUsuario(),
                        funcionario.perfil()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro na busca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarUsuarios() {
        try {
            List<Funcionario> funcionarios = controller.listarFuncionarios();
            modelo.setRowCount(0);
            for (Funcionario funcionario : funcionarios) {
                modelo.addRow(new Object[]{
                        funcionario.pkFuncionario(),
                        funcionario.nomeCompleto(),
                        funcionario.nomeUsuario(),
                        funcionario.perfil()
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
        txtNomeCompleto.setText(modelo.getValueAt(linha, 1).toString());
        txtNomeUsuario.setText(modelo.getValueAt(linha, 2).toString());
        cbPerfil.setSelectedItem(modelo.getValueAt(linha, 3).toString());

        txtSenha.setText("");
    }

    private void limparFormulario() {
        idSelecionado = null;
        txtNomeCompleto.setText("");
        txtNomeUsuario.setText("");
        txtSenha.setText("");
        txtBuscaUsuario.setText("");
        cbPerfil.setSelectedIndex(0);
        tabela.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciarUsuariosView().setVisible(true));
    }
}
