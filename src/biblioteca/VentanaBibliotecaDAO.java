package biblioteca;

import biblioteca.dao.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Interfaz Gráfica de Usuario (GUI) para el Sistema de Biblioteca - TP7.
 * Actúa como la Vista y Controlador, conectando los componentes visuales de Swing 
 * con el Modelo y la Capa de Persistencia (DAO).
 */
public class VentanaBibliotecaDAO extends JFrame implements ActionListener {
    
    // --- EL MODELO Y LOS DAO ---
    private Biblioteca miBiblioteca;
    private SocioDAO socioDAO;
    private LibroDAO libroDAO;
    private PrestamoDAO prestamoDAO;
    
    // --- COMPONENTES VISUALES (SWING) ---
    private JTextArea areaResultados;
    private JButton btnListarSociosLibros;
    private JButton btnCantEstudiantes;
    private JButton btnDocentesResponsables;
    private JButton btnTitulosUnicos;
    private JButton btnPrestamosVencidos;
    private JButton btnRegistrarPrestamo;
    private JButton btnRegistrarDevolucion;
    private JButton btnQuienTieneLibro;
    private JButton btnAgregarSocio;
    private JButton btnAgregarLibro;
    private JButton btnSalir;

    public VentanaBibliotecaDAO() {
        miBiblioteca = new Biblioteca("Biblioteca Central UNNE");
        
        socioDAO = new SocioDAO();
        libroDAO = new LibroDAO();
        prestamoDAO = new PrestamoDAO();
        cargarDatosDesdeBaseDeDatos();
        
        this.setTitle("Sistema de Gestión de Biblioteca - TP7 (SQLite + DAO)");
        this.setSize(1000, 650);
        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout(10, 10));

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(11, 1, 5, 5));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnListarSociosLibros = new JButton("Listar Socios y Libros");
        btnCantEstudiantes = new JButton("Cantidad Estudiantes");
        btnDocentesResponsables = new JButton("Docentes Responsables");
        btnTitulosUnicos = new JButton("Ver Títulos Únicos");
        btnPrestamosVencidos = new JButton("Préstamos Vencidos");
        btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        btnRegistrarDevolucion = new JButton("Registrar Devolución");
        btnQuienTieneLibro = new JButton("¿Quién tiene el libro?");
        btnAgregarSocio = new JButton("Nuevo Socio (BD)");
        btnAgregarLibro = new JButton("Nuevo Libro (BD)");
        btnSalir = new JButton("Salir");

        JButton[] botones = {
            btnListarSociosLibros, btnCantEstudiantes, btnDocentesResponsables,
            btnTitulosUnicos, btnPrestamosVencidos, btnRegistrarPrestamo,
            btnRegistrarDevolucion, btnQuienTieneLibro, btnAgregarSocio,
            btnAgregarLibro, btnSalir
        };

        for (JButton b : botones) {
            b.addActionListener(this);
            panelMenu.add(b);
        }

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setText(">>> Sistema conectado a la Base de Datos SQLite <<<\nSeleccione una opción del menú lateral para comenzar.");
        
        JScrollPane panelScroll = new JScrollPane(areaResultados);
        panelScroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        contenedor.add(panelMenu, BorderLayout.WEST);
        contenedor.add(panelScroll, BorderLayout.CENTER);
    }

    private void cargarDatosDesdeBaseDeDatos() {
        ArrayList<Socio> sociosDB = socioDAO.obtenerTodos();
        for (Socio s : sociosDB) {
            miBiblioteca.getSocios().add(s);
        }

        ArrayList<Libro> librosDB = libroDAO.obtenerTodos();
        for (Libro l : librosDB) {
            miBiblioteca.getLibros().add(l);
        }

        // Variables declaradas fuera del try para poder cerrarlas en el finally
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            rs = prestamoDAO.obtenerTodosRaw();
            if (rs != null) {
                // Recuperamos el Statement y la Connection desde el propio ResultSet
                stmt = rs.getStatement();
                conn = stmt.getConnection();

                while (rs.next()) {
                    int dni = rs.getInt("socio_dni");
                    String titulo = rs.getString("libro_titulo");
                    
                    String fechaRetiroStr = rs.getString("fecha_retiro");
                    String fechaDevolucionStr = rs.getString("fecha_devolucion");
                    
                    if(fechaRetiroStr == null) continue;

                    Date fechaRetiroSQL = Date.valueOf(fechaRetiroStr);
                    Date fechaDevolucionSQL = (fechaDevolucionStr != null) ? Date.valueOf(fechaDevolucionStr) : null;

                    Socio socio = miBiblioteca.buscarSocio(dni);
                    Libro libro = buscarLibroPorTitulo(titulo);

                    if (socio != null && libro != null) {
                        Calendar fechaRetiro = Calendar.getInstance();
                        fechaRetiro.setTimeInMillis(fechaRetiroSQL.getTime());

                        Prestamo prestamoRecuperado = new Prestamo(fechaRetiro, socio, libro);
                        
                        if (fechaDevolucionSQL != null) {
                            Calendar fechaDevolucion = Calendar.getInstance();
                            fechaDevolucion.setTimeInMillis(fechaDevolucionSQL.getTime());
                            prestamoRecuperado.registrarFechaDevolucion(fechaDevolucion);
                        }

                        socio.addPrestamo(prestamoRecuperado);
                        libro.agregarPrestamo(prestamoRecuperado);
                    }
                }
            }
        } catch (SQLException e) {
             System.out.println("Error procesando préstamos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
             System.out.println("Error parseando fecha desde SQLite: " + e.getMessage());
        } finally {
            // ESTO LIBERA EL BLOQUEO DE LA BASE DE DATOS
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println("Error cerrando recursos: " + ex.getMessage());
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object botonPulsado = e.getSource();

        if (botonPulsado == btnListarSociosLibros) {
            areaResultados.setText(miBiblioteca.listaDeSocios() + "\n\n" + miBiblioteca.listaDeLibros());
        } else if (botonPulsado == btnCantEstudiantes) {
            int cant = miBiblioteca.cantidadDeSociosPorTipo("Estudiante");
            areaResultados.setText(">>> REPORTE: CANTIDAD DE ESTUDIANTES <<<\n\nHay un total de " + cant + " socios Estudiantes registrados.");
        } else if (botonPulsado == btnDocentesResponsables) {
            areaResultados.setText(">>> REPORTE: DOCENTES RESPONSABLES <<<\n\n" + miBiblioteca.listaDeDocentesResponsables());
        } else if (botonPulsado == btnTitulosUnicos) {
            areaResultados.setText(">>> REPORTE: TÍTULOS EN CATÁLOGO <<<\n\n" + miBiblioteca.listaDeTitulos());
        } else if (botonPulsado == btnPrestamosVencidos) {
            String reporte = ">>> REPORTE: PRÉSTAMOS VENCIDOS <<<\n\n";
            if (miBiblioteca.prestamosVencidos().isEmpty()) {
                reporte += "No hay préstamos vencidos al día de la fecha.";
            } else {
                for (Prestamo p : miBiblioteca.prestamosVencidos()) {
                    reporte += p.toString() + "\n--------------------\n";
                }
            }
            areaResultados.setText(reporte);
        } else if (botonPulsado == btnQuienTieneLibro) {
            consultarQuienTieneLibroGUI();
        } else if (botonPulsado == btnRegistrarPrestamo) {
            registrarPrestamoGUI();
        } else if (botonPulsado == btnRegistrarDevolucion) {
            registrarDevolucionGUI();
        } else if (botonPulsado == btnAgregarSocio) {
            agregarSocioGUI();
        } else if (botonPulsado == btnAgregarLibro) {
            agregarLibroGUI();
        } else if (botonPulsado == btnSalir) {
            System.exit(0);
        }
    }

    private Libro buscarLibroPorTitulo(String titulo) {
        for (Libro l : miBiblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

    private void consultarQuienTieneLibroGUI() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a consultar:", "Consultar Libro", JOptionPane.QUESTION_MESSAGE);
        if (titulo == null || titulo.trim().isEmpty()) return;

        Libro libroEncontrado = buscarLibroPorTitulo(titulo);
        if (libroEncontrado == null) {
            JOptionPane.showMessageDialog(this, "El libro no existe en el catálogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nombreSocio = miBiblioteca.quienTieneElLibro(libroEncontrado);
            areaResultados.setText(">>> CONSULTA DE PRÉSTAMO <<<\n\nEl libro '" + titulo + "' se encuentra prestado a: " + nombreSocio);
        } catch (LibroNoPrestadoException ex) {
            areaResultados.setText(">>> AVISO <<<\n\n" + ex.getMessage());
        }
    }

    private void agregarSocioGUI() {
        String[] opciones = {"Estudiante", "Docente"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione el tipo de socio a registrar:", "Nuevo Socio",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == -1) return;

        String dniStr = JOptionPane.showInputDialog(this, "Ingrese el DNI del Socio:");
        if (dniStr == null || dniStr.trim().isEmpty()) return;
        
        int dni;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = JOptionPane.showInputDialog(this, "Ingrese Nombre y Apellido:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        Socio nuevoSocio = null;

        if (seleccion == 0) { // Estudiante
            String carrera = JOptionPane.showInputDialog(this, "Ingrese la Carrera:");
            if (carrera == null) return;
            nuevoSocio = new Estudiante(dni, nombre, carrera);
        } else { // Docente
            String area = JOptionPane.showInputDialog(this, "Ingrese el Área:");
            if (area == null) return;
            nuevoSocio = new Docente(dni, nombre, area);
        }

        miBiblioteca.getSocios().add(nuevoSocio);
        boolean exitoBD = socioDAO.insertarSocio(nuevoSocio);

        if (exitoBD) {
            JOptionPane.showMessageDialog(this, "¡Socio registrado con éxito en la Base de Datos!");
            areaResultados.setText(">>> SOCIO AGREGADO <<<\n\n" + nuevoSocio.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos (DNI duplicado o error SQL).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarLibroGUI() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el Título del Libro:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        String edicionStr = JOptionPane.showInputDialog(this, "Ingrese el número de Edición:");
        if (edicionStr == null) return;

        String editorial = JOptionPane.showInputDialog(this, "Ingrese la Editorial:");
        if (editorial == null) return;

        String anioStr = JOptionPane.showInputDialog(this, "Ingrese el Año de edición:");
        if (anioStr == null) return;

        try {
            int edicion = Integer.parseInt(edicionStr);
            int anio = Integer.parseInt(anioStr);

            Libro nuevoLibro = new Libro(titulo, edicion, editorial, anio);
            
            miBiblioteca.getLibros().add(nuevoLibro);
            boolean exitoBD = libroDAO.insertarLibro(nuevoLibro);

            if (exitoBD) {
                JOptionPane.showMessageDialog(this, "¡Libro agregado con éxito al catálogo y a la Base de Datos!");
                areaResultados.setText(">>> LIBRO AGREGADO <<<\n\nTítulo: " + titulo + " - Edición: " + edicion);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el libro en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edición y Año deben ser valores numéricos enteros.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarPrestamoGUI() {
        String dniStr = JOptionPane.showInputDialog(this, "Ingrese el DNI del socio solicitante:");
        if (dniStr == null || dniStr.trim().isEmpty()) return;

        int dni;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Socio socio = miBiblioteca.buscarSocio(dni);
        if (socio == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un socio con ese DNI.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a prestar:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        Libro libro = buscarLibroPorTitulo(titulo);
        if (libro == null || libro.prestado()) {
            JOptionPane.showMessageDialog(this, "El libro no existe o ya se encuentra prestado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!socio.puedePedir()) {
            JOptionPane.showMessageDialog(this, "El socio no está habilitado para solicitar préstamos (deudas o límite superado).", "Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar hoy = Calendar.getInstance();
        boolean exito = miBiblioteca.prestarLibro(hoy, socio, libro);

        if (exito) {
            Prestamo nuevoPrestamo = socio.getPrestamos().get(socio.getPrestamos().size() - 1);
            
            boolean exitoBD = prestamoDAO.insertarPrestamo(nuevoPrestamo);

            if (exitoBD) {
                JOptionPane.showMessageDialog(this, "¡Préstamo registrado y guardado en la Base de Datos con éxito!");
                areaResultados.setText(">>> PRÉSTAMO EXITOSO <<<\n\nLibro: " + libro.getTitulo() + "\nSocio: " + socio.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "El préstamo se hizo en memoria, pero falló el guardado en la Base de Datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarDevolucionGUI() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a devolver:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        Libro libro = buscarLibroPorTitulo(titulo);
        if (libro == null) {
            JOptionPane.showMessageDialog(this, "El libro no pertenece a la biblioteca.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Prestamo prestamoActivo = libro.ultimoPrestamo();
            miBiblioteca.devolverLibro(libro);
            
            if (prestamoActivo != null) {
                prestamoDAO.actualizarDevolucion(prestamoActivo);
            }

            JOptionPane.showMessageDialog(this, "¡Devolución registrada correctamente en el sistema y la Base de Datos!");
            areaResultados.setText(">>> DEVOLUCIÓN EXITOSA <<<\n\nSe registró la devolución de: " + libro.getTitulo());
        } catch (LibroNoPrestadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaBibliotecaDAO ventana = new VentanaBibliotecaDAO();
                ventana.setVisible(true);
            }
        });
    }
}