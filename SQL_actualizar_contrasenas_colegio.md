# SQL para actualizar contraseñas del colegio

Este bloque sirve para actualizar solo las contraseñas de los usuarios ya creados, sin borrar ni recrear nada.

## SQL

```sql
USE incidencias;

START TRANSACTION;

UPDATE usuario
SET password = '$2b$10$VKDGfCEkgrP30RvOHZ/jLuQe8RJBN774nYzhCXAo7pqBWQDD3hj.C'
WHERE email = 'carlos.jefatura@colegiosanmartin.es';

UPDATE usuario
SET password = '$2b$10$peCkJV55P.WznqOMKjvCGeBkL6D8J7iRq9LNdsNtdpSA6di33iDcG'
WHERE email = 'laura.secretaria@colegiosanmartin.es';

UPDATE usuario
SET password = '$2b$10$Azug1qBVYb5xQQ7hEdE/9OXq3VvEYP4W0VwQzszE4qdFml7mcGdZ.'
WHERE email = 'sergio.sistemas@colegiosanmartin.es';

UPDATE usuario
SET password = '$2b$10$teXcToeD9D0G1sfIdhpLV.a9t1j5HZf0RHOh8naifyRWiw6J1kG82'
WHERE email = 'marina.lengua@colegiosanmartin.es';

UPDATE usuario
SET password = '$2b$10$yxFKJX6apK/MDQuMwmTVr.6G7cm5q.HTMk/gAKg7qEgvGZw1gDH0K'
WHERE email = 'javier.matematicas@colegiosanmartin.es';

UPDATE usuario
SET password = '$2b$10$CF3GPP05R1BrUXUKoLCAZODhbrsmXWef5hPpLaNJhB2Mm9MT18iji'
WHERE email = 'elena.ciencias@colegiosanmartin.es';

COMMIT;

-- Comprobación rápida
SELECT id, nombre, email, rol
FROM usuario
WHERE email IN (
  'carlos.jefatura@colegiosanmartin.es',
  'laura.secretaria@colegiosanmartin.es',
  'sergio.sistemas@colegiosanmartin.es',
  'marina.lengua@colegiosanmartin.es',
  'javier.matematicas@colegiosanmartin.es',
  'elena.ciencias@colegiosanmartin.es'
)
ORDER BY rol, nombre;
```

## Contraseñas en texto plano

- Carlos Jefatura: `Jefatura1!`
- Laura Secretaria: `Secretaria1!`
- Sergio Sistemas: `Sistemas1!`
- Marina Lengua: `Lengua1!`
- Javier Matematicas: `Matematicas1!`
- Elena Ciencias: `Ciencias1!`
