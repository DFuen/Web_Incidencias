# SQL de datos de prueba para colegio

Script MySQL para insertar datos de ejemplo en un colegio:

- 3 cuentas de administradores
- 3 cuentas de profesores
- Varias incidencias de prueba

## Script SQL

<pre><code class="language-sql">-- =====================================================
-- DATOS BASE
-- =====================================================
USE incidencias;

START TRANSACTION;

-- Categorías base (evita duplicados por nombre)
INSERT INTO categoria (nombre)
SELECT 'Mantenimiento general'
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'Mantenimiento general'
);

INSERT INTO categoria (nombre)
SELECT 'TIC'
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'TIC'
);

INSERT INTO categoria (nombre)
SELECT 'Infraestructura'
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'Infraestructura'
);

-- Ubicaciones base (evita duplicados por código)
INSERT INTO ubicacion (codigo, descripcion)
SELECT '1.12', 'Aula 1.12'
WHERE NOT EXISTS (
    SELECT 1 FROM ubicacion WHERE codigo = '1.12'
);

INSERT INTO ubicacion (codigo, descripcion)
SELECT '0.3', 'Laboratorio TIC'
WHERE NOT EXISTS (
    SELECT 1 FROM ubicacion WHERE codigo = '0.3'
);

INSERT INTO ubicacion (codigo, descripcion)
SELECT '2.5', 'Biblioteca'
WHERE NOT EXISTS (
    SELECT 1 FROM ubicacion WHERE codigo = '2.5'
);

INSERT INTO ubicacion (codigo, descripcion)
SELECT 'GIM', 'Gimnasio'
WHERE NOT EXISTS (
    SELECT 1 FROM ubicacion WHERE codigo = 'GIM'
);

-- =====================================================
-- USUARIOS (3 ADMIN + 3 PROFES)
-- =====================================================
-- Hashes BCrypt generados para las contraseñas reales de cada cuenta.
SET @bcrypt_jefatura = '$2b$10$VKDGfCEkgrP30RvOHZ/jLuQe8RJBN774nYzhCXAo7pqBWQDD3hj.C';
SET @bcrypt_secretaria = '$2b$10$peCkJV55P.WznqOMKjvCGeBkL6D8J7iRq9LNdsNtdpSA6di33iDcG';
SET @bcrypt_sistemas = '$2b$10$Azug1qBVYb5xQQ7hEdE/9OXq3VvEYP4W0VwQzszE4qdFml7mcGdZ.';
SET @bcrypt_lengua = '$2b$10$teXcToeD9D0G1sfIdhpLV.a9t1j5HZf0RHOh8naifyRWiw6J1kG82';
SET @bcrypt_matematicas = '$2b$10$yxFKJX6apK/MDQuMwmTVr.6G7cm5q.HTMk/gAKg7qEgvGZw1gDH0K';
SET @bcrypt_ciencias = '$2b$10$CF3GPP05R1BrUXUKoLCAZODhbrsmXWef5hPpLaNJhB2Mm9MT18iji';

INSERT INTO usuario (nombre, email, rol, password) VALUES
('Carlos Jefatura', 'carlos.jefatura@colegiosanmartin.es', 'administrador', @bcrypt_jefatura),
('Laura Secretaria', 'laura.secretaria@colegiosanmartin.es', 'administrador', @bcrypt_secretaria),
('Sergio Sistemas', 'sergio.sistemas@colegiosanmartin.es', 'administrador', @bcrypt_sistemas),
('Marina Lengua', 'marina.lengua@colegiosanmartin.es', 'profesor', @bcrypt_lengua),
('Javier Matematicas', 'javier.matematicas@colegiosanmartin.es', 'profesor', @bcrypt_matematicas),
('Elena Ciencias', 'elena.ciencias@colegiosanmartin.es', 'profesor', @bcrypt_ciencias)
ON DUPLICATE KEY UPDATE
nombre = VALUES(nombre),
rol = VALUES(rol),
password = VALUES(password);

-- Contraseñas en texto plano usadas en este juego de prueba:
-- Carlos Jefatura: Jefatura1!
-- Laura Secretaria: Secretaria1!
-- Sergio Sistemas: Sistemas1!
-- Marina Lengua: Lengua1!
-- Javier Matematicas: Matematicas1!
-- Elena Ciencias: Ciencias1!

-- =====================================================
-- INCIDENCIAS DE PRUEBA
-- =====================================================
-- Caso 1: Pendiente, sin asignar
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = '1.12' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'Mantenimiento general' LIMIT 1),
    'La pizarra digital del aula no enciende al comenzar la primera hora.',
    NULL,
    'PENDIENTE',
    NOW() - INTERVAL 5 DAY,
    NULL,
    (SELECT id FROM usuario WHERE email = 'marina.lengua@colegiosanmartin.es' LIMIT 1),
    NULL,
    NULL;

-- Caso 2: En proceso, asignada a admin
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = '0.3' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'TIC' LIMIT 1),
    'Varios ordenadores del aula de informática no tienen conexión a internet.',
    'incidencia_wifi_lab.jpg',
    'EN_PROCESO',
    NOW() - INTERVAL 3 DAY,
    NULL,
    (SELECT id FROM usuario WHERE email = 'javier.matematicas@colegiosanmartin.es' LIMIT 1),
    (SELECT id FROM usuario WHERE email = 'carlos.jefatura@colegiosanmartin.es' LIMIT 1),
    NULL;

-- Caso 3: Realizada con solución
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = '2.5' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'Infraestructura' LIMIT 1),
    'Fuga leve en radiador junto a la zona de lectura de la biblioteca escolar.',
    NULL,
    'REALIZADA',
    NOW() - INTERVAL 10 DAY,
    NOW() - INTERVAL 8 DAY,
    (SELECT id FROM usuario WHERE email = 'elena.ciencias@colegiosanmartin.es' LIMIT 1),
    (SELECT id FROM usuario WHERE email = 'laura.secretaria@colegiosanmartin.es' LIMIT 1),
    'Se sustituyó la junta del radiador y se verificó que no hay más fugas.';

-- Caso 4: Pendiente
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = 'GIM' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'Mantenimiento general' LIMIT 1),
    'Una de las canastas del patio cubierto está suelta y se mueve al tirar.',
    'canasta_gimnasio.jpg',
    'PENDIENTE',
    NOW() - INTERVAL 2 DAY,
    NULL,
    (SELECT id FROM usuario WHERE email = 'marina.lengua@colegiosanmartin.es' LIMIT 1),
    NULL,
    NULL;

-- Caso 5: En proceso
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = '1.12' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'TIC' LIMIT 1),
    'El proyector del aula de 1 ESO muestra imagen con parpadeo intermitente.',
    NULL,
    'EN_PROCESO',
    NOW() - INTERVAL 1 DAY,
    NULL,
    (SELECT id FROM usuario WHERE email = 'javier.matematicas@colegiosanmartin.es' LIMIT 1),
    (SELECT id FROM usuario WHERE email = 'sergio.sistemas@colegiosanmartin.es' LIMIT 1),
    NULL;

-- Caso 6: Realizada
INSERT INTO incidencia (
    ubicacion_id, categoria_id, descripcion, foto, estado, fecha_creacion,
    fecha_resolucion, usuario_creador_id, usuario_encargado_id, descripcion_solucion
)
SELECT
    (SELECT id FROM ubicacion WHERE codigo = '0.3' LIMIT 1),
    (SELECT id FROM categoria WHERE nombre = 'TIC' LIMIT 1),
    'Teclado del puesto 14 del aula TIC con varias teclas bloqueadas.',
    NULL,
    'REALIZADA',
    NOW() - INTERVAL 7 DAY,
    NOW() - INTERVAL 6 DAY,
    (SELECT id FROM usuario WHERE email = 'elena.ciencias@colegiosanmartin.es' LIMIT 1),
    (SELECT id FROM usuario WHERE email = 'carlos.jefatura@colegiosanmartin.es' LIMIT 1),
    'Se reemplazó el teclado y se comprobó funcionamiento correcto.';

COMMIT;

-- =====================================================
-- CONSULTAS RÁPIDAS DE COMPROBACIÓN
-- =====================================================
SELECT id, nombre, email, rol FROM usuario ORDER BY rol, nombre;
SELECT id, codigo, descripcion FROM ubicacion ORDER BY codigo;
SELECT id, nombre FROM categoria ORDER BY nombre;
SELECT id, estado, fecha_creacion, usuario_creador_id, usuario_encargado_id FROM incidencia ORDER BY id DESC;
</code></pre>

## Nota

Cada cuenta tiene su propia contraseña y su hash BCrypt correspondiente, para que el login funcione directamente con Spring Security.
