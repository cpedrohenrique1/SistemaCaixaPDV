package views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import dtos.FuncionarioSessaoDTO;
import controllers.FuncionarioController;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private final FuncionarioController controller = new FuncionarioController();

    public LoginView() {
        setTitle("PDV Supermercado - Autenticação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Usuário:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("  Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel(""));
        btnEntrar = new JButton("Entrar");
        add(btnEntrar);

        btnEntrar.addActionListener((ActionEvent e) -> executarLogin());
    }

    private void executarLogin() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        try {
            FuncionarioSessaoDTO sessao = controller.processarLogin(usuario, senha);
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + sessao.nomeCompleto());

            if (sessao.perfil().equalsIgnoreCase("GERENTE")) {
                new GerenteView().setVisible(true);
            } else {
                new CaixaView(sessao).setVisible(true);
            }
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
