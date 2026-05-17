# Datos de prueba para la aplicación de incidencias

## Objetivo

Este conjunto de datos de prueba está pensado únicamente para comprobar el funcionamiento visual y funcional del frontend de la aplicación.

Con estos datos se pueden probar correctamente:

- Inicio de sesión
- Panel de administrador
- Panel de profesor
- Métricas y estadísticas
- Gestión de incidencias
- Filtros
- Estados de incidencias
- Dashboard
- Gráficas
- Incidencias por ubicación
- Gestión de usuarios
- Gestión de ubicaciones

---

# Usuarios de prueba

## Administrador

| Campo | Valor |
|---|---|
| Nombre | Administrador Principal |
| Email | admin@centro.es |
| Contraseña | 123456 |
| Rol | administrador |

---

## Profesor

| Campo | Valor |
|---|---|
| Nombre | Profesor Demo |
| Email | profesor@centro.es |
| Contraseña | 123456 |
| Rol | profesor |

---

# Categorías de prueba

La aplicación utiliza dos categorías principales para probar las métricas y filtros.

| ID | Categoría |
|---|---|
| 1 | TIC |
| 2 | Mantenimiento |

---

# Estados utilizados en las incidencias

Las incidencias incluyen diferentes estados para poder comprobar correctamente el comportamiento de la aplicación.

| Estado | Uso |
|---|---|
| PENDIENTE | Incidencia recién creada |
| EN_PROCESO | Incidencia actualmente gestionándose |
| REALIZADA | Incidencia ya solucionada |

---

# Resumen de incidencias generadas

Se incluyen:

- 30 incidencias de prueba
- incidencias TIC
- incidencias de mantenimiento
- incidencias abiertas
- incidencias en proceso
- incidencias cerradas
- incidencias distribuidas entre varias ubicaciones
- incidencias para probar gráficas y métricas

---

# Ejemplos de incidencias TIC

- Ordenador no enciende
- Proyector sin imagen
- Internet lento
- Ratón averiado
- Pantalla rota
- Altavoces sin sonido
- Tablet bloqueada
- Impresora atascada
- Teclado roto
- WiFi intermitente
- Pizarra digital congelada
- Software bloqueado
- Cable HDMI roto
- Servidor lento
- Cámara web dañada

---

# Ejemplos de incidencias de mantenimiento

- Luz fundida
- Puerta atascada
- Mesa rota
- Ventana rota
- Aire acondicionado averiado
- Enchufe sin corriente
- Persiana bloqueada
- Techo con humedad
- Silla rota
- Fallo eléctrico
- Lavabo atascado
- Pintura deteriorada
- Puerta del almacén rota
- Filtro del aire sucio
- Suelo levantado

---

# Script SQL de usuarios

```sql
INSERT INTO usuario (id, nombre, email, password, rol) VALUES
(1, 'Administrador Principal', 'admin@centro.es', '$2a$10$pSNR430dRbMkTitxuCdfu.5HnwksT2Ma/UEbTYFac3mM4Tv8Gr.Hy', 'administrador'),
(2, 'Profesor Demo', 'profesor@centro.es', '$2a$10$pSNR430dRbMkTitxuCdfu.5HnwksT2Ma/UEbTYFac3mM4Tv8Gr.Hy', 'profesor');
```

---

# Script SQL de categorías

```sql
INSERT INTO categoria (id, nombre) VALUES
(1, 'TIC'),
(2, 'Mantenimiento');
```

---

# Script SQL de incidencias

```sql
INSERT INTO incidencia (
    id,
    titulo,
    descripcion,
    estado,
    fecha_creacion,
    usuario_id,
    categoria_id,
    ubicacion_id,
    foto_path,
    descripcion_solucion
) VALUES
(1,'Ordenador no enciende','El equipo del aula 101 no responde al encender.','PENDIENTE','2025-06-01 08:00:00',2,1,1,NULL,NULL),
(2,'Proyector sin imagen','El proyector muestra pantalla negra.','EN_PROCESO','2025-06-01 09:10:00',2,1,2,NULL,NULL),
(3,'Internet lento','La conexión es muy lenta en el aula.','REALIZADA','2025-06-01 10:20:00',2,1,3,NULL,'Router reiniciado correctamente.'),
(4,'Ratón averiado','El ratón no responde correctamente.','PENDIENTE','2025-06-01 11:30:00',2,1,4,NULL,NULL),
(5,'Pantalla rota','Monitor con líneas verticales.','EN_PROCESO','2025-06-01 12:15:00',2,1,5,NULL,NULL),
(6,'Altavoces sin sonido','No se escucha audio en clase.','REALIZADA','2025-06-01 13:00:00',2,1,6,NULL,'Cable sustituido.'),
(7,'Tablet bloqueada','No permite iniciar sesión.','PENDIENTE','2025-06-02 08:40:00',2,1,1,NULL,NULL),
(8,'Impresora atascada','La impresora no imprime documentos.','EN_PROCESO','2025-06-02 09:50:00',2,1,2,NULL,NULL),
(9,'Teclado roto','Varias teclas no funcionan.','REALIZADA','2025-06-02 10:30:00',2,1,3,NULL,'Teclado reemplazado.'),
(10,'WiFi intermitente','Se pierde la conexión frecuentemente.','PENDIENTE','2025-06-02 11:45:00',2,1,4,NULL,NULL),
(11,'Pizarra digital congelada','La pantalla táctil no responde.','EN_PROCESO','2025-06-02 12:30:00',2,1,5,NULL,NULL),
(12,'Software bloqueado','La aplicación de gestión no abre.','REALIZADA','2025-06-02 13:15:00',2,1,6,NULL,'Aplicación reinstalada.'),
(13,'Cable HDMI roto','No transmite señal al proyector.','PENDIENTE','2025-06-03 08:20:00',2,1,1,NULL,NULL),
(14,'Servidor lento','El sistema tarda demasiado en cargar.','EN_PROCESO','2025-06-03 09:10:00',2,1,2,NULL,NULL),
(15,'Cámara web dañada','No detecta imagen.','REALIZADA','2025-06-03 10:00:00',2,1,3,NULL,'Cámara sustituida.'),
(16,'Luz fundida','Varias luces no funcionan en el aula.','PENDIENTE','2025-06-03 11:00:00',2,2,4,NULL,NULL),
(17,'Puerta atascada','La puerta principal no cierra bien.','EN_PROCESO','2025-06-03 12:00:00',2,2,5,NULL,NULL),
(18,'Mesa rota','Mesa inestable en el laboratorio.','REALIZADA','2025-06-03 13:00:00',2,2,6,NULL,'Mesa sustituida.'),
(19,'Ventana rota','Cristal agrietado en aula 203.','PENDIENTE','2025-06-04 08:30:00',2,2,1,NULL,NULL),
(20,'Aire acondicionado averiado','No enfría correctamente.','EN_PROCESO','2025-06-04 09:40:00',2,2,2,NULL,NULL),
(21,'Enchufe sin corriente','No funciona el enchufe lateral.','REALIZADA','2025-06-04 10:50:00',2,2,3,NULL,'Cableado reparado.'),
(22,'Persiana bloqueada','No sube completamente.','PENDIENTE','2025-06-04 11:20:00',2,2,4,NULL,NULL),
(23,'Techo con humedad','Aparecen manchas de agua.','EN_PROCESO','2025-06-04 12:40:00',2,2,5,NULL,NULL),
(24,'Silla rota','La silla del profesor está dañada.','REALIZADA','2025-06-04 13:30:00',2,2,6,NULL,'Silla reemplazada.'),
(25,'Fallo eléctrico','Saltan los plomos del aula.','PENDIENTE','2025-06-05 08:15:00',2,2,1,NULL,NULL),
(26,'Lavabo atascado','El agua no desagua correctamente.','EN_PROCESO','2025-06-05 09:30:00',2,2,2,NULL,NULL),
(27,'Pintura deteriorada','Las paredes presentan desgaste.','REALIZADA','2025-06-05 10:45:00',2,2,3,NULL,'Pared repintada.'),
(28,'Puerta del almacén rota','La cerradura no funciona.','PENDIENTE','2025-06-05 11:50:00',2,2,4,NULL,NULL),
(29,'Filtro del aire sucio','El equipo hace mucho ruido.','EN_PROCESO','2025-06-05 12:40:00',2,2,5,NULL,NULL),
(30,'Suelo levantado','Baldosas levantadas en pasillo.','REALIZADA','2025-06-05 13:20:00',2,2,6,NULL,'Baldosas reemplazadas.');
```

---

# Credenciales finales

## Administrador

```txt
Email: admin@centro.es
Password: 123456
```

## Profesor

```txt
Email: profesor@centro.es
Password: 123456
```

---

# Notas

- Las contraseñas están cifradas con BCrypt.
- Los datos están preparados para visualizar correctamente el frontend.
- Las incidencias incluyen suficientes casos para probar gráficas y estadísticas.
- Los estados variados permiten probar filtros y métricas.
