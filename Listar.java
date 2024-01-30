package loja;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Listar {

    private Connection connection;

    public Listar(Connection connection) {
        this.connection = connection;
    }

    public DefaultTableModel listarClientes() throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nome");
        tableModel.addColumn("Email");
        tableModel.addColumn("Telefone");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes");

        while (resultSet.next()) {
            String nome = resultSet.getString("nome");
            String email = resultSet.getString("email");
            String telefone = resultSet.getString("telefone");

            Object[] rowData = {nome, email, telefone};
            tableModel.addRow(rowData);
        }

        return tableModel;
    }
}





