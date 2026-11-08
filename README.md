📚 BookSystem - Gestión de Biblioteca
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL/SQL](https://img.shields.io/badge/SQL-003B57?style=for-the-badge&logo=postgresql&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
Sistema de gestión de biblioteca desarrollado como Práctico Integrador para la materia Programación Orientada a Objetos (Licenciatura en Sistemas de Información). El proyecto implementa una arquitectura MVC (Modelo-Vista-Controlador) y el patrón DAO para la persistencia de datos en una base de datos relacional.
📑 Índice de Contenidos
👥 Autores del Proyecto
⚙️ Arquitectura y Conexión (El Código)
🛠️ Tecnologías Utilizadas
🚀 Instalación y Uso
---
👥 Autores del Proyecto
👨‍💻 Desarrollador	🎓 Rol / Tareas
Nicolás Javier Mancedo	Desarrollo general y estructuración
Néstor Nacimiento	Desarrollo y lógica
Enrique Grat	Desarrollo y lógica
Andrés Candia	Desarrollo y lógica
Silvana Ponce	Desarrollo y lógica
Joaquín Rodríguez	Desarrollo y lógica
---
⚙️ Arquitectura y Conexión (El Código)
Para que el proyecto funcione de manera estructurada, utilizamos el patrón DAO (Data Access Object) y separamos la lógica en diferentes capas. Aquí explicamos dónde encontrar las partes clave del código en el repositorio:
1. La Conexión a la Base de Datos (`utils/Conexion.java`)
Toda la configuración para conectar Java con el motor SQL está centralizada en una sola clase de utilidad. Si se necesita cambiar el puerto, el usuario o la contraseña de la base de datos para probar el proyecto, se modifica directamente aquí:
```java
// Ejemplo de la clase de conexión (Directorio: src/utils/Conexion.java)
public class Conexion {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BibliotecaDB";
    private static final String USER = "tu_usuario";
    private static final String PASS = "tu_contraseña";
    
    public static Connection conectar() {
        // Retorna la conexión activa a la base de datos SQL
    }
}
```
2. Capa de Persistencia (`dao/LibroDAO.java`, `dao/UsuarioDAO.java`)
Es la capa encargada exclusivamente de interactuar con SQL. El controlador llama a estos métodos cuando se necesita guardar, eliminar, actualizar o consultar un registro.
```java
// Ejemplo de guardado (Directorio: src/dao/LibroDAO.java)
public boolean registrarLibro(Libro libro) {
    String sql = "INSERT INTO libros (titulo, autor, isbn) VALUES (?, ?, ?)";
    // Ejecuta la consulta utilizando la clase Conexion
}
```
3. La Interfaz Gráfica / Vista (`vista/`)
Las ventanas construidas en Java Swing capturan los eventos del usuario (por ejemplo, completar un formulario y hacer clic en "Guardar"). Estas clases se limitan a la visualización y le envían las acciones al Controlador para que procese la lógica, manteniendo la vista completamente separada del código SQL.
---
🛠️ Tecnologías Utilizadas
Lenguaje: Java SE
IDE: BlueJ (u otro entorno compatible como Eclipse/NetBeans)
Interfaz de Usuario: Java Swing
Base de Datos: SQL Server / PostgreSQL (conexión vía JDBC)
Paradigma: Programación Orientada a Objetos (POO)
---
🚀 Instalación y Uso
Clonar el repositorio en tu equipo local.
Ejecutar los scripts ubicados en la carpeta de base de datos en tu motor SQL para montar la estructura de tablas.
Descargar el archivo `.jar` del conector JDBC y agregarlo a las librerías del proyecto en tu IDE.
Modificar las credenciales de conexión en la clase `Conexion.java`.
Compilar y ejecutar la clase principal (Main) para iniciar la aplicación.

```text
📦 src/biblioteca
┣ 🧑‍💻 Enrique Grat
┃ ┣ 📜 Socio.java 
┃ ┗ 📜 Prestamo.java
┣ 🧑‍💻 Andrés Candia
┃ ┣ 📜 Docente.java
┃ ┗ 📜 Estudiante.java
┣ 🧑‍💻 Nicolás Javier Mancedo
┃ ┗ 📜 Libro.java
┗ 👥 Néstor Javier Nacimiento & Silvana Ponce (Integración)
  ┣ 📜 Biblioteca.java (Clase Gestora)
  ┗ 📜 GestionBiblioteca.java (Ejecutable principal)
