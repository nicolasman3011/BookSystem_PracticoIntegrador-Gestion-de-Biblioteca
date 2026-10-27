package biblioteca;
import java.util.ArrayList;

/**
 * Clase abstracta que representa a un Socio de la biblioteca (Estudiante o Docente).
 * Centraliza los datos comunes y delega comportamiento específico a las subclases.
 */
public abstract class Socio {
    private int dniSocio;
    private String nombre;
    private int diasPrestamo;
    private ArrayList<Prestamo> prestamos; // Relación 0..* con Prestamo

    /**
     * Constructor 1 (Caso 0): Inicializa atributos básicos y crea la colección de préstamos vacía.
     * @param p_dniSocio DNI del socio.
     * @param p_nombre Nombre y apellido del socio.
     * @param p_diasPrestamo Cantidad de días asignados para el préstamo.
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo) {
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.setPrestamos(new ArrayList<Prestamo>()); // Colección inicializada vacía
    }

    /**
     * Constructor 2: Recibe los atributos básicos y la colección de préstamos instanciada.
     * @param p_dniSocio DNI del socio.
     * @param p_nombre Nombre y apellido del socio.
     * @param p_diasPrestamo Cantidad de días asignados para el préstamo.
     * @param p_prestamos Colección de préstamos asociados al socio.
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo, ArrayList<Prestamo> p_prestamos) {
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.setPrestamos(p_prestamos);
    }

    // Mutadores (Setters)
    private void setDniSocio(int p_dniSocio) {
        this.dniSocio = p_dniSocio;
    }

    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    /**
     * IMPORTANTE: Se declara 'protected' (o public) para que la clase hija 'Docente' 
     * pueda acceder a este mutador en su método 'cambiarDiasDePrestamo(int)'.
     */
    protected void setDiasPrestamo(int p_diasPrestamo) {
        this.diasPrestamo = p_diasPrestamo;
    }

    private void setPrestamos(ArrayList<Prestamo> p_prestamos) {
        this.prestamos = p_prestamos;
    }

    // Observadores (Getters)
    public int getDniSocio() {
        return this.dniSocio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getDiasPrestamo() {
        return this.diasPrestamo;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    // Métodos para la colección (Necesarios para la clase Biblioteca)
    public boolean addPrestamo(Prestamo p_prestamo) {
        return this.getPrestamos().add(p_prestamo);
    }

    public boolean removePrestamo(Prestamo p_prestamo) {
        return this.getPrestamos().remove(p_prestamo);
    }

    // Métodos Abstractos
    public abstract boolean puedePedir();
    public abstract String soyDeLaClase();

    // Lógica de negocio
    /**
     * Calcula la cantidad de libros que actualmente están en poder del socio.
     * @return Cantidad de préstamos activos (sin fecha de devolución).
     */
    public int cantLibrosPrestados() {
        int cantidad = 0;
        // Bucle que recorre el historial buscando préstamos activos
        for (Prestamo prestamo : this.getPrestamos()) {
            if (prestamo.getFechaDevolucion() == null) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /**
     * Retorna una representación en formato texto del socio.
     * @return String con el formato requerido.
     */
    public String toString() {
        // Se aplica enlace dinámico (polimorfismo) al invocar this.soyDeLaClase()
        return "D.N.I.: " + this.getDniSocio() + " || " + this.getNombre() + 
               " (" + this.soyDeLaClase() + ") || Libros Prestados: " + this.cantLibrosPrestados();
    }
}
