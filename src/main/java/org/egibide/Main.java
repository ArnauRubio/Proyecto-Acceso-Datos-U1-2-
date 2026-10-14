package org.egibide;

import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    static void main(String[] args) throws SQLException {
        // Intentamos conectar a la BD
        DatabaseConnection dbc = DatabaseConnection.getInstance();
        Connection conexion = dbc.getConnection();

        System.out.println(conexion);
    }
}
