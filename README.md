# Agrega Firebase al proyecto de Android

    ## [Opción 1: Agrega Firebase mediante Firebase console](https://firebase.google.com/docs/android/setup?hl=es-419)

 

# Guía de Implementación: App de Tareas con Firebase y Jetpack Compose

Este documento resume los pasos realizados para configurar y desarrollar la aplicación de gestión de tareas.

## Paso 1: Configuración del Entorno y Dependencias
*   **Actualización de Catálogo de Librerías (`libs.versions.toml`)**: Se añadieron las versiones y definiciones para Firebase (Auth y Firestore), Navigation Compose y Lifecycle ViewModel Compose.
*   **Configuración del Proyecto (`build.gradle.kts` raíz)**: Se incluyó el plugin de Google Services.
*   **Configuración del Módulo (`app/build.gradle.kts`)**: Se aplicaron los plugins necesarios y se declararon las dependencias de Firebase, Navegación y Material3.
*   **Sincronización de Gradle**: Se ejecutó un "Gradle Sync" para descargar las librerías necesarias.

## Paso 2: Definición del Modelo de Datos
*   **Creación de la clase `Task`**: Se definió un modelo de datos en Kotlin para representar una tarea. Los campos incluidos son:
    *   `id`: Identificador único del documento en Firestore.
    *   `title`: Título de la tarea.
    *   `description`: Descripción detallada.
    *   `date`: Fecha límite o de creación.
    *   `completed`: Estado de la tarea (booleano).
    *   `userId`: Referencia al ID del usuario que creó la tarea para garantizar la privacidad de los datos.

## Paso 3: Implementación de la Lógica de Negocio (ViewModels)
*   **`AuthViewModel`**: 
    *   Implementación de funciones para el registro de nuevos usuarios (`createUserWithEmailAndPassword`).
    *   Funciones para el inicio de sesión (`signInWithEmailAndPassword`).
    *   Gestión del estado de la sesión actual mediante un `MutableState`.
*   **`TaskViewModel`**:
    *   Conexión con Cloud Firestore.
    *   Implementación de un `SnapshotListener` para obtener actualizaciones en tiempo real de las tareas del usuario logueado.
    *   Funciones CRUD: añadir, actualizar (marcar como completada/editar texto) y eliminar documentos de la colección "tasks".

## Paso 4: Desarrollo de la Interfaz de Usuario (Screens)
*   **`LoginScreen`**: Interfaz con campos de texto para email y contraseña, y navegación hacia el registro.
*   **`RegisterScreen`**: Interfaz similar al login para la creación de nuevas cuentas.
*   **`HomeScreen`**: 
    *   Uso de `Scaffold` con una `TopAppBar` para el cierre de sesión.
    *   `LazyColumn` para listar las tareas de forma eficiente.
    *   `FloatingActionButton` para abrir un diálogo de creación.
    *   Diálogos dinámicos (`AlertDialog`) compartidos para las acciones de añadir y editar tareas.

## Paso 5: Configuración de la Navegación y Flujo de la App
*   **Control de Navegación**: Uso de `rememberNavController` y `NavHost` en la `MainActivity`.
*   **Rutas de Navegación**: Definición de los destinos "login", "register" y "home".
*   **Lógica de Inicio**: La aplicación verifica si existe un usuario autenticado al arrancar para decidir si muestra la pantalla de inicio o la de login.
*   **Gestión de Pilas de Pantallas**: Configuración de `popUpTo` para evitar que el usuario regrese al login/registro una vez autenticado.

---
**Nota:** Para que el proyecto funcione, es imprescindible descargar el archivo `google-services.json` de la consola de Firebase y ubicarlo en la carpeta `/app`.
