package org.egibide.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Proporciona la conexión MariaDB compartida por los DAO de la aplicación.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            String driver = "org.mariadb.jdbc.Driver";
            Class.forName(driver);

            String url = "jdbc:mariadb://localhost:3306/asociacionjuegos";
            String user = "root";
            String password = "";
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el controlador JDBC de MariaDB.", e);
        }
    }

    /**
     * Devuelve la conexión JDBC compartida por los DAO.
     *
     * @return conexión JDBC activa de la aplicación
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Obtiene la instancia única y vuelve a abrir la conexión si se cerró.
     *
     * @return proveedor de la conexión de base de datos
     * @throws SQLException si no se puede inicializar o reabrir la conexión
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
