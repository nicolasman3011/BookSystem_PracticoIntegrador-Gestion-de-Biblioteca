package biblioteca.dao;

import biblioteca.Prestamo;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Data Access Object (DAO) para la clase Prestamo.
 * Maneja el registro histórico de operaciones entre Socios y Ejemplares (Libros).
 * 
 * @author Equipo BookSystem
 * @version 3.1 (Corrección definitiva forzando el formato de fechas a String para SQLite)
 */
public class PrestamoDAO {
    private Connection conexion;

    /**
     * Constructor que inicializa la conexión a SQLite.
     */
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
            pstmt.setInt(2, p_prestamo.getLibro().getIdLibro()); 
            
            // Definimos el formato estricto que espera la base de datos
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // Convertimos la fecha de retiro a String
            String fechaRetiroStr = sdf.format(p_prestamo.getFechaRetiro().getTime());
            pstmt.setString(3, fechaRetiroStr);
            
            if (p_prestamo.getFechaDevolucion() != null) {
                String fechaDevolucionStr = sdf.format(p_prestamo.getFechaDevolucion().getTime());
                pstmt.setString(4, fechaDevolucionStr);
            } else {
                // Enviamos el null estricto como texto (VARCHAR)
                pstmt.setNull(4, java.sql.Types.VARCHAR);
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
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE socio_dni = ? AND libro_id = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
            
            // Formateamos la fecha a String para evitar que SQLite guarde los milisegundos
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaDevolucionStr = sdf.format(p_prestamo.getFechaDevolucion().getTime());
            
            pstmt.setString(1, fechaDevolucionStr);
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
     * 
     * @return ResultSet con la tabla de préstamos.
     * @throws SQLException Si hay un problema de comunicación con SQLite.
     */
    public ResultSet obtenerTodosRaw() throws SQLException {
        String sql = "SELECT id_prestamo, socio_dni, libro_id, fecha_retiro, fecha_devolucion FROM prestamos";
        Statement stmt = this.conexion.createStatement();
        return stmt.executeQuery(sql);
    }
}
