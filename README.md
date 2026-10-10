# Sistema de Gestión para Biblioteca (API REST)

## Descripción del Proyecto

Esta aplicación es una plataforma integral construida en Spring Boot diseñada para la administración completa de una biblioteca. Su arquitectura ofrece una **API RESTful** para la interacción y consumo de datos mediante clientes externos. El sistema se divide en tres módulos principales:

*   **Módulo de Inventario (`/libros`):** Encargado de la gestión del catálogo. Permite la adición de nuevas obras, modificación de sus datos (como cantidad de ejemplares), eliminación y búsqueda especializada por autor.
*   **Módulo de Miembros (`/usuarios`):** Administra el registro y seguimiento de los socios de la biblioteca. Proporciona funciones para dar de alta nuevos usuarios, actualizar su información de contacto, consultar registros mediante correo o nombre y darlos de baja del padrón.
*   **Módulo de Control de Préstamos (`/prestamos`):** El núcleo de la lógica de negocio. Vincula a los socios con los libros del inventario, realizando un descuento automático de los ejemplares disponibles al prestar una obra. Además, gestiona las devoluciones reponiendo el inventario y registrando la estampa de tiempo exacta de cada movimiento.

## Tecnologías Implementadas

*   **Lenguaje Base:** Java
*   **Framework Principal:** Spring Boot
*   **Mapeo Objeto-Relacional (ORM):** Spring Data JPA / Hibernate
*   **Motor de Base de Datos:** MySQL

## Guía de Instalación y Ejecución

1.  **Preparación del Entorno:** Verifica que tu equipo cuente con Java (JDK), un entorno de desarrollo integrado (IDE) y el motor de bases de datos MySQL en ejecución.
2.  **Base de Datos:** Accede a tu cliente MySQL y genera una base de datos vacía nombrada `libreria`.
3.  **Obtención del Código:** Descarga o clona el repositorio en tu espacio de trabajo local.
4.  **Importación:** Abre el proyecto en tu IDE como una aplicación basada en Maven.
5.  **Credenciales:** Ubica el archivo `application.properties` (en la ruta `src/main/resources`) e ingresa tus datos de acceso local a MySQL.
6.  **Arranque del Servidor:** Ejecuta el archivo principal `LibreriaApplication.java`. La aplicación levantará el servidor Tomcat embebido en el puerto 1500 y ejecutará los scripts de creación de tablas automáticamente.

## Variables de Entorno (Configuración)

La conexión a la base de datos se gestiona directamente en el archivo `application.properties`. Es indispensable ajustar los campos de usuario y contraseña antes de ejecutar el proyecto:

```properties
spring.application.name=libreria
server.port=1500
spring.datasource.url=jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

## Rutas y Endpoints

### API REST (Consumo de Datos en formato JSON)

**Gestión de Libros**

* `GET /libros`: Consulta el inventario general.
* `GET /libros/{isbn}`: Búsqueda de libro por código ISBN.
* `GET /libros/autor/{autor}`: Filtra libros escritos por un autor en particular.
* `POST /libros`: Ingresa un nuevo título al sistema.
* `PUT /libros/{isbn}`: Modifica los atributos de un libro existente.
* `DELETE /libros/{isbn}`: Borra un libro del catálogo.

**Gestión de Usuarios**

* `GET /usuarios`: Consulta el padrón completo de socios.
* `GET /usuarios/{id}`: Búsqueda de socio por número de identificación.
* `GET /usuarios/nombre/{nombre}`: Localiza usuarios por aproximación de nombre.
* `GET /usuarios/correo/{correo}`: Localiza usuarios por dirección de email.
* `POST /usuarios`: Inscribe un nuevo socio.
* `PUT /usuarios/{id}`: Actualiza el expediente de un usuario.
* `DELETE /usuarios/{id}`: Elimina la membresía de un socio.

**Gestión de Préstamos**

* `GET /prestamos`: Extrae el historial global de préstamos.
* `GET /prestamos/{id}`: Consulta un folio de préstamo específico.
* `POST /prestamos`: Efectúa la salida de un libro hacia un usuario.
* `PUT /prestamos/{id}/devolver`: Consolida el retorno de un libro al acervo.
* `DELETE /prestamos/{id}`: Suprime un registro de préstamo.

---

## Muestras de Peticiones y Respuestas (Payloads)

### A. Registro de una Obra Literaria (POST `/libros`)

**Formato de envío (JSON):**

```json
{
  "titulo": "Python para Todos",
  "autor": "Raúl González",
  "añoPublicacion": "2021",
  "numeroEjemplares": 8
}

```

**Estructura de respuesta exitosa:**

```json
{
  "isbn": 2,
  "titulo": "Python para Todos",
  "autor": "Raúl González",
  "añoPublicacion": "2021",
  "numeroEjemplares": 8
}

```

### B. Inscripción de un Nuevo Socio (POST `/usuarios`)

**Formato de envío (JSON):**

```json
{
  "nombre": "Ana",
  "apellidos": "Martínez Silva",
  "telefono": "2299887766",
  "direccion": "Boulevard Mártires 456",
  "fechaNacimiento": "1999-08-12",
  "estado": "Activo",
  "correo": "ana.martinez@estudiante.edu",
  "contrasena": "claveSegura789",
  "rol": "Alumno",
  "fechaCreacion": "2026-10-08"
}

```

**Estructura de respuesta exitosa:**

```json
{
  "id": 2,
  "nombre": "Ana",
  "apellidos": "Martínez Silva",
  "telefono": "2299887766",
  "direccion": "Boulevard Mártires 456",
  "fechaNacimiento": "1999-08-12",
  "estado": "Activo",
  "correo": "ana.martinez@estudiante.edu",
  "contrasena": "claveSegura789",
  "rol": "Alumno",
  "fechaCreacion": "2026-10-08"
}

```

### C. Autorización de un Préstamo (POST `/prestamos`)

**Formato de envío (JSON):**

```json
{
  "libro": {
    "isbn": 2
  },
  "socio": {
    "id": 2
  }
}

```

**Estructura de respuesta exitosa:**

```json
{
  "id": 2,
  "libro": {
    "isbn": 2,
    "titulo": "Python para Todos",
    "autor": "Raúl González",
    "añoPublicacion": "2021",
    "numeroEjemplares": 7
  },
  "socio": {
    "id": 2,
    "nombre": "Ana",
    "apellidos": "Martínez Silva",
    "telefono": "2299887766",
    "direccion": "Boulevard Mártires 456",
    "fechaNacimiento": "1999-08-12",
    "estado": "Activo",
    "correo": "ana.martinez@estudiante.edu",
    "contrasena": "claveSegura789",
    "rol": "Alumno",
    "fechaCreacion": "2026-10-08"
  },
  "fechaPrestamo": "2026-10-08T01:15:00",
  "fechaDevolucion": null
}

```

```

```
