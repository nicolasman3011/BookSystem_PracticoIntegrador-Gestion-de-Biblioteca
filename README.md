# 📚 BookSystem - Gestión de Biblioteca

**BookSystem** es un sistema de escritorio diseñado para administrar los procesos de una biblioteca (registro de libros, gestión de usuarios, préstamos y devoluciones). Fue desarrollado como Práctico Integrador para la materia Programación Orientada a Objetos, aplicando arquitectura MVC y el patrón DAO para conectar con la base de datos.

---

## 📑 Índice
1. [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
2. [👥 Autores del Proyecto](#-autores-del-proyecto)
3. [📂 Dónde está cada cosa](#-dónde-está-cada-cosa)

---

## 🛠️ Tecnologías Utilizadas
- **Lenguaje:** Java SE
- **Interfaz Gráfica:** Java Swing
- **Base de Datos:** SQL Server / PostgreSQL (conexión vía JDBC)
- **Paradigma:** Programación Orientada a Objetos (POO)

---

## 👥 Autores del Proyecto

| Desarrollador | Rol / Tareas |
| :--- | :--- |
| **Nicolás Javier Mancedo** | Desarrollo general y estructuración |
| **Néstor Nacimiento** | Desarrollo y lógica |
| **Enrique** | Desarrollo y lógica |
| **Andrés Candia** | Desarrollo y lógica |
| **Silvana Ponce** | Desarrollo y lógica |
| **Joaquín Rodríguez** | Desarrollo y lógica |

---

## 📂 Dónde está cada cosa

El proyecto está organizado en paquetes para mantener el código limpio y separar la interfaz de la base de datos:

- **`src/modelo/`** : Contiene las entidades principales (Libro, Usuario, Prestamo).
- **`src/vista/`** : Contiene las ventanas e interfaces gráficas hechas en Java Swing.
- **`src/controlador/`** : Es el puente que captura las acciones de la vista y llama a la base de datos.
- **`src/dao/`** : Capa de persistencia. Aquí están las clases que ejecutan las consultas SQL (CRUD).
- **`src/utils/`** : Utilidades del sistema, incluyendo la clase que realiza la conexión a la base de datos.
- **`database/`** : Scripts SQL para crear las tablas (`schema.sql`) y datos de prueba (`inserts.sql`).
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
