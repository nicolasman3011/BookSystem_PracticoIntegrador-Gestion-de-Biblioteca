 

import java.util.Calendar;
import java.util.ArrayList;

public class Docente extends Socio { 
      private String area;
      
      /**
       * Constructor 1: Crea un Docente sin préstamos iniciales (colección vacía).
       * Se asignan 5 días de préstamo fijos iniciales por requerimiento del sistema.
       */
       public Docente(int p_DniSocio, String p_nombre, String p_area){
          super(p_DniSocio, p_nombre, 5); 
          this.setArea(p_area);
      }

      /**
       * Constructor 2: Crea un Docente recibiendo una colección de préstamos preexistente.
       * Se asignan 5 días de préstamo fijos iniciales por requerimiento del sistema.
       */
       public Docente(int p_DniSocio, String p_nombre, String p_area, ArrayList<Prestamo> p_prestamos){
          super(p_DniSocio, p_nombre, 5, p_prestamos); 
          this.setArea(p_area);
      }
      
      public void setArea(String p_area) {
          this.area = p_area;
      }

      public String getArea() {
          return this.area;
      }
      
      /**
       * Devuelve true si el Docente nunca tuvo ni tiene un préstamo vencido.
       */
       public boolean esResponsable(){
           Calendar hoy = Calendar.getInstance();
           for(Prestamo unPrestamo : super.getPrestamos()){
               if (unPrestamo.getFechaDevolucion() != null) {
                   // Evalúa si lo devolvió tarde en el pasado
                   if (unPrestamo.vencido(unPrestamo.getFechaDevolucion())) {
                       return false;
                   }
               } else {
                   // Evalúa si actualmente lo tiene vencido
                   if (unPrestamo.vencido(hoy)) {
                       return false;
                   }
               }
           }
           return true; 
       }
       
      /**
       * Modifica la cantidad de días de préstamo del docente.
       */
       public void cambiarDiasDePrestamo(int p_dias){
           super.setDiasPrestamo(super.getDiasPrestamo() + p_dias);
       }
       
      /**
       * Verifica si el docente puede pedir un nuevo préstamo.
       * Solo puede pedir si no tiene alguna devolución vencida actualmente.
       */
      @Override
       public boolean puedePedir(){
           Calendar hoy = Calendar.getInstance();
           for(Prestamo unPrestamo : super.getPrestamos()){
               //  evalúan los libros que el docente todavía tiene en su poder
               if(unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(hoy)){
                   return false;
               }
           }
           return true; 
       }
       
      @Override
      public String soyDeLaClase(){
           return "Docente";
       }
}
