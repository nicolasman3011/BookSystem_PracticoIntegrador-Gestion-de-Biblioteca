package biblioteca;
import java.util.Calendar;
import java.text.SimpleDateFormat; // Importamos para formatear fecha

/**
 * Representa la transacción entre un Socio y un Libro.
 */
public class Prestamo {
    private Calendar fechaRetiro;
    private Calendar fechaDevolucion;
    private Socio socio;   
    private Libro libro;   

    public Prestamo(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        this.setFechaRetiro(p_fechaRetiro);
        this.setSocio(p_socio);
        this.setLibro(p_libro);
        this.setFechaDevolucion(null); 
    }

    // --- MUTADORES (Setters privados) ---
    private void setFechaRetiro(Calendar p_fechaRetiro) {
      this.fechaRetiro = p_fechaRetiro; 
    }
  
    private void setFechaDevolucion(Calendar p_fechaDevolucion) { 
      this.fechaDevolucion = p_fechaDevolucion; 
    }
  
    private void setSocio(Socio p_socio) { 
      this.socio = p_socio; 
    }
  
    private void setLibro(Libro p_libro) { 
      this.libro = p_libro; 
    }

    // --- OBSERVADORES (Getters públicos) ---
    public Calendar getFechaRetiro() { 
      return this.fechaRetiro; 
    }
  
    public Calendar getFechaDevolucion() { 
      return this.fechaDevolucion; 
    }
  
    public Socio getSocio() { 
      return this.socio; 
    }
  
    public Libro getLibro() { 
      return this.libro; 
    }

    // --- METODOS ---
    
    public void registrarFechaDevolucion(Calendar p_fecha) {
        this.setFechaDevolucion(p_fecha);
    }

    public boolean vencido(Calendar p_fecha) {
        Calendar fechaVencimiento = (Calendar) this.getFechaRetiro().clone();
        fechaVencimiento.add(Calendar.DAY_OF_YEAR, this.getSocio().getDiasPrestamo());
        return p_fecha.after(fechaVencimiento);
    }

    /**
     * Imprime el formato exacto requerido resolviendo el formateo internamente.
     */
    public String toString() {
        // Instanciamos el formateador con el patrón que pide el enunciado (yyyy/MM/dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        
        // Formateamos la fecha de retiro extrayendo el objeto Date interno de Calendar
        String fRetiro = sdf.format(this.getFechaRetiro().getTime());
        
        // Validamos la fecha de devolución (ya que inicialmente es null)
        String fDevolucion = "Pendiente";
        if (this.getFechaDevolucion() != null) {
            fDevolucion = sdf.format(this.getFechaDevolucion().getTime());
        }
        
        return "Retiro: " + fRetiro + 
               " - Devolución: " + fDevolucion + 
               "\nLibro: " + this.getLibro().getTitulo() + 
               "\nSocio: " + this.getSocio().getNombre();
    }
}
