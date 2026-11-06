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
 * Actúa como la Vista y Controlador (Patrón MVC), conectando los componentes visuales 
 * de Swing con el Modelo (Biblioteca) y la Capa de Persistencia (DAO).
 * 
 * @author Equipo BookSystem
 * @version 3.0 (Con JComboBox y JRadioButton)
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

    /**
     * Constructor de la ventana principal. Configura la conexión a la base de datos,
     * hidrata el modelo y dibuja la interfaz gráfica.
     */
    public VentanaBibliotecaDAO() {
        this.miBiblioteca = new Biblioteca("Biblioteca Central UNNE");
        
        this.socioDAO = new SocioDAO();
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
        
        this.cargarDatosDesdeBaseDeDatos();
        
        this.setTitle("Sistema de Gestión de Biblioteca - TP7 (SQLite + DAO)");
        this.setSize(1000, 650);
        this.setLocationRelativeTo(null); // Centra la ventana
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent p_evento) {
                System.exit(0);
            }
        });

        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout(10, 10));

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(11, 1, 5, 5));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicialización de botones
        this.btnListarSociosLibros = new JButton("Listar Socios y Libros");
        this.btnCantEstudiantes = new JButton("Cantidad Estudiantes");
        this.btnDocentesResponsables = new JButton("Docentes");
        this.btnTitulosUnicos = new JButton("Ver Títulos");
        this.btnPrestamosVencidos = new JButton("Préstamos Vencidos");
        this.btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        this.btnRegistrarDevolucion = new JButton("Registrar Devolución");
        this.btnQuienTieneLibro = new JButton("¿Quién tiene el libro?");
        this.btnAgregarSocio = new JButton("Nuevo Socio (BD)");
        this.btnAgregarLibro = new JButton("Nuevo Libro (BD)");
        this.btnSalir = new JButton("Salir");

        JButton[] botones = {
            this.btnListarSociosLibros, this.btnCantEstudiantes, this.btnDocentesResponsables,
            this.btnTitulosUnicos, this.btnPrestamosVencidos, this.btnRegistrarPrestamo,
            this.btnRegistrarDevolucion, this.btnQuienTieneLibro, this.btnAgregarSocio,
            this.btnAgregarLibro, this.btnSalir
        };

        for (JButton b : botones) {
            b.addActionListener(this);
            panelMenu.add(b);
        }

        this.areaResultados = new JTextArea();
        this.areaResultados.setEditable(false);
        this.areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        this.areaResultados.setText(">>> Sistema conectado a la Base de Datos SQLite <<<\nSeleccione una opción del menú lateral para comenzar.");
        
        JScrollPane panelScroll = new JScrollPane(this.areaResultados);
        panelScroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        contenedor.add(panelMenu, BorderLayout.WEST);
        contenedor.add(panelScroll, BorderLayout.CENTER);
    }

    /**
     * Extrae los datos almacenados en SQLite y reconstruye los objetos en memoria RAM.
     */
    private void cargarDatosDesdeBaseDeDatos() {
        ArrayList<Socio> sociosDB = this.socioDAO.obtenerTodos();
        for (Socio s : sociosDB) {
            this.miBiblioteca.getSocios().add(s);
        }

        ArrayList<Libro> librosDB = this.libroDAO.obtenerTodos();
        for (Libro l : librosDB) {
            this.miBiblioteca.getLibros().add(l);
        }

        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            rs = this.prestamoDAO.obtenerTodosRaw();
            if (rs != null) {
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

                    Socio socio = this.miBiblioteca.buscarSocio(dni);
                    Libro libro = this.buscarLibroPorTitulo(titulo);

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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println("Error cerrando recursos: " + ex.getMessage());
            }
        }
    }

    /**
     * Captura y gestiona todos los eventos de clic generados por el usuario.
     * 
     * @param p_evento El evento de acción originado en los botones.
     */
    public void actionPerformed(ActionEvent p_evento) {
        Object botonPulsado = p_evento.getSource();

        if (botonPulsado == this.btnListarSociosLibros) {
            this.areaResultados.setText(this.miBiblioteca.listaDeSocios() + "\n\n" + this.miBiblioteca.listaDeLibros());
        } else if (botonPulsado == this.btnCantEstudiantes) {
            int cant = this.miBiblioteca.cantidadDeSociosPorTipo("Estudiante");
            this.areaResultados.setText(">>> REPORTE: CANTIDAD DE ESTUDIANTES <<<\n\nHay un total de " + cant + " socios Estudiantes registrados.");
        } else if (botonPulsado == this.btnDocentesResponsables) {
            this.areaResultados.setText(">>> REPORTE: DOCENTES <<<\n\n" + this.miBiblioteca.listaDeDocentesResponsables());
        } else if (botonPulsado == this.btnTitulosUnicos) {
            this.areaResultados.setText(">>> REPORTE: TÍTULOS EN CATÁLOGO <<<\n\n" + this.miBiblioteca.listaDeTitulos());
        } else if (botonPulsado == this.btnPrestamosVencidos) {
            String reporte = ">>> REPORTE: PRÉSTAMOS VENCIDOS <<<\n\n";
            if (this.miBiblioteca.prestamosVencidos().isEmpty()) {
                reporte += "No hay préstamos vencidos al día de la fecha.";
            } else {
                for (Prestamo p : this.miBiblioteca.prestamosVencidos()) {
                    reporte += p.toString() + "\n--------------------\n";
                }
            }
            this.areaResultados.setText(reporte);
        } else if (botonPulsado == this.btnQuienTieneLibro) {
            this.consultarQuienTieneLibroGUI();
        } else if (botonPulsado == this.btnRegistrarPrestamo) {
            this.registrarPrestamoGUI();
        } else if (botonPulsado == this.btnRegistrarDevolucion) {
            this.registrarDevolucionGUI();
        } else if (botonPulsado == this.btnAgregarSocio) {
            this.agregarSocioGUI();
        } else if (botonPulsado == this.btnAgregarLibro) {
            this.agregarLibroGUI();
        } else if (botonPulsado == this.btnSalir) {
            System.exit(0);
        }
    }

    /**
     * Busca un libro en la colección por su título exacto.
     * 
     * @param p_titulo Título del libro a buscar.
     * @return El objeto Libro si existe, o null si no se encuentra.
     */
    private Libro buscarLibroPorTitulo(String p_titulo) {
        for (Libro l : this.miBiblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(p_titulo)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Interfaz gráfica mcon JComboBox para consultar el estado de un libro.
     */
    private void consultarQuienTieneLibroGUI() {
        if (this.miBiblioteca.getLibros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El catálogo está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Crear el JComboBox y llenarlo con los títulos existentes
        JComboBox<String> comboLibros = new JComboBox<>();
        for (Libro libro : this.miBiblioteca.getLibros()) {
            comboLibros.addItem(libro.getTitulo());
        }

        // 2. Crear un panel personalizado para agrupar etiqueta y selector
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Seleccione el libro a consultar:"));
        panel.add(comboLibros);

        // 3. Mostrar el cuadro de diálogo inyectándole nuestro panel
        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Consultar Libro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (seleccion == JOptionPane.OK_OPTION) {
            String tituloSeleccionado = (String) comboLibros.getSelectedItem();
            Libro libroEncontrado = this.buscarLibroPorTitulo(tituloSeleccionado);
            
            try {
                String nombreSocio = this.miBiblioteca.quienTieneElLibro(libroEncontrado);
                this.areaResultados.setText(">>> CONSULTA DE PRÉSTAMO <<<\n\nEl libro '" + tituloSeleccionado + "' se encuentra prestado a: " + nombreSocio);
            } catch (LibroNoPrestadoException ex) {
                this.areaResultados.setText(">>> AVISO <<<\n\n" + ex.getMessage());
            }
        }
    }

    /**
     * Interfaz gráfica con JRadioButton para dar de alta a un socio.
     */
    private void agregarSocioGUI() {
        // 1. Crear los componentes de texto
        JTextField txtDni = new JTextField();
        JTextField txtNombre = new JTextField();
        
        // 2. Crear los JRadioButton
        JRadioButton rbEstudiante = new JRadioButton("Estudiante", true); // Seleccionado por defecto
        JRadioButton rbDocente = new JRadioButton("Docente");
        
        // 3. Agruparlos para que sean mutuamente excluyentes (solo se elige uno)
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbEstudiante);
        grupoTipo.add(rbDocente);
        
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRadios.add(rbEstudiante);
        panelRadios.add(rbDocente);

        // 4. Armar el panel principal del formulario
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("D.N.I.:"));
        panel.add(txtDni);
        panel.add(new JLabel("Nombre y Apellido:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Tipo de Socio:"));
        panel.add(panelRadios);

        // 5. Mostrar el formulario
        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Registrar Nuevo Socio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String nombre = txtNombre.getText();
                
                if (nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Socio nuevoSocio = null;

                // Verificamos qué botón de opción seleccionó el usuario
                if (rbEstudiante.isSelected()) { 
                    String carrera = JOptionPane.showInputDialog(this, "Ingrese la Carrera del Estudiante:");
                    if (carrera == null) return;
                    nuevoSocio = new Estudiante(dni, nombre, carrera);
                } else if (rbDocente.isSelected()) {
                    String area = JOptionPane.showInputDialog(this, "Ingrese el Área del Docente:");
                    if (area == null) return;
                    nuevoSocio = new Docente(dni, nombre, area);
                }

                this.miBiblioteca.getSocios().add(nuevoSocio);
                boolean exitoBD = this.socioDAO.insertarSocio(nuevoSocio);

                if (exitoBD) {
                    JOptionPane.showMessageDialog(this, "¡Socio registrado con éxito en la Base de Datos!");
                    this.areaResultados.setText(">>> SOCIO AGREGADO <<<\n\n" + nuevoSocio.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos (DNI duplicado).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Captura de datos básicos mediante JSpinner y campos para alta de libro.
     */
    private void agregarLibroGUI() {
        JTextField txtTitulo = new JTextField();
        JSpinner spinEdicion = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Solo números
        JTextField txtEditorial = new JTextField();
        JSpinner spinAnio = new JSpinner(new SpinnerNumberModel(2023, 1500, 2100, 1)); // Solo números

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Edición:"));
        panel.add(spinEdicion);
        panel.add(new JLabel("Editorial:"));
        panel.add(txtEditorial);
        panel.add(new JLabel("Año:"));
        panel.add(spinAnio);

        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Registrar Nuevo Libro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            String titulo = txtTitulo.getText();
            String editorial = txtEditorial.getText();
            int edicion = (int) spinEdicion.getValue();
            int anio = (int) spinAnio.getValue();

            if (titulo.trim().isEmpty() || editorial.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Libro nuevoLibro = new Libro(titulo, edicion, editorial, anio);
            this.miBiblioteca.getLibros().add(nuevoLibro);
            boolean exitoBD = this.libroDAO.insertarLibro(nuevoLibro);

            if (exitoBD) {
                JOptionPane.showMessageDialog(this, "¡Libro agregado con éxito al catálogo!");
                this.areaResultados.setText(">>> LIBRO AGREGADO <<<\n\nTítulo: " + titulo + " - Edición: " + edicion);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el libro en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Interfaz gráfica para registrar préstamos (Con selectores en vez de texto tipeado).
     */
    private void registrarPrestamoGUI() {
        if (this.miBiblioteca.getSocios().isEmpty() || this.miBiblioteca.getLibros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan socios o libros en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Selector de Socios
        JComboBox<String> comboSocios = new JComboBox<>();
        for (Socio s : this.miBiblioteca.getSocios()) {
            comboSocios.addItem(s.getDniSocio() + " - " + s.getNombre());
        }

        // 2. Selector de Libros
        JComboBox<String> comboLibros = new JComboBox<>();
        for (Libro l : this.miBiblioteca.getLibros()) {
            comboLibros.addItem(l.getTitulo());
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Seleccione el Socio:"));
        panel.add(comboSocios);
        panel.add(new JLabel("Seleccione el Libro:"));
        panel.add(comboLibros);

        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Registrar Préstamo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            String socioSeleccionado = (String) comboSocios.getSelectedItem();
            int dni = Integer.parseInt(socioSeleccionado.split(" - ")[0]); // Extraemos el DNI del String
            String tituloSeleccionado = (String) comboLibros.getSelectedItem();

            Socio socio = this.miBiblioteca.buscarSocio(dni);
            Libro libro = this.buscarLibroPorTitulo(tituloSeleccionado);

            if (libro.prestado()) {
                JOptionPane.showMessageDialog(this, "El libro ya se encuentra prestado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!socio.puedePedir()) {
                JOptionPane.showMessageDialog(this, "El socio no está habilitado para solicitar préstamos.", "Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Calendar hoy = Calendar.getInstance();
            boolean exito = this.miBiblioteca.prestarLibro(hoy, socio, libro);

            if (exito) {
                Prestamo nuevoPrestamo = socio.getPrestamos().get(socio.getPrestamos().size() - 1);
                boolean exitoBD = this.prestamoDAO.insertarPrestamo(nuevoPrestamo);

                if (exitoBD) {
                    JOptionPane.showMessageDialog(this, "¡Préstamo registrado en la Base de Datos!");
                    this.areaResultados.setText(">>> PRÉSTAMO EXITOSO <<<\n\nLibro: " + libro.getTitulo() + "\nSocio: " + socio.getNombre());
                } else {
                    JOptionPane.showMessageDialog(this, "Fallo al guardar en BD.", "Error SQL", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Interfaz gráfica para devoluciones con selector de libros.
     */
    private void registrarDevolucionGUI() {
        if (this.miBiblioteca.getLibros().isEmpty()) return;

        JComboBox<String> comboLibros = new JComboBox<>();
        for (Libro l : this.miBiblioteca.getLibros()) {
            if (l.prestado()) { // Solo mostramos los libros que actualmente están prestados
                comboLibros.addItem(l.getTitulo());
            }
        }
        
        if (comboLibros.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay ningún libro prestado actualmente.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Seleccione el libro a devolver:"));
        panel.add(comboLibros);

        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Registrar Devolución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            String titulo = (String) comboLibros.getSelectedItem();
            Libro libro = this.buscarLibroPorTitulo(titulo);

            try {
                Prestamo prestamoActivo = libro.ultimoPrestamo();
                this.miBiblioteca.devolverLibro(libro);
                
                if (prestamoActivo != null) {
                    this.prestamoDAO.actualizarDevolucion(prestamoActivo);
                }

                JOptionPane.showMessageDialog(this, "¡Devolución registrada correctamente!");
                this.areaResultados.setText(">>> DEVOLUCIÓN EXITOSA <<<\n\nSe registró la devolución de: " + libro.getTitulo());
            } catch (LibroNoPrestadoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método ejecutable principal.
     * 
     * @param p_args Argumentos de la línea de comandos.
     */
    public static void main(String[] p_args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaBibliotecaDAO ventana = new VentanaBibliotecaDAO();
                ventana.setVisible(true);
            }
        });
    }
}
