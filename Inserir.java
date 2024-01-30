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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quadrinhos (nome, email, telefone) VALUES (?, ?, ?)");
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, telefone);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}







