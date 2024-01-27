package loja;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Listar {

    private Connection connection;

    public Listar(Connection connection) {
        this.connection = connection;
    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes");

        while (resultSet.next()) {
            
            String nome = resultSet.getString("nome");
            String email = resultSet.getString("email");
            String telefone = resultSet.getString("telefone");

            Cliente cliente = new Cliente(nome, email, telefone);
            clientes.add(cliente);
        }

        return clientes;
    }
}




