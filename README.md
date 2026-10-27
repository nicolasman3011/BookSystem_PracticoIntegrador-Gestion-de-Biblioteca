# 📚 Sistema de Gestión de Biblioteca

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![POO](https://img.shields.io/badge/POO-Conceptos-blue?style=for-the-badge)

## 🎓 Presentación Académica
**Carrera:** Licenciatura en Sistemas de Información  
**Asignatura:** Programación Orientada a Objetos  
**Trabajo Práctico:** Integrador - Gestión de Biblioteca  

### 👥 Integrantes del Equipo
- **Candia, Andrés** - DNI: (31.102.189)
- **Grat, Enrique** - DNI: (38.999.702)
- **Mancedo, Nicolás Javier** - DNI: (46.715.563)
- **Nacimiento, Néstor Javier** - DNI:(31.666.602)
- **Ponce, Silvana** - DNI: (32.837.314)

## 📖 Sobre el Proyecto
Desarrollo de un sistema de administración por consola para gestionar préstamos de libros. El software permite registrar diferentes perfiles de usuarios (docentes y estudiantes), llevar el control de vencimientos, aplicar reglas de penalización y generar reportes de estado utilizando estructuras de datos dinámicas.

## ⚙️ Tecnologías y Herramientas
- **Lenguaje Base:** Java  
- **Paradigma:** Programación Orientada a Objetos (POO)  
- **Colecciones y Librerías:** `ArrayList` (gestión dinámica de listas) y `java.util.Calendar` (manejo de fechas).  
- **Control de Versiones:** Git y GitHub  
- **Entorno de Desarrollo:** Compatible con BlueJ, Eclipse e IntelliJ IDEA.

## 🧠 Conceptos Teóricos Aplicados
- **Herencia y Clases Abstractas:** Implementación de la superclase abstracta `Socio` que delega comportamiento a sus subclases `Docente` y `Estudiante`.
- **Polimorfismo:** Sobrescritura de métodos clave como `puedePedir()` y `soyDeLaClase()` para que cada tipo de socio responda con su propia regla de negocio.
- **Encapsulamiento:** Ocultamiento del estado mediante atributos `private` y exposición controlada a través de getters y setters (incluyendo métodos `protected` cuando es requerido).
- **Manejo de Excepciones:** Uso de `LibroNoPrestadoException` para controlar devoluciones inválidas de forma segura.

## 📂 Arquitectura y División de Tareas
El desarrollo del código fuente fue modularizado y asignado a los integrantes de la siguiente manera:

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
