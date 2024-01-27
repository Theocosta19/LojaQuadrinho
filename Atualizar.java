package loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Atualizar {
    private Connection connection;

    public Atualizar(Connection connection) {
        this.connection = connection;
    }

    public void executarAtualizacao(String nome, String email, String telefone) {
        try {
            String query = "UPDATE clientes SET email = ?, telefone = ? WHERE nome = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, telefone);
                preparedStatement.setString(3, nome);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





