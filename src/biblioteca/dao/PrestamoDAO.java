package biblioteca.dao;

import biblioteca.Prestamo;
import java.sql.*;

/**
 * Data Access Object (DAO) para la clase Prestamo.
 * Maneja el registro histórico de operaciones entre Socios y Ejemplares (Libros).
 * 
 * @author Equipo BookSystem
 * @version 2.0 (Adaptado para referenciar a libro_id)
 */
public class PrestamoDAO {
    private Connection conexion;

    public PrestamoDAO() {
        this.conexion = ConexionDB.getConexion();
    }

    /**
     * Registra un nuevo préstamo vinculando a un socio con el ID físico de un ejemplar.
     * 
     * @param p_prestamo Objeto Prestamo con los datos de la operación.
     * @return true si la inserción fue correcta.
     */
    public boolean insertarPrestamo(Prestamo p_prestamo) {
        String sql = "INSERT INTO prestamos (socio_dni, libro_id, fecha_retiro, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, p_prestamo.getSocio().getDniSocio());
            // Guardamos la referencia física exacta del libro (Clave foránea libro_id)
            pstmt.setInt(2, p_prestamo.getLibro().getIdLibro()); 
            
            // Conversión de fechas a java.sql.Date para SQLite
            java.sql.Date fechaRetiroSQL = new java.sql.Date(p_prestamo.getFechaRetiro().getTimeInMillis());
            pstmt.setDate(3, fechaRetiroSQL);
            
            if (p_prestamo.getFechaDevolucion() != null) {
                java.sql.Date fechaDevolucionSQL = new java.sql.Date(p_prestamo.getFechaDevolucion().getTimeInMillis());
                pstmt.setDate(4, fechaDevolucionSQL);
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado de un préstamo para asentar su fecha de devolución.
     * 
     * @param p_prestamo El préstamo que acaba de ser finalizado.
     * @return true si la base de datos se actualizó correctamente.
     */
    public boolean actualizarDevolucion(Prestamo p_prestamo) {
        // La actualización usa la conjunción del DNI del socio y el ID FÍSICO del libro para ser exacta
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE socio_dni = ? AND libro_id = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
            
            java.sql.Date fechaDevolucionSQL = new java.sql.Date(p_prestamo.getFechaDevolucion().getTimeInMillis());
            pstmt.setDate(1, fechaDevolucionSQL);
            pstmt.setInt(2, p_prestamo.getSocio().getDniSocio());
            pstmt.setInt(3, p_prestamo.getLibro().getIdLibro()); 
            
            int filasModificadas = pstmt.executeUpdate();
            return filasModificadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar la devolución: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera el conjunto de datos crudos (ResultSet) de todos los préstamos históricos.
     * Este método extrae libro_id para que VentanaBibliotecaDAO arme los objetos.
     * 
     * @return ResultSet con la tabla de préstamos.
     * @throws SQLException Si hay un problema de comunicación con SQLite.
     */
    public ResultSet obtenerTodosRaw() throws SQLException {
        // Extraemos libro_id (FK) en vez del antiguo título
        String sql = "SELECT id_prestamo, socio_dni, libro_id, fecha_retiro, fecha_devolucion FROM prestamos";
        Statement stmt = this.conexion.createStatement();
        return stmt.executeQuery(sql);
    }
}
