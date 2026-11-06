package biblioteca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Práctico Integrador - Gestión de Biblioteca.
 * Administra la colección de libros, socios y los préstamos otorgados.
 * 
 * @author Ponce Silvana Beatriz, Nacimiento Nestor (Adaptado por Equipo BookSystem)
 * @version 3.0 (Compatible con idLibro, copias múltiples y estándares de la cátedra)
 */
public class Biblioteca {
    
    // --- ATRIBUTOS ---
    
    private String nombre;
    private ArrayList<Libro> libros;
    private ArrayList<Socio> socios;
     
    // --- CONSTRUCTORES ---
    
    /**
     * Constructor que inicializa el nombre y las colecciones vacías.
     * @param p_nombre Nombre de la biblioteca.
     */
    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
    }  

    // --- MUTADORES (Setters Privados para Doble Encapsulamiento) ---
    
    private void setNombre(String p_nombre) { 
        this.nombre = p_nombre; 
    }
    
    private void setLibros(ArrayList<Libro> p_libros) { 
        this.libros = p_libros; 
    }
    
    private void setSocios(ArrayList<Socio> p_socios) { 
        this.socios = p_socios; 
    }

    // --- OBSERVADORES (Getters Públicos) ---
    
    public String getNombre() { 
        return this.nombre; 
    }
    
    public ArrayList<Libro> getLibros() { 
        return this.libros; 
    }
    
    public ArrayList<Socio> getSocios() { 
        return this.socios; 
    }

    // --- MÉTODOS DE CREACIÓN DE ENTIDADES (UML) ---

    /**
     * Instancia un nuevo libro físico y lo agrega a la colección.
     * Adaptado del UML para recibir y asignar el ID físico de la base de datos.
     * 
     * @param p_idLibro Identificador único del ejemplar físico.
     * @param p_titulo Título del libro.
     * @param p_edicion Número de edición.
     * @param p_editorial Editorial.
     * @param p_anio Año de publicación.
     * @return El Libro instanciado, o null si el ID físico ya existe.
     */
    public Libro nuevoLibro(int p_idLibro, String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        if (this.buscarLibroPorId(p_idLibro) != null) {
            return null; // Evita duplicar el ID físico
        }
        Libro unLibro = new Libro(p_idLibro, p_titulo, p_edicion, p_editorial, p_anio);
        this.getLibros().add(unLibro);
        return unLibro;
    }

    /**
     * Instancia un nuevo Socio Estudiante y lo agrega a la colección si no existe su DNI.
     * 
     * @param p_dniSocio DNI del estudiante.
     * @param p_nombre Nombre y apellido.
     * @param p_carrera Carrera que cursa.
     * @return El Estudiante creado, o null si el DNI ya existía.
     */
    public Estudiante nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        if (this.buscarSocio(p_dniSocio) != null) {
            return null; 
        }
        Estudiante nuevoEstudiante = new Estudiante(p_dniSocio, p_nombre, p_carrera);
        this.getSocios().add(nuevoEstudiante);
        return nuevoEstudiante;
    }

    /**
     * Instancia un nuevo Socio Docente y lo agrega a la colección si no existe su DNI.
     * 
     * @param p_dniSocio DNI del docente.
     * @param p_nombre Nombre y apellido.
     * @param p_area Área de desempeño.
     * @return El Docente creado, o null si el DNI ya existía.
     */
    public Docente nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area) {
        if (this.buscarSocio(p_dniSocio) != null) {
            return null; 
        }
        Docente nuevoDocente = new Docente(p_dniSocio, p_nombre, p_area);
        this.getSocios().add(nuevoDocente);
        return nuevoDocente;
    }

    // --- MÉTODOS DE GESTIÓN DE PRÉSTAMOS ---

    /**
     * Crea el préstamo y lo agrega en el libro y el socio.
     * 
     * @param p_fechaRetiro Fecha en la que se retira el libro.
     * @param p_socio Socio que realiza el retiro.
     * @param p_libro Libro físico a prestar.
     * @return true si el préstamo se efectuó correctamente (socio habilitado y libro disponible).
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        if (!(p_libro.prestado()) && p_socio.puedePedir()) {
            Prestamo unPrestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);
            p_libro.agregarPrestamo(unPrestamo);
            p_socio.addPrestamo(unPrestamo);
            return true;
        }
        return false;
    }

    /**
     * Asigna la fecha de devolución del préstamo con la fecha actual.
     * 
     * @param p_libro El libro físico que se devuelve.
     * @throws LibroNoPrestadoException si el libro no se encuentra prestado.
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + p_libro.getTitulo() + " no se puede devolver ya que se encuentra en la biblioteca");
        }
        Prestamo unPrestamo = p_libro.ultimoPrestamo();
        unPrestamo.registrarFechaDevolucion(Calendar.getInstance());
    }

    /**
     * Devuelve el nombre del Socio que tiene el libro físico indicado.
     * 
     * @param p_libro Libro físico consultado.
     * @return String con el nombre del socio.
     * @throws LibroNoPrestadoException si el libro no está prestado.
     */
    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }
        return p_libro.ultimoPrestamo().getSocio().getNombre();
    }

    // --- MÉTODOS DE REPORTES Y BÚSQUEDA ---

    /**
     * Devuelve la cantidad de socios del tipo pasado como parámetro (ej. "Estudiante").
     * 
     * @param p_tipo String con el tipo de socio a contar.
     * @return int cantidad de socios encontrados.
     */
    public int cantidadDeSociosPorTipo(String p_tipo) {
        int contador = 0;
        for (Socio unSocio : this.getSocios()) { 
            if (unSocio.soyDeLaClase().equalsIgnoreCase(p_tipo)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Devuelve una colección con los préstamos vencidos al día de la fecha.
     * 
     * @return ArrayList con los préstamos vencidos.
     */
    public ArrayList<Prestamo> prestamosVencidos() {
        Calendar hoy = Calendar.getInstance();
        ArrayList<Prestamo> vencidos = new ArrayList<>();
        
        for (Socio unSocio : this.getSocios()) {
            for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                if (unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(hoy)) {
                    vencidos.add(unPrestamo);
                }
            }
        }
        return vencidos;
    }   

    /**
     * Devuelve una colección con los docentes responsables.
     * 
     * @return ArrayList de docentes responsables.
     */
    public ArrayList<Docente> docentesResponsables() {
        ArrayList<Docente> responsables = new ArrayList<>();
        for (Socio unSocio : this.getSocios()) {
            if (unSocio instanceof Docente && ((Docente)unSocio).esResponsable()) {
                responsables.add((Docente) unSocio);
            }
        }
        return responsables;
    }
    
    /**
     * Devuelve el Socio que tiene el dni pasado como parámetro.
     * 
     * @param p_dni Documento único del socio.
     * @return El objeto Socio encontrado, o null si no lo encuentra.
     */
    public Socio buscarSocio(int p_dni) {
        for (Socio unSocio : this.getSocios()) {
            if (unSocio.getDniSocio() == p_dni) {
                return unSocio; 
            }
        }
        return null;
    }

    /**
     * Búsqueda física exacta: Devuelve el Libro según su ID numérico.
     * Método indispensable para vincular la base de datos de ejemplares físicos.
     * 
     * @param p_idLibro ID físico del libro en la BD.
     * @return El objeto Libro encontrado, o null si no lo encuentra.
     */
    public Libro buscarLibroPorId(int p_idLibro) {
        for (Libro unLibro : this.getLibros()) {
            if (unLibro.getIdLibro() == p_idLibro) {
                return unLibro; 
            }
        }
        return null;
    }

    // --- MÉTODOS DE FORMATO STRING ---

    /**
     * Devuelve un String detallando a los socios con el Formato 1.
     * @return Listado formateado.
     */
    public String listaDeSocios() {
        StringBuilder sb = new StringBuilder("Lista de Socios:\n");
        int numero = 1; 
        for (Socio unSocio : this.getSocios()) {
            sb.append(numero).append(") ").append(unSocio.toString()).append("\n");
            numero++; 
        }
        return sb.toString();
    }

    /**
     * Devuelve un String detallando a los libros con el Formato 2.
     * @return Listado formateado de todos los ejemplares físicos.
     */
    public String listaDeLibros() {
        StringBuilder sb = new StringBuilder("Lista de Libros:\n");
        int numero = 1; 
        for (Libro unLibro : this.getLibros()) {
            String estado = unLibro.prestado() ? "Si" : "No";
            sb.append(numero).append(") Titulo: ").append(unLibro.getTitulo())
              .append(" || Prestado: (").append(estado).append(")\n");
            numero++; 
        }
        return sb.toString();
    }

    /**
     * Devuelve un String con la lista de los títulos con los que cuenta la Biblioteca.
     * Cumple la directiva: Sin repetir, en caso de haber más de 1 ejemplar.
     * 
     * @return String con títulos únicos.
     */
    public String listaDeTitulos() {
        HashSet<String> titulosUnicos = new HashSet<>();
        for (Libro l : this.getLibros()) {
            titulosUnicos.add(l.getTitulo());
        }
        
        StringBuilder sb = new StringBuilder();
        for (String titulo : titulosUnicos) {
            sb.append(titulo).append("\n");
        }
        return sb.toString();
    }   

    /**
     * Devuelve un String listando a los docentes responsables con el Formato 3.
     * @return Listado formateado.
     */
    public String listaDeDocentesResponsables() {
        ArrayList<Docente> responsables = this.docentesResponsables();
        StringBuilder sb = new StringBuilder("Lista de Docentes Responsables:\n\n"); 
        
        for (Docente d : responsables) {
            sb.append("* ").append(d.toString()).append("\n");
        }
        return sb.toString();
    }
}
