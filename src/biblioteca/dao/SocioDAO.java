package biblioteca.dao;

import biblioteca.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Objeto de acceso a datos para Socio (Estudiante y Docente) aplicando Single Table.
 */
public class SocioDAO {

    public boolean insertarSocio(Socio p_socio) {
        String sql = "INSERT INTO socios (dni, nombre, dias_prestamo, tipo_socio, carrera, area) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, p_socio.getDniSocio());
            pstmt.setString(2, p_socio.getNombre());
            pstmt.setInt(3, p_socio.getDiasPrestamo());
            pstmt.setString(4, p_socio.soyDeLaClase());
            
            if (p_socio instanceof Estudiante) {
                Estudiante est = (Estudiante) p_socio;
                pstmt.setString(5, est.getCarrera());
                pstmt.setNull(6, Types.VARCHAR);
            } else if (p_socio instanceof Docente) {
                Docente doc = (Docente) p_socio;
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setString(6, doc.getArea());
            }
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar socio: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Socio> obtenerTodos() {
        ArrayList<Socio> lista = new ArrayList<>();
        String sql = "SELECT * FROM socios";
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int dni = rs.getInt("dni");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_socio");
                
                Socio socio = null;
                if (tipo.equals("Estudiante")) {
                    socio = new Estudiante(dni, nombre, rs.getString("carrera"));
                } else if (tipo.equals("Docente")) {
                    socio = new Docente(dni, nombre, rs.getString("area"));
                }
                
                if (socio != null) {
                    lista.add(socio);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener socios: " + e.getMessage());
        }
        return lista;
    }
}