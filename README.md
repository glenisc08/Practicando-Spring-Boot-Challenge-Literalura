# LiterAlura - Catálogo de Libros y Autores

Este proyecto es un catálogo de libros y autores desarrollado como parte del desafío "LiterAlur". Permite interactuar con la API de Gutendex para buscar libros, así como gestionar y consultar la información de libros y autores en una base de datos local (PostgreSQL).

## 📚 Funcionalidades

La aplicación ofrece un menú interactivo en consola con las siguientes opciones:

1.  **Buscar libro por título:** Permite al usuario buscar un libro en la API de Gutendex. Si el libro y su autor no existen en la base de datos local, se guardan automáticamente.
2.  **Listar libros registrados:** Muestra todos los libros que han sido guardados en la base de datos local.
3.  **Listar autores registrados:** Muestra todos los autores únicos que han sido guardados en la base de datos local.
4.  **Listar autores vivos en un determinado año:** Permite al usuario ingresar un año y lista todos los autores registrados en la base de datos que estaban vivos en ese año.
5.  **Listar libros por idioma:** Permite al usuario seleccionar un idioma (ej. `es`, `en`, `fr`, `pt`) y lista todos los libros registrados en la base de datos en ese idioma.
6.  **Top 10 libros más descargados:** Muestra una lista de los 10 libros con el mayor número de descargas de los que están registrados en la base de datos.
7.  **Generar estadísticas de libros:** Proporciona estadísticas sobre el número de descargas de los libros registrados (conteo, mínimo, máximo, promedio y suma).
0.  **Salir:** Cierra la aplicación.

## ⚙️ Tecnologías Utilizadas

* **Java 23**
* **Spring Boot 3.2.3**
    * Spring Data JPA
    * Spring Web (para HttpClient implícito)
* **PostgreSQL** (Base de Datos Relacional)
* **Maven** (Gestor de Proyectos y Dependencias)
* **Jackson** (Para mapeo JSON)
* **API Gutendex:** Para la búsqueda y obtención de datos de libros.

## 🚀 Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/](https://github.com/)<tu-usuario>/LiterAlura-Challenge.git
    cd LiterAlura-Challenge
    ```
2.  **Configurar la Base de Datos PostgreSQL:**
    * Asegúrate de tener una instancia de PostgreSQL en ejecución.
    * Crea una base de datos. Por ejemplo: `CREATE DATABASE literalura_db;`
    * Configura las credenciales de tu base de datos en el archivo `src/main/resources/application.properties` (o `application.yml`):
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
        spring.datasource.username=<tu_usuario_postgres>
        spring.datasource.password=<tu_password_postgres>
        spring.jpa.hibernate.ddl-auto=update # o create si quieres que se creen las tablas automáticamente al inicio
        spring.jpa.show-sql=true
        spring.jpa.properties.hibernate.format_sql=true
        ```
        *Asegúrate de reemplazar `<tu_usuario_postgres>` y `<tu_password_postgres>` con tus credenciales.*
3.  **Compilar y Ejecutar:**
    * Desde la línea de comandos en la raíz del proyecto:
        ```bash
        ./mvnw spring-boot:run
        ```
    * O ábrelo en tu IDE (IntelliJ IDEA recomendado) y ejecuta la clase `LiteraluraApplication.java`.
  
* ## 🗃️ Base de Datos

Para la persistencia de datos, el proyecto utiliza PostgreSQL. Aquí se muestran capturas de pantalla de las tablas principales y algunos de sus datos:

### Tabla `libros`
Esta tabla almacena la información detallada de cada libro consultado y guardado.


### Tabla `autores`
Esta tabla contiene los datos de los autores asociados a los libros.


## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si encuentras un error o tienes una mejora, por favor, abre un 'issue' o envía un 'pull request'.


