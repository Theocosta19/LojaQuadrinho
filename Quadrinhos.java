package loja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Quadrinhos extends JFrame {
    private JTextField textFieldNome, textFieldEmail, textFieldTelefone;
    private JButton btnListar, btnInserir, btnAtualizar, btnExcluir, btnBuscar;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private Connection connection;
    private Inserir inserir;
    private Atualizar atualizar;
    private Excluir excluir;
    private Buscar buscar;

    public Quadrinhos() {
        setTitle("Loja de Quadrinhos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        connection = obterConexao();
        criarTabela();

        textFieldNome = new JTextField();
        textFieldEmail = new JTextField();
        textFieldTelefone = new JTextField();

        btnListar = new JButton("Listar");
        btnInserir = new JButton("Inserir");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnBuscar = new JButton("Buscar");

        configurarBotoes();

        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridLayout(4, 2));
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(textFieldNome);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(textFieldEmail);
        painelFormulario.add(new JLabel("Telefone:"));
        painelFormulario.add(textFieldTelefone);
        painelFormulario.add(btnInserir);
        painelFormulario.add(btnAtualizar);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());
        painelBotoes.add(btnListar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnBuscar);

        tabela = new JTable();
        tableModel = new DefaultTableModel();
        tabela.setModel(tableModel);
        tabela.setVisible(false); 

        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        setVisible(true);
    }

    private Connection obterConexao() {
        try {
            String url = "jdbc:mysql://localhost:3306/quadrinho";
            String usuario = "root";
            String senha = "loja1313";
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }

    private void criarTabela() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS clientes ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nome VARCHAR(255), "
                    + "email VARCHAR(255) UNIQUE, "
                    + "telefone VARCHAR(255) UNIQUE)";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao criar tabela 'clientes'.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void configurarBotoes() {
        inserir = new Inserir(connection);
        atualizar = new Atualizar(connection);
        excluir = new Excluir(connection);
        buscar = new Buscar(connection);

        btnInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText();
                String email = textFieldEmail.getText();
                String telefone = textFieldTelefone.getText();

                if (!nome.isEmpty() && !email.isEmpty() && !telefone.isEmpty()) {
                    if (inserirCliente(nome, email, telefone)) {
                        limparCampos();
                        listarClientesFromDB();
                    }
                } else {
                    JOptionPane.showMessageDialog(Quadrinhos.this, "Preencha todos os campos antes de inserir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText();
                String email = textFieldEmail.getText();
                String telefone = textFieldTelefone.getText();

                if (!nome.isEmpty() && !email.isEmpty() && !telefone.isEmpty()) {
                    atualizar.executarAtualizacao(nome, email, telefone);
                    limparCampos();
                    listarClientesFromDB();
                } else {
                    JOptionPane.showMessageDialog(Quadrinhos.this, "Preencha todos os campos antes de atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarClientesFromDB();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText();

                if (!nome.isEmpty()) {
                    excluir.executarExclusao(nome);
                    limparCampos();
                    listarClientesFromDB();
                } else {
                    JOptionPane.showMessageDialog(Quadrinhos.this, "Digite o nome do cliente a ser excluído.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText();

                if (!nome.isEmpty()) {
                    buscar.executarBusca(nome);
                } else {
                    JOptionPane.showMessageDialog(Quadrinhos.this, "Digite o nome do cliente a ser buscado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private boolean inserirCliente(String nome, String email, String telefone) {
        try {
            if (clienteExiste(email, telefone)) {
                JOptionPane.showMessageDialog(this, "Cliente com o mesmo email ou telefone já cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            inserir.executarInsercao(nome, email, telefone);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao inserir cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean clienteExiste(String email, String telefone) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clientes WHERE email = ? OR telefone = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, telefone);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private void listarClientesFromDB() {
        try {
            DefaultTableModel model = new Listar(connection).listarClientes();
            tabela.setModel(model);

            tabela.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao listar clientes.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        textFieldNome.setText("");
        textFieldEmail.setText("");
        textFieldTelefone.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Quadrinhos());
    }
}




































