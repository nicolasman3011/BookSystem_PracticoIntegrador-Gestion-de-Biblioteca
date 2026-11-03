package biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión mediante JDBC con la base de datos SQLite.
 */
public class ConexionDB {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            return null;
        }
    }
}