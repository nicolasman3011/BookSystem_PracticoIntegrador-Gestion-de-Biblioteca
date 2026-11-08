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
 * con el Modelo y la Base de Datos SQLite.
 * 
 * @author Equipo BookSystem
 * @version 4.2 (Con parche para lectura segura de fechas nulas)
 */
public class VentanaBibliotecaDAO extends JFrame implements ActionListener {
    
    private Biblioteca miBiblioteca;
    private SocioDAO socioDAO;
    private LibroDAO libroDAO;
    private PrestamoDAO prestamoDAO;
    
    private JTextArea areaResultados;
    private JButton btnListarSociosLibros, btnCantEstudiantes, btnDocentesResponsables;
    private JButton btnTitulosUnicos, btnPrestamosVencidos, btnRegistrarPrestamo;
    private JButton btnRegistrarDevolucion, btnQuienTieneLibro, btnAgregarSocio;
    private JButton btnAgregarLibro, btnSalir;

    /**
     * Constructor principal de la GUI.
     * @param p_biblioteca Instancia del modelo Biblioteca inyectada (MVC).
     */
    public VentanaBibliotecaDAO(Biblioteca p_biblioteca) {
        this.miBiblioteca = p_biblioteca;
        
        this.socioDAO = new SocioDAO();
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
        
        this.cargarDatosDesdeBaseDeDatos();
        
        this.setTitle("Sistema de Gestión de Biblioteca - BookSystem");
        this.setSize(1000, 650);
        this.setLocationRelativeTo(null);
        
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

        this.btnListarSociosLibros = new JButton("Listar Socios y Libros");
        this.btnCantEstudiantes = new JButton("Cantidad Estudiantes");
        this.btnDocentesResponsables = new JButton("Docentes");
        this.btnTitulosUnicos = new JButton("Ver Títulos Únicos");
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
        this.areaResultados.setText(">>> Sistema conectado a SQLite <<<\nSeleccione una opción del menú lateral para comenzar.");
        
        JScrollPane panelScroll = new JScrollPane(this.areaResultados);
        panelScroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        contenedor.add(panelMenu, BorderLayout.WEST);
        contenedor.add(panelScroll, BorderLayout.CENTER);
    }

    /**
     * Recupera la base de datos e hidrata la memoria, utilizando libro_id en lugar de título.
     * Incorpora validación estricta para evitar fallos de parseo en fechas nulas.
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
                    int idLibro = rs.getInt("libro_id"); 
                    
                    String fechaRetiroStr = rs.getString("fecha_retiro");
                    String fechaDevolucionStr = rs.getString("fecha_devolucion");
                    
                    if (fechaRetiroStr == null) continue;

                    Date fechaRetiroSQL = Date.valueOf(fechaRetiroStr);
                    
                    // CORRECCIÓN PARSEO: Blindaje contra fechas devueltas como texto "null"
                    Date fechaDevolucionSQL = null;
                    if (fechaDevolucionStr != null && !fechaDevolucionStr.trim().equalsIgnoreCase("null") && !fechaDevolucionStr.trim().isEmpty()) {
                        fechaDevolucionSQL = Date.valueOf(fechaDevolucionStr);
                    }

                    Socio socio = this.miBiblioteca.buscarSocio(dni);
                    Libro libro = this.miBiblioteca.buscarLibroPorId(idLibro); 

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
            } catch (SQLException ex) {
                System.out.println("Error cerrando recursos: " + ex.getMessage());
            }
        }
    }

    /**
     * Método controlador de eventos (Patrón MVC).
     * @param p_evento El evento detectado.
     */
    public void actionPerformed(ActionEvent p_evento) {
        Object boton = p_evento.getSource();

        if (boton == this.btnListarSociosLibros) {
            this.areaResultados.setText(this.miBiblioteca.listaDeSocios() + "\n\n" + this.miBiblioteca.listaDeLibros());
        } else if (boton == this.btnCantEstudiantes) {
            int cant = this.miBiblioteca.cantidadDeSociosPorTipo("Estudiante");
            this.areaResultados.setText("Cantidad Estudiantes: " + cant);
        } else if (boton == this.btnDocentesResponsables) {
            this.areaResultados.setText(this.miBiblioteca.listaDeDocentesResponsables());
        } else if (boton == this.btnTitulosUnicos) {
            this.areaResultados.setText(">>> TÍTULOS EN CATÁLOGO <<<\n" + this.miBiblioteca.listaDeTitulos());
        } else if (boton == this.btnPrestamosVencidos) {
            String reporte = ">>> PRÉSTAMOS VENCIDOS <<<\n\n";
            for (Prestamo p : this.miBiblioteca.prestamosVencidos()) {
                reporte += p.toString() + "\n";
            }
            this.areaResultados.setText(reporte);
        } else if (boton == this.btnQuienTieneLibro) {
            this.consultarQuienTieneLibroGUI();
        } else if (boton == this.btnRegistrarPrestamo) {
            this.registrarPrestamoGUI();
        } else if (boton == this.btnRegistrarDevolucion) {
            this.registrarDevolucionGUI();
        } else if (boton == this.btnAgregarSocio) {
            this.agregarSocioGUI();
        } else if (boton == this.btnAgregarLibro) {
            this.agregarLibroGUI();
        } else if (boton == this.btnSalir) {
            System.exit(0);
        }
    }

    /**
     * Consulta usando despliegue de ID para seleccionar la copia exacta.
     */
    private void consultarQuienTieneLibroGUI() {
        JComboBox<String> combo = new JComboBox<>();
        for (Libro l : this.miBiblioteca.getLibros()) {
            combo.addItem("ID: " + l.getIdLibro() + " - " + l.getTitulo());
        }

        int sel = JOptionPane.showConfirmDialog(this, combo, "Consultar Libro", JOptionPane.OK_CANCEL_OPTION);
        if (sel == JOptionPane.OK_OPTION && combo.getSelectedItem() != null) {
            String textoSel = (String) combo.getSelectedItem();
            int idFisico = Integer.parseInt(textoSel.split(" - ")[0].replace("ID: ", "").trim());
            
            Libro libroElegido = this.miBiblioteca.buscarLibroPorId(idFisico);
            try {
                String nombreSocio = this.miBiblioteca.quienTieneElLibro(libroElegido);
                this.areaResultados.setText("El libro ID " + idFisico + " lo tiene: " + nombreSocio);
            } catch (LibroNoPrestadoException ex) {
                this.areaResultados.setText(ex.getMessage());
            }
        }
    }

    /**
     * Interfaz gráfica mejorada para dar de alta a un socio.
     */
    private void agregarSocioGUI() {
        JTextField txtDni = new JTextField();
        JTextField txtNombre = new JTextField();
        JRadioButton rbEst = new JRadioButton("Estudiante", true); 
        JRadioButton rbDoc = new JRadioButton("Docente");
        
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbEst); grupoTipo.add(rbDoc);
        JPanel panelRadios = new JPanel(); panelRadios.add(rbEst); panelRadios.add(rbDoc);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("D.N.I.:")); panel.add(txtDni);
        panel.add(new JLabel("Nombre y Apellido:")); panel.add(txtNombre);
        panel.add(new JLabel("Tipo:")); panel.add(panelRadios);

        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Nuevo Socio", JOptionPane.OK_CANCEL_OPTION);

        if (seleccion == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String nombre = txtNombre.getText();
                Socio nuevoSocio = null;

                if (rbEst.isSelected()) { 
                    String carrera = JOptionPane.showInputDialog(this, "Carrera:");
                    nuevoSocio = this.miBiblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
                } else {
                    String area = JOptionPane.showInputDialog(this, "Área:");
                    nuevoSocio = this.miBiblioteca.nuevoSocioDocente(dni, nombre, area);
                }

                if (nuevoSocio != null && this.socioDAO.insertarSocio(nuevoSocio)) {
                    JOptionPane.showMessageDialog(this, "Socio guardado en BD!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: DNI duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "DNI debe ser número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Interfaz para agregar un libro asignando un ID único.
     */
    private void agregarLibroGUI() {
        JSpinner spinId = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        JTextField txtTitulo = new JTextField();
        JSpinner spinEdicion = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); 
        JTextField txtEditorial = new JTextField();
        JSpinner spinAnio = new JSpinner(new SpinnerNumberModel(2023, 1500, 2100, 1)); 

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("ID Único Físico:")); panel.add(spinId);
        panel.add(new JLabel("Título:")); panel.add(txtTitulo);
        panel.add(new JLabel("Edición:")); panel.add(spinEdicion);
        panel.add(new JLabel("Editorial:")); panel.add(txtEditorial);
        panel.add(new JLabel("Año:")); panel.add(spinAnio);

        int seleccion = JOptionPane.showConfirmDialog(this, panel, "Nuevo Libro", JOptionPane.OK_CANCEL_OPTION);

        if (seleccion == JOptionPane.OK_OPTION) {
            int idFisico = (int) spinId.getValue();
            
            if (this.miBiblioteca.buscarLibroPorId(idFisico) != null) {
                JOptionPane.showMessageDialog(this, "El ID físico ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Libro l = new Libro(idFisico, txtTitulo.getText(), (int)spinEdicion.getValue(), txtEditorial.getText(), (int)spinAnio.getValue());
            this.miBiblioteca.getLibros().add(l);
            this.libroDAO.insertarLibro(l);
            JOptionPane.showMessageDialog(this, "¡Libro agregado y guardado en BD!");
        }
    }

    /**
     * Registra préstamos asegurando usar el ID físico.
     */
    private void registrarPrestamoGUI() {
        JComboBox<String> comboSocios = new JComboBox<>();
        for (Socio s : this.miBiblioteca.getSocios()) { comboSocios.addItem(s.getDniSocio() + " - " + s.getNombre()); }

        JComboBox<String> comboLibros = new JComboBox<>();
        for (Libro l : this.miBiblioteca.getLibros()) { comboLibros.addItem("ID: " + l.getIdLibro() + " - " + l.getTitulo()); }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Socio:")); panel.add(comboSocios);
        panel.add(new JLabel("Libro (Ejemplar):")); panel.add(comboLibros);

        if (JOptionPane.showConfirmDialog(this, panel, "Registrar Préstamo", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int dni = Integer.parseInt(((String)comboSocios.getSelectedItem()).split(" - ")[0]);
            int idLibro = Integer.parseInt(((String)comboLibros.getSelectedItem()).split(" - ")[0].replace("ID: ", "").trim());

            Socio socio = this.miBiblioteca.buscarSocio(dni);
            Libro libro = this.miBiblioteca.buscarLibroPorId(idLibro);

            if (libro.prestado()) {
                JOptionPane.showMessageDialog(this, "Este ejemplar en particular ya está prestado.");
                return;
            }

            if (this.miBiblioteca.prestarLibro(Calendar.getInstance(), socio, libro)) {
                Prestamo nuevoPrestamo = socio.getPrestamos().get(socio.getPrestamos().size() - 1);
                this.prestamoDAO.insertarPrestamo(nuevoPrestamo);
                JOptionPane.showMessageDialog(this, "Préstamo exitoso en Memoria y BD.");
            }
        }
    }

    /**
     * Filtra y permite devolver libros por su ID.
     */
    private void registrarDevolucionGUI() {
        JComboBox<String> combo = new JComboBox<>();
        for (Libro l : this.miBiblioteca.getLibros()) {
            if (l.prestado()) combo.addItem("ID: " + l.getIdLibro() + " - " + l.getTitulo());
        }
        if (combo.getItemCount() == 0) return;

        if (JOptionPane.showConfirmDialog(this, combo, "Devolución", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int idLibro = Integer.parseInt(((String)combo.getSelectedItem()).split(" - ")[0].replace("ID: ", "").trim());
            Libro libro = this.miBiblioteca.buscarLibroPorId(idLibro);

            try {
                Prestamo pActivo = libro.ultimoPrestamo();
                this.miBiblioteca.devolverLibro(libro);
                if (pActivo != null) {
                    this.prestamoDAO.actualizarDevolucion(pActivo);
                }
                JOptionPane.showMessageDialog(this, "Devolución guardada.");
            } catch (Exception ex) {}
        }
    }
    
    /**
     * Clase principal de arranque del sistema.
     * @param p_args Argumentos base.
     */
    public static void main(String[] p_args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Biblioteca bibliotecaCentral = new Biblioteca("Biblioteca Central UNNE");
                VentanaBibliotecaDAO ventana = new VentanaBibliotecaDAO(bibliotecaCentral);
                ventana.setVisible(true);
            }
        });
    }
}
