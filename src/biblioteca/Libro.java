package biblioteca;

import java.util.ArrayList;

/**
 * Clase Libro - Representa un ejemplar físico de la biblioteca y gestiona su historial de préstamos.
 * 
 * @author Nicolás Javier Mancedo (Adaptado para BD con idLibro por Equipo BookSystem)
 * @version 4.0
 * @since 31/10/2026
 */
public class Libro {
    
    // --- ATRIBUTOS ---
    private int idLibro;
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;
    
    /**
     * Constructor 1: Inicializa el libro creando una lista de préstamos vacía.
     * 
     * @param p_idLibro Identificador único del ejemplar.
     * @param p_titulo Título de la obra.
     * @param p_edicion Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación.
     */
    public Libro(int p_idLibro, String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setIdLibro(p_idLibro);
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        // Regla del TP4: Constructor inicializa colección vacía[cite: 8]
        this.setPrestamos(new ArrayList<Prestamo>());
    }
    
    /**
     * Constructor 2: Inicializa el libro recibiendo una colección de préstamos ya existente.
     * 
     * @param p_idLibro Identificador único del ejemplar.
     * @param p_titulo Título de la obra.
     * @param p_edicion Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación.
     * @param p_prestamos Colección de préstamos del libro.
     */
    public Libro(int p_idLibro, String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos){
        this.setIdLibro(p_idLibro);
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }
    
    /**
     * Constructor 3: Inicializa el libro creando la lista y agregando un primer préstamo individual.
     * 
     * @param p_idLibro Identificador único del ejemplar.
     * @param p_titulo Título de la obra.
     * @param p_edicion Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación.
     * @param p_prestamo Primer préstamo a registrar.
     */
    public Libro(int p_idLibro, String p_titulo, int p_edicion, String p_editorial, int p_anio, Prestamo p_prestamo){
        this.setIdLibro(p_idLibro);
        this.setTitulo(p_titulo); 
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
        this.agregarPrestamo(p_prestamo);
    }
    
    // --- SETTERS PRIVADOS (Doble Encapsulamiento) ---
    // Encapsulamiento estricto, tal como lo establece el Tema 2[cite: 14]
    
    private void setIdLibro(int p_idLibro) {
        this.idLibro = p_idLibro;
    }

    private void setTitulo(String p_titulo) {
        this.titulo = p_titulo;
    }
    
    private void setEdicion(int p_edicion) {
        this.edicion = p_edicion;
    }
    
    private void setEditorial(String p_editorial) {
        this.editorial = p_editorial;
    }
    
    private void setAnio(int p_anio) {
        this.anio = p_anio;
    }
    
    private void setPrestamos(ArrayList<Prestamo> p_prestamos) {
        this.prestamos = p_prestamos;
    }
    
    // --- GETTERS PÚBLICOS ---
    
    public int getIdLibro() {
        return this.idLibro;
    }

    public String getTitulo() {
        return this.titulo;
    }
    
    public int getEdicion() {
        return this.edicion;
    }
    
    public String getEditorial() {
        return this.editorial;
    }
    
    public int getAnio() {
        return this.anio;
    }
    
    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    // --- MÉTODOS DE COMPORTAMIENTO (Gestión de la colección) ---

    /**
     * Añade una ficha de préstamo a la colección del libro.
     * @param p_prestamo El préstamo a registrar.
     * @return true si se agregó correctamente a la colección.
     */
    public boolean agregarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().add(p_prestamo);
    }
    
    /**
     * Elimina un préstamo de la colección.
     * @param p_prestamo El préstamo a eliminar.
     * @return true si fue eliminado con éxito.
     */
    public boolean quitarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().remove(p_prestamo);
    }

    // --- MÉTODOS DEL DOMINIO (Estrictamente los del UML del TP Integrador) ---

    /**
     * Retorna el último préstamo registrado en el libro.
     * @return El último objeto Prestamo, o null si nunca fue prestado.
     */
    public Prestamo ultimoPrestamo() {
        if (this.getPrestamos().isEmpty()) {
            return null;
        }
        return this.getPrestamos().get(this.getPrestamos().size() - 1);
    }

    /**
     * Verifica si el libro se encuentra prestado actualmente revisando si la última
     * fecha de devolución está vacía.
     * @return true si está prestado, false en caso contrario.
     */
    public boolean prestado() {
        Prestamo ultimo = this.ultimoPrestamo();
        if (ultimo == null) {
            return false; 
        }
        return ultimo.getFechaDevolucion() == null;
    }

    /**
     * Retorna una representación en texto del libro.
     * @return String con el formato del título.
     */
    @Override
    public String toString() {
        return "Titulo: " + this.getTitulo();
    }
}
