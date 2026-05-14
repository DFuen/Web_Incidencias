# Credenciales de login (colegio)

Este archivo resume las cuentas de administradores y profesores creadas en el script de datos de prueba.

## Contraseña

Cada cuenta tiene su propia contraseña en texto plano:

- Carlos Jefatura: Jefatura1!
- Laura Secretaria: Secretaria1!
- Sergio Sistemas: Sistemas1!
- Marina Lengua: Lengua1!
- Javier Matematicas: Matematicas1!
- Elena Ciencias: Ciencias1!

## Cuentas de administradores

| Nombre | Email | Rol |
|---|---|---|
| Carlos Jefatura | carlos.jefatura@colegiosanmartin.es | administrador |
| Laura Secretaria | laura.secretaria@colegiosanmartin.es | administrador |
| Sergio Sistemas | sergio.sistemas@colegiosanmartin.es | administrador |

## Cuentas de profesores

| Nombre | Email | Rol |
|---|---|---|
| Marina Lengua | marina.lengua@colegiosanmartin.es | profesor |
| Javier Matematicas | javier.matematicas@colegiosanmartin.es | profesor |
| Elena Ciencias | elena.ciencias@colegiosanmartin.es | profesor |

## Nota técnica

- En base de datos, la contraseña está almacenada en formato BCrypt.
- En el login se introduce la contraseña en texto plano: password.
