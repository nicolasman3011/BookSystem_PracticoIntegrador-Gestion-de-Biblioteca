--- 1. Recreación limpia de la estructura de tablas
DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS socios;

CREATE TABLE socios (
    dni INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    tipo_socio TEXT NOT NULL, -- 'Estudiante' o 'Docente'
    dias_prestamo INTEGER NOT NULL,
    carrera TEXT,
    area TEXT
);

CREATE TABLE libros (
    id_libro INTEGER PRIMARY KEY,
    titulo TEXT NOT NULL,
    edicion INTEGER NOT NULL,
    editorial TEXT NOT NULL,
    anio INTEGER NOT NULL
);

CREATE TABLE prestamos (
    id_prestamo INTEGER PRIMARY KEY AUTOINCREMENT,
    socio_dni INTEGER NOT NULL,
    libro_id INTEGER NOT NULL,
    fecha_retiro DATE NOT NULL,      -- Formato de texto 'YYYY-MM-DD'
    fecha_devolucion DATE,           -- NULL si se encuentra en poder del socio
    FOREIGN KEY (socio_dni) REFERENCES socios(dni),
    FOREIGN KEY (libro_id) REFERENCES libros(id_libro)
);

-- =========================================================
-- 2. CARGA DE DATOS DE PRUEBA (TP7)
-- =========================================================

-- Insertar Socios (3 Estudiantes y 3 Docentes)
INSERT INTO socios (dni, nombre, tipo_socio, dias_prestamo, carrera, area) VALUES
(38999702, 'Carlos Gomez', 'Estudiante', 20, 'Lic. en Sistemas', NULL),
(40123456, 'María López', 'Estudiante', 20, 'Ingeniería en Agrimensura', NULL),
(41987654, 'Lucas Rodríguez', 'Estudiante', 20, 'Lic. en Química', NULL),
(14524782, 'Juan Perez', 'Docente', 5, NULL, 'Programación'),
(17982110, 'Juan Fernández', 'Docente', 10, NULL, 'Matemática'),
(20111222, 'Ana Martínez', 'Docente', 5, NULL, 'Física');

-- Insertar Libros (Asignando un ID único de ejemplar físico)
INSERT INTO libros (id_libro, titulo, edicion, editorial, anio) VALUES
(1, 'JAVA. Como Programar', 10, 'Pearson', 2015),
(2, 'Longman. Diccionario Pocket', 3, 'Longman', 2010),
(3, 'Vivir para contarla', 1, 'Sudamericana', 2002),
(4, 'Programando con JAVA', 2, 'Alfaomega', 2018),
(5, 'Prueba 12 - A la espera de fallos', 1, 'UTN Ediciones', 2024);

-- =========================================================
-- 3. CARGA DE PRÉSTAMOS (Normales, Activos y Vencidos)
-- =========================================================

INSERT INTO prestamos (socio_dni, libro_id, fecha_retiro, fecha_devolucion) VALUES
-- Préstamo 1: Finalizado / Devuelto a tiempo (Ejemplar ID 3: Vivir para contarla)
(38999702, 3, '2026-09-01', '2026-09-10'),

-- Préstamo 2: Activo regular en término (Ejemplar ID 2: Longman. Diccionario Pocket)
(38999702, 2, '2026-11-01', NULL),

-- Préstamo 3: Activo regular en término (Ejemplar ID 1: JAVA. Como Programar)
(14524782, 1, '2026-11-02', NULL),

-- Préstamo 4: *** PRÉSTAMO VENCIDO (ESTUDIANTE) *** (Ejemplar ID 4: Programando con JAVA)
(41987654, 4, '2026-09-15', NULL),

-- Préstamo 5: *** PRÉSTAMO VENCIDO (DOCENTE) *** (Ejemplar ID 5: Prueba 12 - A la espera de fallos)
(20111222, 5, '2026-10-01', NULL);