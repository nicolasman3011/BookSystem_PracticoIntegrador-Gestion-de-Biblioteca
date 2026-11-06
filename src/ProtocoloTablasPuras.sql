-- Borrar todas las tablas de la base de datos en orden correcto 
-- (evitando conflictos de Claves Foráneas / Foreign Keys)

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS socios;