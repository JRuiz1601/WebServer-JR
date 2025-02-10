# Servidor Web Multi-Hilos con Thread Pool

## 📌 Información del Proyecto

**Nombre:** Juan Esteban Ruiz Ome\
**Código:** A00399562\
**Puerto usado:** 6789\
**Archivos Servidos:**

- `index.html` (Página por defecto)
- `cr7.gif` (Imagen adicional en la carpeta `www`)

---

## 🚀 Cómo Probar el Servidor Web

### 1️⃣ **Preparar el Entorno**

Antes de ejecutar el servidor, asegúrate de que tienes los siguientes archivos en la carpeta `www`:

📁 **Estructura del Proyecto:**

```
📂 Proyecto
 ├── 📂 www
 │    ├── index.html
 │    ├── cr7.gif
 ├── MultiThreadWebServer.java
 ├── ClienteHandler.java
 ├── README.md
```



### 2️⃣ **Compilar y Ejecutar el Servidor**

Desde la terminal, compila y ejecuta el servidor:

```sh
javac MultiThreadWebServer.java ClienteHandler.java
java MultiThreadWebServer
```

Deberías ver algo como:

```
Servidor web multi-hilos escuchando en el puerto 6789
```

---

### 3️⃣ **Acceder al Servidor desde un Navegador**

Abre tu navegador favorito y visita:

- 🌐 **Página principal:** [http://localhost:6789/](http://localhost:6789/)
- 🖼️ **Imagen de CR7:** [http://localhost:6789/cr7.gif](http://localhost:6789/cr7.gif)

---

