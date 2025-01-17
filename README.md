# CHALLENGE LITERALURA 

###Challenge de práctica en java con Spring Boot

Este desafío consiste en construir un catálogo de libros, en el que se puedan realizar solicitudes a la API de libros Gutendex y muestre los resultados de la busqueda.

El catálogo de libros proporciona un menú principal de opciones para interactuar. Los libros se buscarán a través de la API mencionada anteriormente y luego muestra los resultados de la consulta, como tambien mensajes de atención en caso de que haya ingresos invalidos o no se encuentre la información solicitada.

Los pasos para completar este desafío se detallan a continuación:

- Configuración del Ambiente Java.

- Creación del Proyecto.

- Consumo de la API.

- Análisis de la Respuesta JSON.

- Inserción y consulta en la base de datos.

- Exibición de resultados a los usuarios.

- Configurar la base de datos


##Antes de ejecutar el programa:

Esta aplicación utiliza una base de datos, por lo que se debe crear una base de datos local y configurar el archivo src/main/resources/application.properties.
En el archivo hay que reemplazar lo siguiente:

- {DB_NAME}: Aqui tiene que ingresar el nombre de la base de datos creada.

- {DB_HOST}: el nombre del host de la base de datos (por ejemplo: jdbc:postgresql://localhost/{DB_NAME}, en caso de que usen postgresql).

- {DB_USERNAME}: el nombre del usuario de la base datos que utiliza (postgres asigna el nombre de "postgre" como determinado).

- {DB_PASSWORD}: contraseña asignada de tu cuenta para ingresar a la base de datos.


##Programas utilizados para configurar el entorno de desarrollo de la aplicación:
- Java JDK 17
- Maven: versión 4 en adelante
- Spring Boot: versión 3.2.3
- IDE (Entorno de desenvolvimento integrado) IntelliJ IDEA
- PostgreSQL: versión 16 en adelante
