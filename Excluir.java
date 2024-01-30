package loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Excluir {
    private Connection connection;

    public Excluir(Connection connection) {
        this.connection = connection;
    }

    public void executarExclusao(String nome) {
        try {
            String query = "DELETE FROM clientes WHERE nome = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





