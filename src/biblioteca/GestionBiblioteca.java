package biblioteca;

import java.util.Calendar;
import java.util.Scanner;

/**
 * Clase ejecutable principal para la gestión de la biblioteca.
 * Contiene el punto de entrada (main) y un menú interactivo por consola
 * para instanciar y probar todas las funcionalidades requeridas por el TP7.
 * 
 * @author Grupo Completo - TP7 Integrador
 * @version 1.0
 */
public class GestionBiblioteca {

  /**
   * Método principal de ejecución.
   * Instancia la biblioteca con datos iniciales de prueba y despliega el menú de opciones.
   * 
   * @param args Argumentos de la línea de comandos.
   */
  public static void main(String[] args) {
    Scanner teclado = new Scanner(System.in);
    
    // 1. Instanciamos la biblioteca central
    Biblioteca miBiblioteca = new Biblioteca("Biblioteca Central UNNE");
    
    // 2. Carga inicial de datos de prueba para verificar las consultas del TP7
    cargarDatosIniciales(miBiblioteca);

    int opcion = -1;

    do {
      mostrarMenu();
      System.out.print("Ingrese una opción: ");
      
      try {
        opcion = Integer.parseInt(teclado.nextLine());
      } catch (NumberFormatException e) {
        opcion = -1;
      }

      System.out.println("\n--------------------------------------------------");
      switch (opcion) {
        case 1:
          // ¿Qué cantidad de socios de tipo Estudiante hay?
          int cantEstudiantes = miBiblioteca.cantidadDeSociosPorTipo("Estudiante");
          System.out.println("Cantidad de socios de tipo Estudiante: " + cantEstudiantes);
          break;

        case 2:
          // ¿Cuál es la lista de docentes que nunca han adeudado ni adeudan libros?
          System.out.println(miBiblioteca.listaDeDocentesResponsables());
          break;

        case 3:
          // ¿Cuál es la lista de libros? ¿Y la de socios?
          System.out.println(miBiblioteca.listaDeSocios());
          System.out.println();
          System.out.println(miBiblioteca.listaDeLibros());
          break;

        case 4:
          // ¿Qué socio tiene prestado un libro determinado?
          consultarQuienTieneLibro(miBiblioteca, teclado);
          break;

        case 5:
          // Registrar nuevo préstamo interactivo
          registrarNuevoPrestamo(miBiblioteca, teclado);
          break;

        case 6:
          // Registrar devolución interactiva (Prueba de captura de LibroNoPrestadoException)
          registrarDevolucionLibro(miBiblioteca, teclado);
          break;

        case 7:
          // Mostrar lista de títulos sin repetir
          System.out.println(miBiblioteca.listaDeTitulos());
          break;

        case 8:
          // Mostrar préstamos vencidos a la fecha
          mostrarPrestamosVencidos(miBiblioteca);
          break;

        case 9:
          // Agregar un nuevo socio (Estudiante o Docente)
          agregarNuevoSocio(miBiblioteca, teclado);
          break;

        case 10:
          // Agregar un nuevo libro
          agregarNuevoLibro(miBiblioteca, teclado);
          break;

        case 0:
          System.out.println("Saliendo del sistema de gestión de biblioteca...");
          break;

        default:
          System.out.println("Opción inválida. Ingrese un número correspondiente al menú.");
          break;
      }
      System.out.println("--------------------------------------------------\n");

    } while (opcion != 0);

    teclado.close();
  }

  /**
   * Despliega las opciones del menú en la consola.
   */
  private static void mostrarMenu() {
    System.out.println("==================================================");
    System.out.println("         MENÚ DE GESTIÓN DE BIBLIOTECA - TP7      ");
    System.out.println("==================================================");
    System.out.println("1) Consultar cantidad de socios tipo Estudiante");
    System.out.println("2) Listar docentes responsables (sin deudas)");
    System.out.println("3) Mostrar listado completo de Socios y Libros");
    System.out.println("4) Consultar quién tiene un libro en préstamo");
    System.out.println("5) Registrar el préstamo de un libro");
    System.out.println("6) Registrar la devolución de un libro");
    System.out.println("7) Listar títulos de la biblioteca (sin repetir)");
    System.out.println("8) Listar préstamos vencidos");
    System.out.println("9) Dar de alta un nuevo socio");
    System.out.println("10) Dar de alta un nuevo libro");
    System.out.println("0) Salir");
    System.out.println("==================================================");
  }

  /**
   * Carga inicial automática de socios y libros para permitir probar el sistema al instante.
   */
  private static void cargarDatosIniciales(Biblioteca p_biblioteca) {
    // Carga de libros
    p_biblioteca.nuevoLibro("JAVA. Como Programar", 10, "Pearson", 2016);
    p_biblioteca.nuevoLibro("Longman. Diccionario Pocket", 2, "Longman", 2018);
    p_biblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);
    p_biblioteca.nuevoLibro("JAVA. Como Programar", 11, "Pearson", 2020); // Segundo ejemplar del mismo título

    // Carga de socios docentes
    p_biblioteca.nuevoSocioDocente(14524782, "Juan Perez", "Sistemas");
    p_biblioteca.nuevoSocioDocente(17982110, "Juan Fernández", "Matemática");
    p_biblioteca.nuevoSocioDocente(10912002, "María Alegre", "Física");

    // Carga de socios estudiantes
    p_biblioteca.nuevoSocioEstudiante(28987498, "Francisco Paenza", "Lic. en Sistemas");
    p_biblioteca.nuevoSocioEstudiante(31987123, "Cesar Milstein", "Ing. en Informática");

    // Simulación de un préstamo inicial activo para probar la consulta de 'quienTieneElLibro'
    Socio docente1 = p_biblioteca.buscarSocio(14524782);
    Libro libro1 = p_biblioteca.getLibros().get(0); // "JAVA. Como Programar"

    if (docente1 != null && libro1 != null) {
      Calendar fechaRetiro = Calendar.getInstance();
      p_biblioteca.prestarLibro(fechaRetiro, docente1, libro1);
    }
  }

  /**
   * Permite consultar qué socio tiene en su poder un libro especificado.
   * Captura la excepción LibroNoPrestadoException si el ejemplar está en la biblioteca.
   */
  private static void consultarQuienTieneLibro(Biblioteca p_biblioteca, Scanner p_teclado) {
    System.out.print("Ingrese el título del libro a consultar: ");
    String tituloIngresado = p_teclado.nextLine();

    Libro libroEncontrado = null;
    for (Libro l : p_biblioteca.getLibros()) {
      if (l.getTitulo().equalsIgnoreCase(tituloIngresado)) {
        libroEncontrado = l;
        break;
      }
    }

    if (libroEncontrado == null) {
      System.out.println("El libro '" + tituloIngresado + "' no existe en el catálogo de la biblioteca.");
      return;
    }

    try {
      String nombreSocio = p_biblioteca.quienTieneElLibro(libroEncontrado);
      System.out.println("El libro '" + libroEncontrado.getTitulo() + "' se encuentra prestado a: " + nombreSocio);
    } catch (LibroNoPrestadoException e) {
      System.out.println("Respuesta del sistema: " + e.getMessage());
    }
  }

  /**
   * Registra un nuevo préstamo validando que el socio pueda pedir y que el libro esté disponible.
   */
  private static void registrarNuevoPrestamo(Biblioteca p_biblioteca, Scanner p_teclado) {
    System.out.print("Ingrese el DNI del socio solicitante: ");
    int dni = Integer.parseInt(p_teclado.nextLine());

    Socio socio = p_biblioteca.buscarSocio(dni);
    if (socio == null) {
      System.out.println("No se encontró ningún socio registrado con el DNI: " + dni);
      return;
    }

    System.out.print("Ingrese el título del libro a prestar: ");
    String titulo = p_teclado.nextLine();

    Libro libroAPrestar = null;
    for (Libro l : p_biblioteca.getLibros()) {
      if (l.getTitulo().equalsIgnoreCase(titulo) && !l.prestado()) {
        libroAPrestar = l;
        break;
      }
    }

    if (libroAPrestar == null) {
      System.out.println("No hay ejemplares disponibles para préstamo con el título: " + titulo);
      return;
    }

    if (!socio.puedePedir()) {
      System.out.println("El socio " + socio.getNombre() + " no está habilitado para solicitar préstamos (Posee préstamos vencidos o superó el límite).");
      return;
    }

    Calendar fechaHoy = Calendar.getInstance();
    boolean exito = p_biblioteca.prestarLibro(fechaHoy, socio, libroAPrestar);
    
    if (exito) {
      System.out.println("¡Préstamo registrado con éxito!");
      System.out.println("Libro: " + libroAPrestar.getTitulo());
      System.out.println("Socio: " + socio.getNombre());
    }
  }

  /**
   * Registra la devolución de un libro utilizando un bloque try-catch para capturar LibroNoPrestadoException.
   */
  private static void registrarDevolucionLibro(Biblioteca p_biblioteca, Scanner p_teclado) {
    System.out.print("Ingrese el título del libro a devolver: ");
    String titulo = p_teclado.nextLine();

    Libro libroADevolver = null;
    for (Libro l : p_biblioteca.getLibros()) {
      if (l.getTitulo().equalsIgnoreCase(titulo)) {
        libroADevolver = l;
        break;
      }
    }

    if (libroADevolver == null) {
      System.out.println("El libro '" + titulo + "' no pertenece a la biblioteca.");
      return;
    }

    // Bloque seguro try-catch exigido por el Tema 8 de la cátedra
    try {
      p_biblioteca.devolverLibro(libroADevolver);
      System.out.println("¡Devolución registrada con éxito para el libro: " + libroADevolver.getTitulo() + "!");
    } catch (LibroNoPrestadoException e) {
      System.out.println("Excepción capturada: " + e.getMessage());
    }
  }

  /**
   * Muestra la lista de préstamos vencidos en la fecha actual.
   */
  private static void mostrarPrestamosVencidos(Biblioteca p_biblioteca) {
    System.out.println("Préstamos vencidos a la fecha:");
    if (p_biblioteca.prestamosVencidos().isEmpty()) {
      System.out.println("No hay préstamos vencidos registrados.");
    } else {
      for (Prestamo p : p_biblioteca.prestamosVencidos()) {
        System.out.println(p.toString());
        System.out.println("--------------------");
      }
    }
  }

  /**
   * Permite agregar de forma interactiva un nuevo socio mediante teclado.
   */
  private static void agregarNuevoSocio(Biblioteca p_biblioteca, Scanner p_teclado) {
    System.out.println("Tipo de socio a agregar:");
    System.out.println("1) Estudiante");
    System.out.println("2) Docente");
    System.out.print("Seleccione tipo: ");
    int tipo = Integer.parseInt(p_teclado.nextLine());

    System.out.print("DNI: ");
    int dni = Integer.parseInt(p_teclado.nextLine());

    System.out.print("Nombre y Apellido: ");
    String nombre = p_teclado.nextLine();

    if (tipo == 1) {
      System.out.print("Carrera: ");
      String carrera = p_teclado.nextLine();
      p_biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
      System.out.println("Socio Estudiante agregado correctamente.");
    } else if (tipo == 2) {
      System.out.print("Área: ");
      String area = p_teclado.nextLine();
      p_biblioteca.nuevoSocioDocente(dni, nombre, area);
      System.out.println("Socio Docente agregado correctamente.");
    } else {
      System.out.println("Tipo de socio no válido.");
    }
  }

  /**
   * Permite agregar de forma interactiva un nuevo libro mediante teclado.
   */
  private static void agregarNuevoLibro(Biblioteca p_biblioteca, Scanner p_teclado) {
    System.out.print("Título: ");
    String titulo = p_teclado.nextLine();

    System.out.print("Edición: ");
    int edicion = Integer.parseInt(p_teclado.nextLine());

    System.out.print("Editorial: ");
    String editorial = p_teclado.nextLine();

    System.out.print("Año: ");
    int anio = Integer.parseInt(p_teclado.nextLine());

    p_biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
    System.out.println("Libro agregado exitosamente al catálogo.");
  }
}