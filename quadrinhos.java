package loja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class quadrinhos extends JFrame {
    private JTextField textFieldNome, textFieldCargo;
    private JButton btnListar, btnInserir, btnAtualizar, btnExcluir, btnBuscar;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private List<funcionario> funcionario;
    private Connection connection;

    public quadrinhos() {
        super("Loja Quadrinhos - CRUD de Funcionário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        textFieldNome = new JTextField(20);
        textFieldCargo = new JTextField(20);

        btnListar = new JButton("Listar");
        btnInserir = new JButton("Inserir");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnBuscar = new JButton("Buscar");

        tableModel = new DefaultTableModel();
        tabela = new JTable(tableModel);

        funcionario = new ArrayList<>();

        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new FlowLayout());
        panelForm.add(new JLabel("Nome:"));
        panelForm.add(textFieldNome);
        panelForm.add(new JLabel("Cargo:"));
        panelForm.add(textFieldCargo);
        panelForm.add(btnInserir);
        panelForm.add(btnAtualizar);
        panelForm.add(btnExcluir);
        panelForm.add(btnBuscar);

        add(panelForm, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarFuncionariosFromDB();
            }
        });

        btnInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirFuncionarioToDB();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarFuncionarioInDB();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirFuncionarioFromDB();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFuncionarioFromDB();
            }
        });

        conectarAoBancoDeDados();

        setVisible(true);
    }

    private void conectarAoBancoDeDados() {
        try {
            String url = "jdbc:mysql://localhost:3306/quadrinho";
            String usuario = "root";
            String senha = "loja1313";
            connection = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void listarFuncionariosFromDB() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM funcionarios");

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String cargo = resultSet.getString("cargo");

                tableModel.addRow(new Object[]{id, nome, cargo});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao listar funcionários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inserirFuncionarioToDB() {
        String nome = textFieldNome.getText();
        String cargo = textFieldCargo.getText();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO funcionarios (nome, cargo) VALUES (?, ?)");
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cargo);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Funcionário inserido com sucesso!");
            limparCampos();
            listarFuncionariosFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao inserir funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarFuncionarioInDB() {
        int idSelecionado = obterIDSelecionado();
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = textFieldNome.getText();
        String cargo = textFieldCargo.getText();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE funcionarios SET nome=?, cargo=? WHERE id=?");
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cargo);
            preparedStatement.setInt(3, idSelecionado);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            limparCampos();
            listarFuncionariosFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirFuncionarioFromDB() {
        int idSelecionado = obterIDSelecionado();
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este funcionário?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM funcionarios WHERE id=?");
                preparedStatement.setInt(1, idSelecionado);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
                limparCampos();
                listarFuncionariosFromDB();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarFuncionarioFromDB() {
        String nome = textFieldNome.getText();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM funcionarios WHERE nome LIKE ?");
            preparedStatement.setString(1, "%" + nome + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cargo = resultSet.getString("cargo");

                tableModel.addRow(new Object[]{id, nome, cargo});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        textFieldNome.setText("");
        textFieldCargo.setText("");
    }

    private int obterIDSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            return (int) tabela.getValueAt(linhaSelecionada, 0);
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new quadrinhos());
    }
}



