# 🔄 ConvertMe

> Convierte imágenes, audio y vídeo a cualquier formato — directamente desde el navegador.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)
![Nginx](https://img.shields.io/badge/Nginx-Alpine-009639?style=flat-square&logo=nginx)

---

## ✨ ¿Qué es ConvertMe?

ConvertMe es una aplicación web full-stack que permite convertir archivos multimedia entre distintos formatos sin instalar nada, sin registrarse y sin límites. Todo el procesamiento ocurre en el servidor, devolviendo el archivo listo para descargar.

---

## 🖼️ Capturas

> *(Añade aquí una captura de pantalla de la UI)*

---

## ⚙️ Funcionalidades

### 🖼️ Imágenes
Convierte entre **JPG · PNG · WEBP · GIF · BMP · TIFF**

### 🎵 Audio
Convierte entre **MP3 · WAV · OGG · AAC · FLAC**

### 🎬 Vídeo
Convierte entre **MP4 · AVI · MOV · MKV · WEBM**

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────┐
│                  Usuario                    │
│            (Navegador web)                  │
└─────────────────┬───────────────────────────┘
                  │ HTTP
┌─────────────────▼───────────────────────────┐
│            Frontend — Nginx                 │
│         HTML · CSS · JavaScript             │
│    Proxy inverso hacia el backend           │
└─────────────────┬───────────────────────────┘
                  │ /api/*
┌─────────────────▼───────────────────────────┐
│          Backend — Spring Boot              │
│                                             │
│  /api/convert/image  → ImageConverterService│
│  /api/convert/audio  → AudioConverterService│
│  /api/convert/video  → VideoConverterService│
│                                             │
│  Librerías: ImageIO · JAVE2 (FFmpeg)        │
└─────────────────────────────────────────────┘
```

Cada pieza corre en su propio contenedor Docker, orquestados con Docker Compose.

---

## 🛠️ Tecnologías

| Capa | Tecnología | Por qué |
|---|---|---|
| Backend | Java 17 + Spring Boot 3.2 | Framework robusto para APIs REST |
| Conversión imágenes | Java ImageIO + webp-imageio | Soporte nativo + WebP |
| Conversión audio/vídeo | JAVE2 (FFmpeg) | El estándar en conversión multimedia |
| Frontend | HTML + CSS + JavaScript vanilla | Sin frameworks, ligero y rápido |
| Servidor web | Nginx Alpine | Sirve el frontend y hace de proxy |
| Contenedores | Docker + Docker Compose | Despliegue reproducible en cualquier máquina |

---

## 🚀 Cómo ejecutarlo en local

### Requisitos
- [Docker Desktop](https://www.docker.com/products/docker-desktop) instalado y arrancado

### Pasos

```bash
# 1. Clona el repositorio
git clone https://github.com/TUUSUARIO/convertme.git
cd convertme

# 2. Levanta todo con un solo comando
docker-compose up --build
```

### Accede a la app

```
http://localhost:3000
```

La primera vez tarda 2-3 minutos porque Docker compila el proyecto Java. Las siguientes veces arranca en segundos gracias a la caché.

---

## 📁 Estructura del proyecto

```
convertme/
├── docker-compose.yml
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/converter/
│       ├── ConvertmeApplication.java
│       ├── ImageConverterController.java
│       ├── ImageConverterService.java
│       ├── AudioConverterService.java
│       └── VideoConverterService.java
└── frontend/
    ├── Dockerfile
    ├── nginx.conf
    └── index.html
```

---

## 🔌 API REST

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/convert/image` | Convierte una imagen |
| POST | `/api/convert/audio` | Convierte un audio |
| POST | `/api/convert/video` | Convierte un vídeo |

### Ejemplo de llamada

```bash
curl -X POST http://localhost:8888/api/convert/image \
  -F "file=@foto.png" \
  -F "format=webp" \
  --output foto.webp
```

---

## 🐳 Comandos Docker útiles

```bash
# Arrancar todo
docker-compose up --build

# Parar todo
docker-compose down

# Reconstruir solo el frontend
docker-compose up --build frontend

# Reconstruir solo el backend
docker-compose up --build backend

# Ver logs
docker-compose logs backend
docker-compose logs frontend
```

---

## 👨‍💻 Autor

**Víctor Daniel** — [LinkedIn](https://linkedin.com/in/TUPERFIL) · [GitHub](https://github.com/TUUSUARIO)

---

## 📄 Licencia

MIT — úsalo, modifícalo y compártelo libremente.
