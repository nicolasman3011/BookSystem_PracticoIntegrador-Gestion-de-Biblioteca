package biblioteca.dao;

import biblioteca.Libro;
import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) para la clase Libro.
 * Gestiona la persistencia de los ejemplares físicos de la biblioteca en la Base de Datos SQLite.
 * 
 * @author Equipo BookSystem
 * @version 2.0 (Adaptado para copias múltiples mediante idLibro)
 */
public class LibroDAO {
    private Connection conexion;

    public LibroDAO() {
        this.conexion = ConexionDB.getConexion();
    }

    /**
     * Inserta un nuevo ejemplar físico en la base de datos.
     * 
     * @param p_libro El objeto Libro que se desea persistir.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarLibro(Libro p_libro) {
        String sql = "INSERT INTO libros (id_libro, titulo, edicion, editorial, anio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
            // Guardamos el ID único del ejemplar
            pstmt.setInt(1, p_libro.getIdLibro()); 
            pstmt.setString(2, p_libro.getTitulo());
            pstmt.setInt(3, p_libro.getEdicion());
            pstmt.setString(4, p_libro.getEditorial());
            pstmt.setInt(5, p_libro.getAnio());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar libro en BD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera todos los libros almacenados en la base de datos.
     * 
     * @return Una colección (ArrayList) con todos los objetos Libro instanciados.
     */
    public ArrayList<Libro> obtenerTodos() {
        ArrayList<Libro> listaLibros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        
        try (Statement stmt = this.conexion.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Extracción de datos, incluyendo la clave primaria id_libro
                int idLibro = rs.getInt("id_libro"); 
                String titulo = rs.getString("titulo");
                int edicion = rs.getInt("edicion");
                String editorial = rs.getString("editorial");
                int anio = rs.getInt("anio");
                
                // Instanciación utilizando el constructor modificado con idLibro
                Libro libro = new Libro(idLibro, titulo, edicion, editorial, anio);
                listaLibros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al recuperar libros desde BD: " + e.getMessage());
        }
        return listaLibros;
    }
}
