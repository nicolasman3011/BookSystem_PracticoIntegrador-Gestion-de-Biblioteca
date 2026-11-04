import java.util.*;

/**
 * Práctico Integrador - Gestión de Biblioteca
 * Clase Biblioteca: Administra la colección de libros, socios y los préstamos otorgados.
 * 
 * @author Ponce Silvana Beatriz, Nacimiento Nestor
 * @version 1.0
 */
    public class Biblioteca {
    private String nombre;
    private ArrayList<Libro> libros;
    private ArrayList<Socio> socios;
     
    /**
     * Constructor sobrecargado. Instancia una Biblioteca a partir de un nombre y listas existentes.
     * 
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socios Lista de socios.
     * @param p_libros Lista de libros.
     */
    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(p_libros);
    }
    
    /**
     * Constructor sobrecargado. Instancia una Biblioteca con un primer socio y un primer libro.
     * 
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socio  Primer socio a incorporar.
     * @param p_libro  Primer libro a incorporar.
     */
    public Biblioteca(String p_nombre, Socio p_socio, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.getSocios().add(p_socio);
        this.setLibros(new ArrayList<Libro>());
        this.getLibros().add(p_libro);
    }

    /**
     * Constructor sobrecargado. Instancia una Biblioteca con colecciones vacías.
     * 
     * @param p_nombre Nombre de la biblioteca.
     */
    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
    }  

    /**
     * Setter privado. Asigna el nombre de la biblioteca.
     * @param p_nombre Nombre de la biblioteca.
     */
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }
    
    /**
     * Setter privado. Asigna la lista de libros.
     * @param p_libros ArrayList de libros.
     */
    private void setLibros(ArrayList<Libro> p_libros) {
        this.libros = p_libros;
    }
    
    /**
     * Setter privado. Asigna la lista de socios.
     * @param p_socios ArrayList de socios.
     */
    private void setSocios(ArrayList<Socio> p_socios) {
        this.socios = p_socios;
    }

    /**
     * Getter público. Devuelve el nombre de la biblioteca.
     * @return String con el nombre.
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Getter público. Devuelve la lista de libros.
     * @return ArrayList de libros.
     */
    public ArrayList<Libro> getLibros() {
        return this.libros;
    }
    
    /**
     * Getter público. Devuelve la lista de socios.
     * @return ArrayList de socios.
     */
    public ArrayList<Socio> getSocios() {
        return this.socios;
    }

    /**
     * Agrega un nuevo socio estudiante a la colección si no existe previamente por DNI.
     * 
     * @param p_dniSocio DNI del estudiante.
     * @param p_nombre   Nombre y apellido.
     * @param p_carrera  Carrera que cursa.
     */
    public void nuevosSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        if (this.getSocios() == null) {
            return; 
        }
        
        for (Socio unSocio : this.getSocios()) {
            if (unSocio.getDniSocio() == p_dniSocio) {
                return; 
            }
        }
        
        this.getSocios().add(new Estudiante(p_dniSocio, p_nombre, p_carrera));
    }

    /**
     * Agrega un nuevo socio docente a la colección si no existe previamente por DNI.
     * 
     * @param p_dniSocio DNI del docente.
     * @param p_nombre   Nombre y apellido.
     * @param p_area     Área de desempeño.
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area) {
        if (this.getSocios() == null) {
            return; 
        }
        
        for (Socio unSocio : this.getSocios()) {
            if (unSocio.getDniSocio() == p_dniSocio) {
                return; 
            }
        }
        
        this.getSocios().add(new Docente(p_dniSocio, p_nombre, p_area));
    }

    /**
     * Elimina un socio de la colección de la biblioteca.
     * 
     * @param p_socio Socio a remover.
     * @return boolean true si fue eliminado con éxito.
     */
    boolean borrarSocio(Socio p_socio) {
        if (this.getSocios() == null || this.getSocios().isEmpty()) {
            return false;
        }
        return this.getSocios().remove(p_socio);
    }

    /**
     * Agrega un nuevo libro a la biblioteca.
     * 
     * @param p_titulo    Título del libro.
     * @param p_edicion   Número de edición.
     * @param p_editorial Editorial.
     * @param p_anio      Año de publicación.
     * @return boolean    true si se añadió correctamente.
     */
    public boolean nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        return this.getLibros().add(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    /**
     * Elimina un libro de la colección de la biblioteca.
     * 
     * @param p_libro Libro a remover.
     * @return boolean true si fue eliminado con éxito.
     */
    boolean borrarLibros(Libro p_libro) {
        if (this.getLibros() == null || this.getLibros().isEmpty()) {
            return false;
        }
        return this.getLibros().remove(p_libro);
    }

    /**
     * Registra un préstamo de libro si el libro no está prestado y el socio cumple las condiciones.
     * 
     * @param p_fechaRetiro Fecha de retiro del préstamo.
     * @param p_socio       Socio solicitante.
     * @param p_libro       Libro a prestar.
     * @return boolean      true si el préstamo fue exitoso.
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
     * Registra la devolución de un libro prestado.
     * 
     * @param libro Libro que se devuelve.
     * @throws LibroNoPrestadoException si el libro no se encontraba prestado.
     */
    public void devolverLibro(Libro libro) throws LibroNoPrestadoException {
        if (!libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + libro.getTitulo() + " no registra ningún préstamo activo en este momento.");
        }
        Prestamo unPrestamo = libro.ultimoPrestamo();
        unPrestamo.registrarFechaDevolucion(Calendar.getInstance());
    }

    /**
     * Devuelve la cantidad de socios que pertenecen a un tipo específico (ej. "Estudiante" o "Docente").
     * 
     * @param tipo Tipo de socio a filtrar.
     * @return int cantidad de socios encontrados.
     */
    public int cantidadDeSociosPorTipo(String tipo) {
        int contador = 0;
        for (Socio unSocio : this.getSocios()) { 
            if (unSocio.soyDeLaClase().equalsIgnoreCase(tipo)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Obtiene una colección con todos los préstamos vencidos a la fecha actual.
     * 
     * @return ArrayList de préstamos vencidos.
     */
    public ArrayList<Prestamo> prestamosVencidos() {
        Calendar hoy = Calendar.getInstance();
        ArrayList<Prestamo> vencidos = new ArrayList<>();
        
        for (Socio unSocio : this.socios) {
            for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                if (unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(hoy)) {
                    vencidos.add(unPrestamo);
                }
            }
        }
        return vencidos;
    }    

    /**
     * Obtiene una lista de los docentes considerados responsables.
     * 
     * @return ArrayList de docentes responsables.
     */
    public ArrayList<Docente> docentesResponsables() {
        ArrayList<Docente> responsables = new ArrayList<>();
        
        for (Socio unSocio : this.socios) {
            if (unSocio instanceof Docente && ((Docente)unSocio).esResponsable()) {
                responsables.add((Docente) unSocio);
            }
        }
        return responsables;
    }
    
    /**
     * Devuelve el nombre del socio que tiene en su poder un libro determinado.
     * 
     * @param libro Libro consultado.
     * @return String con el nombre del socio.
     * @throws LibroNoPrestadoException si el libro no está prestado.
     */
    public String quienTieneElLibro(Libro libro) throws LibroNoPrestadoException {
        if (!libro.prestado()) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }
        return libro.ultimoPrestamo().getSocio().getNombre();
    }
    
    /**
     * Genera un listado en formato de texto con todos los socios y cantidades.
     * 
     * @return String con el formato de listado de socios.
     */
    public String listaDeSocios() {
        StringBuilder sb = new StringBuilder("Lista de Socios:\n");
        int numero = 1; 
        
        for (Socio unSocio : this.socios) {
            sb.append(numero).append(") ").append(unSocio.toString()).append("\n");
            numero++; 
        }
        
        sb.append("****************************************\n");
        sb.append("Cantidad de Socios del tipo Estudiante: ").append(cantidadDeSociosPorTipo("Estudiante")).append("\n");
        sb.append("Cantidad de Socios del tipo Docente: ").append(cantidadDeSociosPorTipo("Docente")).append("\n");
        sb.append("****************************************\n");
        
        return sb.toString();
    }

    /**
     * Genera una lista con los títulos únicos con los que cuenta la biblioteca (sin repetir).
     * 
     * @return String con los títulos únicos.
     */
    public String listaDeTitulos() {
        HashSet<String> titulosUnicos = new HashSet<>();
        
        for (Libro l : this.libros) {
            titulosUnicos.add(l.getTitulo());
        }
        
        StringBuilder sb = new StringBuilder();
        for (String titulo : titulosUnicos) {
            sb.append(titulo).append("\n");
        }
        
        return sb.toString();
    }    
    
    /**
     * Busca y devuelve un socio según su número de DNI.
     * 
     * @param dni DNI a buscar.
     * @return Socio encontrado o null si no existe.
     */
    public Socio buscarSocio(int dni) {
        for (Socio unSocio : this.socios) {
            if (unSocio.getDniSocio() == dni) {
                return unSocio; 
            }
        }
        
        System.out.println("No se encontró ningún socio registrado con el DNI: " + dni);
        return null;
    }

    /**
     * Genera un listado en formato de texto con los docentes responsables.
     * 
     * @return String con el listado de docentes responsables.
     */
    public String listaDeDocentesResponsables() {
        ArrayList<Docente> responsables = docentesResponsables();
        StringBuilder sb = new StringBuilder("Lista de Docentes Responsables:\n\n"); 
        
        for (Docente d : responsables) {
            sb.append("* ").append(d.toString()).append("\n");
        }
        
        return sb.toString();
    }
}