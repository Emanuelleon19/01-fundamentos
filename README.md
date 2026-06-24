# Práctica 1 - Spring Boot: Instalación y Primer Endpoint

**Autor:** Emanuel Leon  
**Materia:** Programación y Plataformas Web  
**Universidad:** Universidad Politécnica Salesiana

---

## 1. Verificación de Java

![java-version](assets/java-version.png)

---

## 2. Servidor Spring Boot ejecutándose

![servidor](assets/servidor.png)

---

## 3. Endpoint `/api/status` funcionando

![endpoint](assets/endpoint.png)

---

## 4. Archivo StatusController.java

![ls](assets/ls.png)

---

## 5. Explicación

El endpoint `/api/status` responde a peticiones GET y devuelve un JSON con el nombre del servicio, su estado y la fecha y hora actual. Sirve para verificar que la aplicación está corriendo correctamente.

Spring Boot facilita la creación de servidores web en Java al incluir un servidor Tomcat embebido que se inicia automáticamente con la aplicación, sin necesidad de configuraciones externas.

# Práctica 3: API Rest
## Capturas 18/06/2026

#### 1.- Localhost del nuevo recurso Students:

![Creacion de Students](assets/students.png)

#### 2.- Students/count:

![Conteo de Students](assets/count.png)

# Práctica 5 (Spring Boot): Persistencia real con PostgreSQL, Entidades JPA y Repositorios

## Evidencias:

#### 1.- Aplicación Docker Desktop:

![Software de Docker funcionando](assets/docker.png)

#### 2.- Verificación en PostgreSQL:

![Comando para visualizar al usuario](assets/confirmacion.png)

### Creación de clases en ```\products```:

## Evidencias:

#### 1.- Visualización de los 5 productos:

![5 Productos](assets/products.png)

**Descripción:** Se insertaron 5 registros de productos a través de BRUNO.

#### 2.- Verificación de un producto en especifico:

![Producto en específico](assets/especifico.png)


#### 3.- Verificación en PostgreSQL:

![Comando para visualizar los productos](assets/05-lista-productos.png)



## Flujo de datos: API REST ↔ PostgreSQL

Cuando el cliente realiza una petición HTTP con un JSON, el `ProductsController` recibe los datos mediante un `CreateProductDto`. Este pasa al `ProductServiceImpl`, que usa `ProductMapper` para transformarlo a `ProductEntity`. Al ser `ProductEntity` hija de `BaseEntity`, los callbacks `@PrePersist` y `@PreUpdate` asignan automáticamente los campos de auditoría (`id`, `createdAt`, `updatedAt`). Finalmente el `ProductRepository` ejecuta `.save()` y Hibernate traduce la entidad a un `INSERT` en PostgreSQL.

En el sentido inverso, PostgreSQL retorna el registro, Hibernate lo mapea a `ProductEntity`, el `ProductMapper` lo convierte a `ProductResponseDto` y el controlador lo devuelve al cliente como JSON.

`BaseEntity` centraliza los campos comunes evitando duplicación en cada entidad del proyecto.