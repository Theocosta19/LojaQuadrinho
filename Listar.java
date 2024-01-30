package loja;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listar {
    private Connection connection;

    public Listar(Connection connection) {
        this.connection = connection;
    }

    public DefaultTableModel listarQuadrinhos() throws SQLException {
        String query = "SELECT nome, email, telefone FROM quadrinhos";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nome");
        model.addColumn("Email");
        model.addColumn("Telefone");

        while (rs.next()) {
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String telefone = rs.getString("telefone");

            Object[] data = {nome, email, telefone};
            model.addRow(data);
        }

        statement.close();
        return model;
    }
}






