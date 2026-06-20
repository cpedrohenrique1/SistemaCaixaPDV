package views;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controllers.ProdutoController;

public class CadastroProdutoView extends JDialog{
	private JTextField txtCodigo, txtNome, txtPreco;
    private JButton btnSalvar;
    private final ProdutoController controller = new ProdutoController();

    public CadastroProdutoView(Frame parent) {
        super(parent, "PDV Supermercado - Cadastrar Produto", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("  Código de Barras:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("  Descrição / Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("  Preço de Venda (R$):"));
        txtPreco = new JTextField();
        add(txtPreco);

        add(new JLabel(""));
        btnSalvar = new JButton("Salvar Produto");
        add(btnSalvar);

        btnSalvar.addActionListener((ActionEvent e) -> acaoSalvar());
    }

    private void acaoSalvar() {
        try {
            String codigo = txtCodigo.getText();
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));

            controller.registrarProduto(codigo, nome, preco);
            
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            this.dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um preço válido.", "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Cadastrar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
