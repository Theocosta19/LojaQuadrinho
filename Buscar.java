package loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Buscar {
    private Connection connection;

    public Buscar(Connection connection) {
        this.connection = connection;
    }

    public void executarBusca(String nome) {
        try {
            String query = "SELECT * FROM loja.quadrinhos WHERE nome LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + nome + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    
                    String mensagem = String.format("Nome: %s\nEmail: %s\nTelefone: %s",
                            resultSet.getString("nome"),
                            resultSet.getString("email"),
                            resultSet.getString("telefone"));

                    JOptionPane.showMessageDialog(null, mensagem, "Informações do Quadrinho", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum quadrinho encontrado com o nome informado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao buscar quadrinho.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}











