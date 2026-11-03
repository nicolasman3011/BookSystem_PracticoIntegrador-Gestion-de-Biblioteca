 import package biblioteca;

import java.util.ArrayList;

/**
 * Clase Libro - Representa un ejemplar físico de la biblioteca y gestiona su historial de préstamos.
 * 
 * @author Nicolás Javier Mancedo 
 * @version 1.0
 * @since 31/10/2026
 * 
 * Historial de cambios:
 * - Implementación de la estructura de 3 constructores para relación 0..*.
 * - Encapsulamiento estricto con setters privados.
 */
public class Libro {
    
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos; // Relación 0..* según diagrama UML
    
    /**
     * Constructor 1: Inicializa el libro creando una lista de préstamos vacía.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
    }
    
    /**
     * Constructor 2: Inicializa el libro recibiendo una colección de préstamos ya existente.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }
    
    /**
     * Constructor 3: Inicializa el libro creando la lista y agregando un primer préstamo individual.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, Prestamo p_prestamo){
        this.setTitulo(p_titulo); 
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
        this.agregarPrestamo(p_prestamo);
    }
    
    // --- MÉTODOS DE COMPORTAMIENTO Y MUTADORES PRIVADOS ---
    
    /**
     * Añade una ficha de préstamo a la colección del libro.
     * @param p_prestamo El préstamo a registrar.
     * @return true si se agregó correctamente a la colección.
     */
    public boolean agregarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().add(p_prestamo);
    }
    
    public boolean quitarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().remove(p_prestamo);
    }
    
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }
    
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }
    
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }
    
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }
    
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }
    
    // --- GETTERS PÚBLICOS ---
    
    public String getTitulo(){
        return this.titulo;
    }
    
    public int getEdicion(){
        return this.edicion;
    }
    
    public String getEditorial(){
        return this.editorial;
    }
    
    public int getAnio(){
        return this.anio;
    }
    
    public ArrayList<Prestamo> getPrestamos(){
        return this.prestamos;
    }
    
    // --- MÉTODOS DEL DOMINIO (UML) ---

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
     */
    @Override
    public String toString() {
        return "Titulo: " + this.getTitulo();
    }
}

