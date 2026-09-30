-- Database: tienda_javafx

-- DROP DATABASE IF EXISTS tienda_javafx;

CREATE DATABASE tienda_javafx
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

	-- Ejecutar conectado a tienda_javafx. No elimina datos existentes.
BEGIN;
CREATE TABLE IF NOT EXISTS categoria (
 id SERIAL PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL CHECK (btrim(nombre) <> ''),
 activa BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS producto (
 id SERIAL PRIMARY KEY,
 codigo VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(codigo) <> ''),
 nombre VARCHAR(150) NOT NULL CHECK (btrim(nombre) <> ''),
 categoria_id INTEGER NOT NULL,
 precio_venta NUMERIC(12,2) NOT NULL CHECK (precio_venta >= 0),
 existencia INTEGER NOT NULL DEFAULT 0 CHECK (existencia >= 0),
 activo BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_producto_categoria FOREIGN KEY(categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT
);
CREATE INDEX IF NOT EXISTS idx_producto_categoria ON producto(categoria_id);
CREATE TABLE IF NOT EXISTS cliente (
 id SERIAL PRIMARY KEY,
 nombre VARCHAR(150) NOT NULL CHECK (btrim(nombre) <> ''),
 documento VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(documento) <> ''),
 telefono VARCHAR(30), correo VARCHAR(150), direccion VARCHAR(300),
 activo BOOLEAN NOT NULL DEFAULT TRUE
);
COMMIT;

-- DATOS DE PRÁCTICA: 5 categorías, 10 productos y 10 clientes.
-- Todos los clientes y sus datos de contacto son ficticios.
-- Requisito: ejecutar primero 02-tablas.sql en la base tienda_javafx.
-- pgAdmin: abrir Query Tool en tienda_javafx y ejecutar este archivo completo.
-- Terminal: psql -U postgres -d tienda_javafx -v ON_ERROR_STOP=1 -f database/03-datos-ejemplo.sql
-- Se puede volver a ejecutar: omite categorías existentes por nombre,
-- productos por código y clientes por documento, sin modificar sus datos.
-- Si ya ejecutaste la versión anterior, reutiliza sus categorías y P001/P002.
-- Los ID son generados por PostgreSQL; no se supone que comiencen en 1.

BEGIN;

-- 1. CATEGORÍAS (5)
INSERT INTO categoria (nombre, activa)
SELECT datos.nombre, TRUE
FROM (VALUES
    ('Papelería'),
    ('Tecnología'),
    ('Hogar'),
    ('Alimentos'),
    ('Limpieza')
) AS datos(nombre)
WHERE NOT EXISTS (
    SELECT 1 FROM categoria c WHERE c.nombre = datos.nombre
);

-- 2. PRODUCTOS (10: dos por cada categoría)
-- La categoría se resuelve por su nombre para respetar la clave foránea.
-- MIN(id) elige una sola categoría si la base ya tenía nombres repetidos.
-- La ruta de imagen queda vacía; puedes asignarla desde la aplicación.
INSERT INTO producto
    (codigo, nombre, categoria_id, precio_venta, existencia, activo)
SELECT datos.codigo, datos.nombre,
       (SELECT MIN(c.id) FROM categoria c WHERE c.nombre = datos.categoria),
       datos.precio, datos.existencia, TRUE
FROM (VALUES
    ('P001', 'Cuaderno universitario', 'Papelería', 85.50, 24),
    ('P002', 'Mouse inalámbrico',      'Tecnología', 350.00, 8),
    ('P003', 'Caja de lápices',        'Papelería', 60.00, 35),
    ('P004', 'Teclado USB',            'Tecnología', 480.00, 12),
    ('P005', 'Taza de cerámica',       'Hogar', 95.00, 30),
    ('P006', 'Jarra de plástico',      'Hogar', 125.00, 18),
    ('P007', 'Arroz de 1 kg',          'Alimentos', 48.00, 60),
    ('P008', 'Frijoles de 1 kg',       'Alimentos', 72.00, 45),
    ('P009', 'Detergente de 500 g',    'Limpieza', 55.00, 40),
    ('P010', 'Jabón líquido de 1 L',   'Limpieza', 110.00, 22)
) AS datos(codigo, nombre, categoria, precio, existencia)
ON CONFLICT (codigo) DO NOTHING;

-- 3. CLIENTES (10)
-- Documentos DEMO y correos example.com exclusivos para la práctica.
INSERT INTO cliente (nombre, documento, telefono, correo, direccion, activo)
VALUES
    ('Ana López',      'DEMO-001', '0000-0001', 'ana.lopez@example.com',     'Dirección de prueba 01, Managua', TRUE),
    ('Carlos Pérez',   'DEMO-002', '0000-0002', 'carlos.perez@example.com',  'Dirección de prueba 02, León', TRUE),
    ('María García',   'DEMO-003', '0000-0003', 'maria.garcia@example.com',  'Dirección de prueba 03, Masaya', TRUE),
    ('José Martínez',  'DEMO-004', '0000-0004', 'jose.martinez@example.com', 'Dirección de prueba 04, Granada', TRUE),
    ('Lucía Rodríguez','DEMO-005', '0000-0005', 'lucia.rodriguez@example.com','Dirección de prueba 05, Estelí', TRUE),
    ('Pedro Sánchez',  'DEMO-006', '0000-0006', 'pedro.sanchez@example.com', 'Dirección de prueba 06, Matagalpa', TRUE),
    ('Sofía Ramírez',   'DEMO-007', '0000-0007', 'sofia.ramirez@example.com', 'Dirección de prueba 07, Jinotepe', TRUE),
    ('Diego Torres',   'DEMO-008', '0000-0008', 'diego.torres@example.com',  'Dirección de prueba 08, Rivas', TRUE),
    ('Elena Flores',   'DEMO-009', '0000-0009', 'elena.flores@example.com',  'Dirección de prueba 09, Chinandega', TRUE),
    ('Miguel Herrera', 'DEMO-010', '0000-0010', 'miguel.herrera@example.com','Dirección de prueba 10, Boaco', FALSE)
ON CONFLICT (documento) DO NOTHING;

COMMIT;

-- 4. CONSULTAR LOS DATOS INSERTADOS
-- En una base vacía antes de la carga: 5 categorías, 10 productos, 10 clientes.
-- Si ya había otros registros, los totales pueden ser mayores.
SELECT 'Categorías' AS tabla, COUNT(*) AS cantidad FROM categoria
UNION ALL
SELECT 'Productos', COUNT(*) FROM producto
UNION ALL
SELECT 'Clientes', COUNT(*) FROM cliente;

SELECT c.nombre AS categoria, COUNT(p.id) AS productos
FROM categoria c
LEFT JOIN producto p ON p.categoria_id = c.id
GROUP BY c.id, c.nombre
ORDER BY c.nombre;

select * from categoria
