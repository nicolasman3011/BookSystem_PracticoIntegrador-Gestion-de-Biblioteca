package biblioteca.dao;

import biblioteca.Libro;
import java.sql.*;
import java.util.ArrayList;

/**
 * Objeto de acceso a datos para la entidad Libro.
 */
public class LibroDAO {

    public boolean insertarLibro(Libro p_libro) {
        String sql = "INSERT INTO libros (titulo, edicion, editorial, anio) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p_libro.getTitulo());
            pstmt.setInt(2, p_libro.getEdicion());
            pstmt.setString(3, p_libro.getEditorial());
            pstmt.setInt(4, p_libro.getAnio());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Libro> obtenerTodos() {
        ArrayList<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Libro libro = new Libro(
                    rs.getString("titulo"),
                    rs.getInt("edicion"),
                    rs.getString("editorial"),
                    rs.getInt("anio")
                );
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener libros: " + e.getMessage());
        }
        return lista;
    }
}