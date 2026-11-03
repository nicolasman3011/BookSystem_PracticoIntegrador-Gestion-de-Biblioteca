package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

/**
 * Interfaz gráfica (GUI) para la gestión de la biblioteca utilizando Java Swing.
 * Actúa como Controlador y Vista, reemplazando a la clase de consola GestionBiblioteca.
 * 
 * @author Grupo Completo - TP7 Integrador
 */
public class VentanaBiblioteca extends JFrame implements ActionListener {
    
    // --- EL MODELO ---
    private Biblioteca miBiblioteca;
    
    // --- COMPONENTES VISUALES ---
    private JTextArea areaResultados;
    private JButton btnListarSociosLibros;
    private JButton btnCantEstudiantes;
    private JButton btnDocentesResponsables;
    private JButton btnTitulosUnicos;
    private JButton btnPrestamosVencidos;
    private JButton btnRegistrarPrestamo;
    private JButton btnRegistrarDevolucion;
    private JButton btnQuienTieneLibro;
    private JButton btnSalir;

    /**
     * Constructor de la ventana. Configura el contenedor y los componentes.
     */
    public VentanaBiblioteca() {
        // 1. Inicializar el Modelo y cargar datos de prueba
        miBiblioteca = new Biblioteca("Biblioteca Central UNNE");
        cargarDatosPrueba();
        
        // 2. Configuración del JFrame (Ventana principal)
        this.setTitle("Sistema de Gestión de Biblioteca - TP7");
        this.setSize(900, 600);
        
        // Manejador para cerrar la aplicación (Exigencia del Tema 6)
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 3. Crear el contenedor principal y asignarle un BorderLayout
        Container contenedor = this.getContentPane();
        contenedor.setLayout(new BorderLayout(10, 10));

        // 4. Crear el Panel lateral izquierdo (Menú de botones) con GridLayout
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(9, 1, 5, 5)); // 9 botones, 1 columna, separación de 5px
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen interno

        // Instanciar botones
        btnListarSociosLibros = new JButton("Listar Socios y Libros");
        btnCantEstudiantes = new JButton("Cantidad Estudiantes");
        btnDocentesResponsables = new JButton("Docentes Responsables");
        btnTitulosUnicos = new JButton("Ver Títulos");
        btnPrestamosVencidos = new JButton("Préstamos Vencidos");
        btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        btnRegistrarDevolucion = new JButton("Registrar Devolución");
        btnQuienTieneLibro = new JButton("¿Quién tiene el libro?");
        btnSalir = new JButton("Salir");

        // Registrar el "oyente" (ActionListener) para cada botón
        btnListarSociosLibros.addActionListener(this);
        btnCantEstudiantes.addActionListener(this);
        btnDocentesResponsables.addActionListener(this);
        btnTitulosUnicos.addActionListener(this);
        btnPrestamosVencidos.addActionListener(this);
        btnRegistrarPrestamo.addActionListener(this);
        btnRegistrarDevolucion.addActionListener(this);
        btnQuienTieneLibro.addActionListener(this);
        btnSalir.addActionListener(this);

        // Añadir botones al panel lateral
        panelMenu.add(btnListarSociosLibros);
        panelMenu.add(btnCantEstudiantes);
        panelMenu.add(btnDocentesResponsables);
        panelMenu.add(btnTitulosUnicos);
        panelMenu.add(btnPrestamosVencidos);
        panelMenu.add(btnRegistrarPrestamo);
        panelMenu.add(btnRegistrarDevolucion);
        panelMenu.add(btnQuienTieneLibro);
        panelMenu.add(btnSalir);

        // 5. Crear el Área de Texto central para mostrar resultados (JTextArea)
        areaResultados = new JTextArea();
        areaResultados.setEditable(false); // Solo lectura
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Fuente monoespaciada para mejor alineación
        areaResultados.setText("Bienvenido al sistema de Gestión de la Biblioteca Central UNNE.\nSeleccione una opción del menú lateral para comenzar.");
        
        // Añadir barra de desplazamiento (JScrollPane) al área de texto
        JScrollPane panelScroll = new JScrollPane(areaResultados);
        panelScroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        // 6. Añadir los paneles al contenedor principal
        contenedor.add(panelMenu, BorderLayout.WEST);
        contenedor.add(panelScroll, BorderLayout.CENTER);
    }

    /**
     * Carga inicial automática de socios y libros.
     */
    private void cargarDatosPrueba() {
        // Carga de libros
        miBiblioteca.nuevoLibro("JAVA. Como Programar", 10, "Pearson", 2016);
        miBiblioteca.nuevoLibro("Longman. Diccionario Pocket", 2, "Longman", 2018);
        miBiblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);
        miBiblioteca.nuevoLibro("JAVA. Como Programar", 11, "Pearson", 2020); 

        // Carga de socios docentes
        miBiblioteca.nuevoSocioDocente(14524782, "Juan Perez", "Sistemas");
        miBiblioteca.nuevoSocioDocente(17982110, "Juan Fernández", "Matemática");
        miBiblioteca.nuevoSocioDocente(10912002, "María Alegre", "Física");

        // Carga de socios estudiantes
        miBiblioteca.nuevoSocioEstudiante(28987498, "Francisco Paenza", "Lic. en Sistemas");
        miBiblioteca.nuevoSocioEstudiante(31987123, "Cesar Milstein", "Ing. en Informática");

        // Simulación de un préstamo inicial para pruebas
        Socio docente1 = miBiblioteca.buscarSocio(14524782);
        Libro libro1 = miBiblioteca.getLibros().get(0); 

        if (docente1 != null && libro1 != null) {
            Calendar fechaRetiro = Calendar.getInstance();
            miBiblioteca.prestarLibro(fechaRetiro, docente1, libro1);
        }
    }

    /**
     * Método obligatorio de la interfaz ActionListener.
     * Define qué acción se ejecuta al presionar cada botón.
     */
    public void actionPerformed(ActionEvent e) {
        Object botonPulsado = e.getSource(); // Identifica qué botón generó el evento

        if (botonPulsado == btnListarSociosLibros) {
            // Limpia y muestra las dos listas concatenadas
            areaResultados.setText(miBiblioteca.listaDeSocios() + "\n\n" + miBiblioteca.listaDeLibros());
            
        } else if (botonPulsado == btnCantEstudiantes) {
            int cant = miBiblioteca.cantidadDeSociosPorTipo("Estudiante");
            areaResultados.setText(">>> REPORTE: CANTIDAD DE ESTUDIANTES <<<\n\nHay un total de " + cant + " socios de tipo Estudiante registrados.");
            
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
            // JOptionPane.showInputDialog levanta una ventana emergente para pedir un dato al usuario
            String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a consultar:", "Consultar Libro", JOptionPane.QUESTION_MESSAGE);
            
            if (titulo != null && !titulo.trim().isEmpty()) {
                Libro libroEncontrado = buscarLibroPorTitulo(titulo);
                
                if (libroEncontrado != null) {
                    try {
                        String nombre = miBiblioteca.quienTieneElLibro(libroEncontrado);
                        areaResultados.setText(">>> RESULTADO DE BÚSQUEDA <<<\n\nEl libro '" + titulo + "' lo tiene el socio: " + nombre);
                    } catch (LibroNoPrestadoException ex) {
                        areaResultados.setText(">>> AVISO DEL SISTEMA <<<\n\n" + ex.getMessage());
                    }
                } else {
                    areaResultados.setText(">>> ERROR <<<\n\nEl libro '" + titulo + "' no existe en el catálogo.");
                }
            }
            
        } else if (botonPulsado == btnRegistrarPrestamo) {
            registrarPrestamoGUI();
            
        } else if (botonPulsado == btnRegistrarDevolucion) {
            registrarDevolucionGUI();
            
        } else if (botonPulsado == btnSalir) {
            System.exit(0);
        }
    }
    
    // --- MÉTODOS AUXILIARES PARA VENTANAS EMERGENTES (DIALOGS) ---
    
    private Libro buscarLibroPorTitulo(String titulo) {
        for (Libro l : miBiblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

    private void registrarPrestamoGUI() {
        String dniStr = JOptionPane.showInputDialog(this, "Ingrese el DNI del Socio:");
        if (dniStr == null || dniStr.trim().isEmpty()) return;
        
        int dni = 0;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Socio socio = miBiblioteca.buscarSocio(dni);
        if (socio == null) {
            JOptionPane.showMessageDialog(this, "Socio no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a prestar:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        Libro libroEncontrado = buscarLibroPorTitulo(titulo);
        
        if (libroEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Libro no encontrado en catálogo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (libroEncontrado.prestado()) {
            JOptionPane.showMessageDialog(this, "El libro ya se encuentra prestado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!socio.puedePedir()) {
            JOptionPane.showMessageDialog(this, "El socio " + socio.getNombre() + " no está habilitado para pedir.", "Operación Denegada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = miBiblioteca.prestarLibro(Calendar.getInstance(), socio, libroEncontrado);
        if (exito) {
            areaResultados.setText(">>> PRÉSTAMO REGISTRADO CON ÉXITO <<<\n\nLibro: " + libroEncontrado.getTitulo() + "\nSocio: " + socio.getNombre());
            JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");
        }
    }

    private void registrarDevolucionGUI() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a devolver:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        Libro libroEncontrado = buscarLibroPorTitulo(titulo);
        if (libroEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Libro no encontrado en catálogo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            miBiblioteca.devolverLibro(libroEncontrado);
            areaResultados.setText(">>> DEVOLUCIÓN REGISTRADA CON ÉXITO <<<\n\nSe devolvió: " + libroEncontrado.getTitulo());
            JOptionPane.showMessageDialog(this, "Devolución registrada correctamente.");
        } catch (LibroNoPrestadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Devolución", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Punto de entrada principal de la aplicación.
     */
    public static void main(String[] args) {
        // Se instancia y se hace visible la interfaz gráfica
        VentanaBiblioteca ventana = new VentanaBiblioteca();
        ventana.setVisible(true);
    }
}