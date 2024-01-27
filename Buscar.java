package loja;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Buscar {
    private Connection connection;

    public Buscar(Connection connection) {
        this.connection = connection;
    }

    public void executarBusca(String nome) {
        try {
            String query = "SELECT * FROM clientes WHERE nome = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String telefone = resultSet.getString("telefone");

                    JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nEmail: " + email + "\nTelefone: " + telefone, "Informações", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





