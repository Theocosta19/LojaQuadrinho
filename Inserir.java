package loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Inserir {
    private Connection connection;

    public Inserir(Connection connection) {
        this.connection = connection;
    }

    public void executarInsercao(String nome, String email, String telefone) {
        try {
            String query = "INSERT INTO clientes (nome, email, telefone) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, telefone);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





