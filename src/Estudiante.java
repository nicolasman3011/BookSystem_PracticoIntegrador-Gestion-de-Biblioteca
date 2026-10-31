 package biblioteca;

import java.util.ArrayList;

public class Estudiante extends Socio {
    
    private String carrera;
    
    /**
     * Constructor 1: Sin préstamos iniciales.
     * Se asignan 20 días de préstamo fijos.
     */
    public Estudiante(int p_DniSocio, String p_nombre, String p_carrera){
        super(p_DniSocio, p_nombre, 20);
        this.setCarrera(p_carrera);
    }
    
    /**
     * Constructor 2: Con colección de préstamos preexistente.
     * Se asignan 20 días de préstamo fijos.
     */
    public Estudiante(int p_DniSocio, String p_nombre, String p_carrera, ArrayList<Prestamo> p_prestamos){
        super(p_DniSocio, p_nombre, 20, p_prestamos);
        this.setCarrera(p_carrera);
    }
    
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }
    
    public String getCarrera(){
        return this.carrera;
    }
    
    /**
     * Un estudiante puede pedir prestado un libro sólo si no tiene más de 3 libros 
     * en su poder y si no tiene ningún libro vencido.
     */
    @Override
    public boolean puedePedir(){
          return super.puedePedir() && super.cantLibrosPrestados() < 3;
    }
    
    @Override
    public String soyDeLaClase(){
        return "Estudiante";
    }
}
