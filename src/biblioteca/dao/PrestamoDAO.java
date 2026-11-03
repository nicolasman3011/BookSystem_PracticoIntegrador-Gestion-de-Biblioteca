package biblioteca.dao;

import biblioteca.*;
import java.sql.*;
import java.util.Calendar;

/**
 * Objeto de acceso a datos para la entidad Prestamo.
 */
public class PrestamoDAO {

    public boolean insertarPrestamo(Prestamo p_prestamo) {
        String sql = "INSERT INTO prestamos (socio_dni, libro_titulo, fecha_retiro, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, p_prestamo.getSocio().getDniSocio());
            pstmt.setString(2, p_prestamo.getLibro().getTitulo());
            
            java.sql.Date fechaRetiroSQL = new java.sql.Date(p_prestamo.getFechaRetiro().getTimeInMillis());
            pstmt.setDate(3, fechaRetiroSQL);
            
            if (p_prestamo.getFechaDevolucion() != null) {
                java.sql.Date fechaDevolucionSQL = new java.sql.Date(p_prestamo.getFechaDevolucion().getTimeInMillis());
                pstmt.setDate(4, fechaDevolucionSQL);
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarDevolucion(Prestamo p_prestamo) {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE socio_dni = ? AND libro_titulo = ? AND fecha_devolucion IS NULL";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            java.sql.Date fechaDevolucionSQL = new java.sql.Date(p_prestamo.getFechaDevolucion().getTimeInMillis());
            pstmt.setDate(1, fechaDevolucionSQL);
            
            pstmt.setInt(2, p_prestamo.getSocio().getDniSocio());
            pstmt.setString(3, p_prestamo.getLibro().getTitulo());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar devolución: " + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerTodosRaw() {
        String sql = "SELECT * FROM prestamos";
        try {
             Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error al obtener préstamos: " + e.getMessage());
            return null;
        }
    }
}