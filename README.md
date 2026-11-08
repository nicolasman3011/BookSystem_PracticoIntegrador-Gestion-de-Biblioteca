# 📚 BookSystem - Gestión de Biblioteca

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![SQL](https://img.shields.io/badge/Base_de_Datos-SQL-003B57?style=for-the-badge&logo=postgresql&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java_Swing-blue?style=for-the-badge)
![POO](https://img.shields.io/badge/Paradigma-POO-success?style=for-the-badge)

**BookSystem** es un software de escritorio integral diseñado para digitalizar y administrar de manera eficiente los procesos centrales de una biblioteca. El sistema permite gestionar el catálogo de libros, registrar usuarios, y llevar un control estricto de los préstamos, devoluciones y disponibilidad del material bibliográfico.

### 💡 ¿De qué se trata y en qué nos basamos?
Este proyecto fue desarrollado como el Trabajo Práctico Integrador final para la materia Programación Orientada a Objetos (Licenciatura en Sistemas de Información). 

Nos basamos fuertemente en los pilares de la POO (Encapsulamiento, Herencia, Polimorfismo y Abstracción) para modelar las entidades del mundo real (Libros, Usuarios, Préstamos) y llevarlas al código. Para garantizar que el sistema sea escalable, seguro y fácil de mantener, implementamos la **Arquitectura MVC (Modelo-Vista-Controlador)**. Esto nos permitió separar completamente la interfaz visual (lo que ve el usuario) de la lógica del negocio. Además, utilizamos el **Patrón DAO (Data Access Object)** para abstraer la conexión y asegurar que toda la información persista de forma segura en una base de datos relacional.

### ⚙️ ¿Cómo llevamos a cabo el proyecto y cómo nos dividimos?
Desarrollar un sistema completo requiere organización, por lo que trabajamos de manera colaborativa y modular. Nos dividimos las tareas en dos grandes frentes simultáneos para optimizar los tiempos:

1. **Back-End y Persistencia:** Un equipo se encargó de la estructuración de la base de datos (diagramas DER), la creación de las entidades del Modelo, la configuración de la conexión JDBC y la programación de toda la capa DAO para ejecutar las consultas SQL (CRUD).
2. **Front-End y Controladores:** El otro equipo se enfocó en la experiencia del usuario, diseñando las interfaces gráficas mediante Java Swing, maquetando los formularios y programando los Controladores encargados de capturar los eventos (clics, ingresos por teclado) para enviarlos a la base de datos de manera validada.

---

## 📑 Índice
1. [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
2. [👥 Autores y División de Tareas](#-autores-y-división-de-tareas)
3. [📂 Dónde está cada cosa](#-dónde-está-cada-cosa)
4. [🏗️ Estructura del Repositorio](#️-estructura-del-repositorio)

---

## 🛠️ Tecnologías Utilizadas
- **Lenguaje Principal:** Java SE
- **Interfaz Gráfica:** Java Swing
- **Base de Datos:** SQL Server / PostgreSQL
- **Conexión a BD:** JDBC (Java Database Connectivity)
- **Arquitectura y Patrones:** MVC (Modelo-Vista-Controlador) y DAO
- **Paradigma:** Programación Orientada a Objetos (POO)

---

## 👥 Autores y División de Tareas

| Desarrollador | Rol / Tareas Asignadas |
| :--- | :--- |
| **Nicolás Javier Mancedo** | Desarrollo general, estructuración de arquitectura y capa de persistencia (DAO/SQL) |
| **Néstor Nacimiento** | Desarrollo core, lógica de controladores e interfaz gráfica (Swing) |
| **Enrique** | Desarrollo de módulos y lógica de negocio |
| **Andrés Candia** | Desarrollo de módulos y lógica de negocio |
| **Silvana Ponce** | Desarrollo de módulos y lógica de negocio |
| **Joaquín Rodríguez** | Desarrollo de módulos y lógica de negocio |

---
## 📂 Dónde está cada cosa

El proyecto está rigurosamente organizado en paquetes lógicos para mantener el código limpio y cumplir con la arquitectura MVC:

- **`src/modelo/`** : Contiene las clases base y entidades (Libro, Usuario, Prestamo). No tienen dependencias de la vista ni de la base de datos.
- **`src/vista/`** : Contiene las ventanas e interfaces gráficas. Se encargan únicamente de mostrar datos y capturar las interacciones del usuario.
- **`src/controlador/`** : Es el cerebro del sistema. Escucha las acciones de la vista (ej. botón "Guardar"), valida los datos y llama a la base de datos.
- **`src/dao/`** : Capa de persistencia. Aquí están las clases que ejecutan las sentencias SQL (INSERT, SELECT, UPDATE, DELETE).
- **`src/utils/`** : Utilidades globales del sistema, incluyendo la clase fundamental `Conexion.java` que gestiona el puente entre Java y el motor SQL.
- **`database/`** : Scripts SQL preparados para generar las tablas del sistema (`schema.sql`) y cargar información de prueba (`inserts.sql`).


## 🏗️ Estructura del Repositorio
El proyecto está organizado reflejando el trabajo modular de cada integrante del equipo dentro del paquete principal `src/biblioteca`:

```text
📦 BookSystem_PracticoIntegrador
 ┣ 📂 src/biblioteca/
 ┃ ┣ 🧑‍💻 Enrique Grat
 ┃ ┃ ┣ 📜 Socio.java           # Clase base para los socios de la biblioteca
 ┃ ┃ ┗ 📜 Prestamo.java        # Lógica y registro de préstamos
 ┃ ┣ 🧑‍💻 Andrés Candia
 ┃ ┃ ┣ 📜 Docente.java         # Clase heredada para socios tipo Docente
 ┃ ┃ ┗ 📜 Estudiante.java      # Clase heredada para socios tipo Estudiante
 ┃ ┣ 🧑‍💻 Nicolás Javier Mancedo
 ┃ ┃ ┗ 📜 Libro.java           # Entidad principal del catálogo bibliográfico
 ┃ ┗ 👥 Néstor, Silvana & Joaquín (Equipo de Integración)
 ┃   ┣ 📜 Biblioteca.java      # Clase Gestora (Controlador principal)
 ┃   ┗ 📜 GestionBiblioteca.java # Ejecutable principal (Main)
 ┗ 📄 README.md                # Documentación del proyecto
