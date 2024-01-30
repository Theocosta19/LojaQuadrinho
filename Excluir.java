package loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Excluir {
    private Connection connection;

    public Excluir(Connection connection) {
        this.connection = connection;
    }

    public void executarExclusao(String nome) {
        try {
            String query = "DELETE FROM loja.quadrinhos WHERE nome = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Quadrinho excluído com sucesso!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir quadrinho.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}








