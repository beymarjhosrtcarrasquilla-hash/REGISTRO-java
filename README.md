# Informe Técnico Detallado y Comparativo: `REGISTRO.java`

**ID de Misión:** `20261122-006`  
**Tipo de auditoría:** Análisis estático 100% de solo lectura (sin compilación, ejecución ni capturas)  
**Fuentes auditadas:**
- **Códigos originales del profesor:** `C:\Users\beyma\Mi unidad\areas\estudio\ingeniería en sistemas\2026\3 semestre\Seminario de sistemas\Actividades_Seminario_Sistemas\Códigos de aplicación de registro de clases` (9 archivos `.java`)
- **Proyecto del usuario (versión base seleccionada — más reciente y completa):** `C:\Users\beyma\Mi unidad\areas\estudio\ingeniería en sistemas\2026\3 semestre\Seminario de sistemas\Mis_Trabajos_Seminario_Sistemas\REGISTRO.java\REGISTRO.java` (18 archivos `.java`)
- **Otras copias verificadas:**
  - `C:\Users\beyma\Documents\NetBeansProjects\REGISTRO.java` (18 archivos `.java`; idéntica salvo versiones ligeramente anteriores de `ConfiguracionGui.java` y `Validaciones.java`).
  - `C:\Users\beyma\Mi unidad\areas\estudio\ingeniería en sistemas\2026\3 semestre\Seminario de sistemas\Mis_Trabajos_Seminario_Sistemas\REGISTRO_java_actualizado\REGISTRO.java` (carpeta `src/` sin archivos `.java`).

---

## Índice General Navegable

1. [Sección 1: Resumen Ejecutivo y Veredicto Comparativo](#sección-1-resumen-ejecutivo-y-veredicto-comparativo)
   - [1.1 Veredicto General](#11-veredicto-general)
   - [1.2 Clasificación de Clases (Idénticas, Adaptadas, Nuevas)](#12-clasificación-de-clases-idénticas-adaptadas-nuevas)
   - [1.3 Tabla Comparativa Completa](#13-tabla-comparativa-completa)
2. [Sección 2: Funcionamiento General y Arquitectura del Programa](#sección-2-funcionamiento-general-y-arquitectura-del-programa)
   - [2.1 Propósito del Sistema y Patrón Arquitectónico](#21-propósito-del-sistema-y-patrón-arquitectónico)
   - [2.2 Árbol de Carpetas y Paquetes](#22-árbol-de-carpetas-y-paquetes)
   - [2.3 Dependencias y Librerías](#23-dependencias-y-librerías)
   - [2.4 Base de Datos SQLite (`datos/RegBD.db`)](#24-base-de-datos-sqlite-datosregbddb)
   - [2.5 Seguridad Criptográfica y Persistencia Binaria](#25-seguridad-criptográfica-y-persistencia-binaria)
   - [2.6 Ciclo de Vida de la Aplicación](#26-ciclo-de-vida-de-la-aplicación)
3. [Sección 3: Mapa de Ventanas y Correlación entre Formularios](#sección-3-mapa-de-ventanas-y-correlación-entre-formularios)
   - [3.1 Inventario de Formularios (`VISTA`)](#31-inventario-de-formularios-vista)
   - [3.2 Diagrama de Flujo y Navegación](#32-diagrama-de-flujo-y-navegación)
   - [3.3 Correlación Detallada: Ventanas, Constructores, Eventos y Capas](#33-correlación-detallada-ventanas-constructores-eventos-y-capas)
4. [Sección 4: Análisis Profundo por Paquete, Clase y Método](#sección-4-análisis-profundo-por-paquete-clase-y-método)
   - [4.1 Paquete `MODELO`](#41-paquete-modelo)
   - [4.2 Paquete `CONTROL`](#42-paquete-control)
   - [4.3 Paquete `VISTA`](#43-paquete-vista)
5. [Sección 5: Observaciones Técnicas del Análisis Estático](#sección-5-observaciones-técnicas-del-análisis-estático)

---

## Sección 1: Resumen Ejecutivo y Veredicto Comparativo

### 1.1 Veredicto General

**Se confirma que el proyecto del usuario conserva y reutiliza íntegramente la base arquitectónica y las 9 clases originales entregadas por el profesor**, evolucionándolas desde un conjunto de clases de consola/prueba hacia una aplicación de escritorio completa en **Java 21 + Swing + SQLite** con soporte multiusuario.

- **Total de clases `.java` del profesor:** 9
- **Clases del profesor conservadas en el proyecto del usuario:** 9 (100% de las clases base)
  - **Idénticas (100%):** 3 (`Usuario.java`, `Configuracion.java`, `SEGURIDAD.java`)
  - **Adaptadas / Extendidas:** 6 (`Conexion.java`, `Persistencia.java`, `SeguridadDAO.java`, `Registro.java`, `ControlRegistro.java`, `recordar2GUI.java`)
- **Complementos nuevos desarrollados por el usuario:** 9 clases `.java` adicionales (4 en `CONTROL` y 5 en `VISTA` con sus respectivos `.form` de NetBeans Matisse), alcanzando un total de **18 clases `.java`**.

### 1.2 Clasificación de Clases (Idénticas, Adaptadas, Nuevas)

#### a) Clases idénticas al original del profesor
1. `MODELO/Usuario.java` (2,325 B, 91 líneas): conserva intactos atributos (`codigo`, `password`, `claves[4]`), constructores, getters/setters y método `main` de prueba.
2. `MODELO/Configuracion.java` (14,969 B, 544 líneas): conserva los datos del docente (`identificacion`, `apellidos`, `nombres`, `programa`, `nroAsignaturas`) y los 14 arreglos paralelos por asignatura/grupo.
3. `CONTROL/SEGURIDAD.java` (4,169 B, 145 líneas): conserva sin cambios el cifrado autenticado `AES/GCM/NoPadding` con llaves de 256 bits, IV de 12 bytes y tag de 128 bits.

#### b) Clases del profesor adaptadas o extendidas
1. `CONTROL/Conexion.java`: pasa de una única ruta fija `jdbc:sqlite:C:/MIBD/RegBD.db` a una estrategia dual (usa `C:/MIBD/RegBD.db` si existe; si no, crea la carpeta local `datos/` y usa `jdbc:sqlite:datos/RegBD.db`), carga explícitamente `org.sqlite.JDBC` y añade `crearTablas(Connection)` para autogenerar y migrar las 4 tablas (`registro`, `seguridad`, `docente`, `asignatura_config`).
2. `CONTROL/Persistencia.java`: cambia la ruta por defecto de `"c:/temp/datos/"` a `"datos/"` y agrega `c.mkdirs()` en `abrirArchivoSalida()` para evitar `FileNotFoundException` si la carpeta no existe.
3. `CONTROL/SeguridadDAO.java`: corrige la lectura en `buscar(String)` y `consultarTodos()` para recuperar también las 4 columnas `CL1, CL2, CL3, CL4` (el original solo leía `usuario` y `password`, lo que impedía verificar imágenes de recuperación desde BD).
4. `MODELO/Registro.java`: mantiene intactos los 19 atributos y el constructor del profesor, añadiendo 3 atributos de soporte para la interfaz y multiusuario (`tipoEncuentro`, `docenteId`, `usuario`) con sus getters/setters y actualizando `toString()`.
5. `CONTROL/ControlRegistro.java`: amplía la llave del `TreeMap` de `ASIGNATURA-NRO` a `ASIGNATURA-GRUPO-NRO` (manteniendo sobrecargas de compatibilidad sin grupo), añade aislamiento por `usuario`, integra persistencia SQLite vía `RegistroDAO` (`cargarDesdeBD`, `guardarEnBD`, `modificarEnBD`, `eliminarEnBD`, `siguienteEncuentro`, `listar`) y cambia la ruta de prueba en `main` a `"datos/"`.
6. `VISTA/recordar2GUI.java`: conserva la matriz 4x4 de botones con imágenes (`aleon.jpg`..`vtren.jpg`) y el arreglo estático `clV[4]`, pero migra el diseño a NetBeans Matisse (`panelCuadricula`, `panelPie`), añade fallback de rutas (`getResource("/imagenes/...")` y `imagenes/`), borde verde de selección, `btnReiniciar`, `btnConfirmar`, `isConfirmado()` y `getSeleccion()`.

#### c) Complementos nuevos creados por el usuario
- **`CONTROL` (4 clases):**
  - `ConfiguracionDAO.java`: CRUD de `Configuracion` en SQLite (`docente` + `asignatura_config`), aislamiento por usuario, creación automática de configuración inicial y `ejemplo()`.
  - `ControlSeguridad.java`: fachada criptográfica y de negocio sobre `SeguridadDAO`, `SEGURIDAD` y `Persistencia` (`registrar`, `validarLogin`, `verificarImagenes`, `cambiarPassword`, migración transparente de texto plano a cifrado).
  - `RegistroDAO.java`: DAO SQLite completo para `Registro` (adicionar, modificar, eliminar, buscar, existe, consultarTodos, siguienteEncuentro).
  - `Validaciones.java`: utilidades de validación de cadenas, enteros, reales, correos, teléfonos y métodos de lectura por consola (incluye `estaVacio` heredado/simple y métodos modernos `esVacio`, `esEnteroPositivo`, etc.).
- **`VISTA` (5 ventanas/diálogos + sus `.form`):**
  - `LoginGui.java`: diálogo modal de inicio de sesión, registro de nuevo usuario (con selección de 4 imágenes) y recuperación de contraseña (máx. 3 intentos).
  - `VentanaPrincipal.java`: `JFrame` principal con barra lateral, tabla de registros recientes/todos, menú contextual (clic derecho), doble clic, tecla `Supr` y diálogos de consulta (perfil, horario, asignaturas, grupos).
  - `ConfiguracionGui.java`: diálogo modal para editar datos del docente y tabla dinámica de hasta 6 grupos/asignaturas, con persistencia dual (SQLite + `.dat`).
  - `RegistroGui.java`: diálogo modal para crear o modificar un `Registro` de clase, con autocompletado de sede, vocero y siguiente número de encuentro.
  - `SelectorFecha.java`: calendario visual modal hecho en Swing puro (`LocalDate`) sin librerías externas.

### 1.3 Tabla Comparativa Completa

| Archivo | Paquete | Estado | Similitud aprox. | Observaciones clave |
| :--- | :--- | :--- | :---: | :--- |
| `Usuario.java` | `MODELO` | **Idéntico** | 100% | Sin cambios respecto al original del profesor. |
| `Configuracion.java` | `MODELO` | **Idéntico** | 100% | Sin cambios respecto al original del profesor. |
| `Registro.java` | `MODELO` | **Adaptado** | ~90% | Conserva 19 campos y constructor; añade `tipoEncuentro`, `docenteId`, `usuario`. |
| `SEGURIDAD.java` | `CONTROL` | **Idéntico** | 100% | Cifrado AES-256-GCM intacto. |
| `Conexion.java` | `CONTROL` | **Adaptado** | ~25% | Añade ruta relativa `datos/RegBD.db`, fallback a `C:/MIBD/RegBD.db` y `crearTablas()`. |
| `Persistencia.java` | `CONTROL` | **Adaptado** | ~92% | Cambia carpeta por defecto a `datos/` y crea directorio con `mkdirs()`. |
| `SeguridadDAO.java` | `CONTROL` | **Adaptado** | ~92% | `buscar()` y `consultarTodos()` ahora leen `CL1..CL4`. |
| `ControlRegistro.java` | `CONTROL` | **Adaptado** | ~75% | Llave incluye `grupo`; añade `usuario`, `RegistroDAO` y métodos `*EnBD()`. |
| `ConfiguracionDAO.java` | `CONTROL` | **Nuevo complemento** | 0% | Persistencia SQLite de `Configuracion` por usuario. |
| `ControlSeguridad.java` | `CONTROL` | **Nuevo complemento** | 0% | Orquesta login, registro, cifrado AES y recuperación por imágenes. |
| `RegistroDAO.java` | `CONTROL` | **Nuevo complemento** | 0% | CRUD SQLite de `Registro` filtrado por `usuario`. |
| `Validaciones.java` | `CONTROL` | **Nuevo complemento** | 0% | Validaciones regex/numéricas y utilidades de entrada. |
| `recordar2GUI.java` | `VISTA` | **Adaptado** | ~45% | Mantiene matriz 4x4 y `clV`; agrega confirmación, reinicio y rutas locales. |
| `LoginGui.java` | `VISTA` | **Nuevo complemento** | 0% | Pantalla de login, alta de usuario y recuperación de clave. |
| `VentanaPrincipal.java` | `VISTA` | **Nuevo complemento** | 0% | Punto de entrada (`main`), dashboard y tabla CRUD de registros. |
| `ConfiguracionGui.java` | `VISTA` | **Nuevo complemento** | 0% | Editor de configuración docente y grupos (1 a 6 columnas). |
| `RegistroGui.java` | `VISTA` | **Nuevo complemento** | 0% | Formulario de creación y edición de registros de clase. |
| `SelectorFecha.java` | `VISTA` | **Nuevo complemento** | 0% | Selector de fecha mensual en Swing puro. |

---

## Sección 2: Funcionamiento General y Arquitectura del Programa

### 2.1 Propósito del Sistema y Patrón Arquitectónico

`REGISTRO.java` es una aplicación de escritorio para la **gestión y control de registro de clases docentes**. Permite a cada docente autenticarse con usuario y contraseña, configurar sus datos personales y hasta 6 grupos/asignaturas (con horario, sede, plataforma y datos del vocero), y registrar o editar el desarrollo de cada encuentro académico (fecha, número de clase, tipo, plataforma de video, inasistencias, temas dictados, actividades y observaciones).

El proyecto sigue una arquitectura clásica de **3 capas por paquetes**:
- **`MODELO`**: Entidades de dominio serializables (`Usuario`, `Configuracion`, `Registro`).
- **`CONTROL`**: Lógica de negocio, seguridad criptográfica (`SEGURIDAD`, `ControlSeguridad`), serialización (`Persistencia`), conexión (`Conexion`), acceso a datos SQLite (`*DAO`) y validaciones (`Validaciones`).
- **`VISTA`**: Interfaces gráficas Swing diseñadas con NetBeans Matisse (`.java` + `.form`).

### 2.2 Árbol de Carpetas y Paquetes

```text
REGISTRO.java/
├── LEEME.txt                          # Guía de instalación, BD y uso
├── build.xml / manifest.mf            # Configuración de compilación Ant/NetBeans
├── datos/                             # Persistencia en tiempo de ejecución
│   ├── RegBD.db                       # Base de datos SQLite principal
│   ├── secretKey.dat                  # Llave maestra AES-256 serializada
│   └── config_<usuario>.dat           # Respaldo serializado de configuración
├── imagenes/                          # 16 imágenes de desafío (aleon.jpg .. vtren.jpg)
├── lib/
│   ├── AbsoluteLayout.jar             # Layout usado por formularios Matisse
│   └── sqlite-jdbc-3.50.3.0.jar       # Driver JDBC de SQLite
├── nbproject/project.properties       # Java 21, main.class=VISTA.VentanaPrincipal
└── src/
    ├── CONTROL/                       # 9 clases de control, DAO y utilidades
    ├── MODELO/                        # 3 clases de entidad (Serializable)
    ├── VISTA/                         # 6 formularios Swing (.java + .form) + iconos PNG
    └── imagenes/                      # Copia de las 16 imágenes como recurso de classpath
```

### 2.3 Dependencias y Librerías

Según `nbproject/project.properties`:
- **Versión de Java:** `javac.source=21` y `javac.target=21` (permite *text blocks* `"""..."""` y *pattern matching* `instanceof`).
- **Driver SQLite:** `lib/sqlite-jdbc-3.50.3.0.jar` (el profesor proveía `sqlite-jdbc-3.40.0.0.jar`).
- **Layout Manager:** `lib/AbsoluteLayout.jar` (`org.netbeans.lib.awtextra.AbsoluteLayout`).
- **Bibliotecas estándar JDK:** `javax.swing.*`, `java.sql.*`, `javax.crypto.*`, `java.time.*`, `java.util.regex.*`.

### 2.4 Base de Datos SQLite (`datos/RegBD.db`)

Al invocar `Conexion.conectar()`, el sistema verifica si existe `C:/MIBD/RegBD.db` (ruta del profesor); en caso contrario, crea la carpeta `datos/` y abre `jdbc:sqlite:datos/RegBD.db`. Inmediatamente ejecuta `crearTablas(cn)` con `CREATE TABLE IF NOT EXISTS`:

1. **`seguridad`**:
   - `usuario TEXT PRIMARY KEY`, `password TEXT`, `CL1 TEXT`, `CL2 TEXT`, `CL3 TEXT`, `CL4 TEXT`
2. **`docente`**:
   - `usuario TEXT PRIMARY KEY`, `identificacion INTEGER`, `apellidos TEXT`, `nombres TEXT`, `programa TEXT`
3. **`asignatura_config`**:
   - `usuario TEXT`, `indice INTEGER`, `asignatura TEXT`, `grupo TEXT`, `dia_semana TEXT`, `hora INTEGER`, `modalidad TEXT`, `plataforma_lms TEXT`, `sede TEXT`, `programa_grupo TEXT`, `plata_video TEXT`, `est_matriculados INTEGER`, `vocero_id INTEGER`, `vocero_nombre TEXT`, `vocero_email TEXT`, `vocero_tel INTEGER`, `PRIMARY KEY (usuario, indice)`
4. **`registro`**:
   - `id INTEGER PRIMARY KEY AUTOINCREMENT`, `asignatura TEXT`, `grupo TEXT`, `nro_encuentro INTEGER`, `hora INTEGER`, `modalidad TEXT`, `plataforma_lms TEXT`, `sede TEXT`, `programa_grupo TEXT`, `plata_video TEXT`, `est_matriculados INTEGER`, `actividad TEXT`, `trabajo_realizado TEXT`, `ano INTEGER`, `mes INTEGER`, `dia INTEGER`, `nro_asistentes INTEGER`, `nro_no_asisten INTEGER`, `nombres_no_asisten TEXT`, `observaciones TEXT`, `tipo_encuentro TEXT`, `docente_id INTEGER`, `usuario TEXT`

### 2.5 Seguridad Criptográfica y Persistencia Binaria

- **Llave maestra:** `ControlSeguridad.llave()` carga `SecretKey` desde `datos/secretKey.dat` mediante `Persistencia`. Si el archivo no existe, genera una llave AES de 256 bits con `SEGURIDAD.generarLlave()` y la guarda en disco.
- **Cifrado:** `SEGURIDAD.encriptar(...)` usa `AES/GCM/NoPadding`, genera un IV automático por cada operación, concatena `IV (12 bytes) + textoCifrado + tag (128 bits)` y lo codifica en Base64. Tanto `password` como las 4 respuestas de imagen (`CL1`..`CL4`) se almacenan cifradas.
- **Migración automática:** Si un usuario antiguo tenía su contraseña o claves en texto plano, `ControlSeguridad.validarLogin()` detecta que no están cifradas y las cifra automáticamente en BD tras un inicio de sesión exitoso.

### 2.6 Ciclo de Vida de la Aplicación

1. **Arranque:** `VentanaPrincipal.main()` configura el *Look and Feel* Nimbus y abre `LoginGui(null, true)` de forma modal.
2. **Autenticación:** Si el usuario cancela o cierra `LoginGui` sin autenticarse, el programa finaliza con `System.exit(0)`. Si inicia sesión correctamente, se instancia `new VentanaPrincipal(login.getUsuarioAutenticado()).setVisible(true)`.
3. **Operación:** `VentanaPrincipal` crea un `ControlRegistro(usuario)`, carga los registros propios de ese usuario desde SQLite y muestra los 10 más recientes (o todos). Desde allí se abren modales para configurar al docente o registrar/editar clases.
4. **Cierre de sesión / Salida:** "Cerrar sesión" (`jButton3`) destruye la `VentanaPrincipal` actual y relanza `LoginGui`; "Salir" (`jButton4`) confirma y ejecuta `System.exit(0)`.

---

## Sección 3: Mapa de Ventanas y Correlación entre Formularios

### 3.1 Inventario de Formularios (`VISTA`)

| Clase | Tipo Swing | Archivo `.form` | Función principal |
| :--- | :--- | :--- | :--- |
| `VentanaPrincipal` | `JFrame` | `VentanaPrincipal.form` | Punto de entrada (`main`), panel de gestión y tabla de registros |
| `LoginGui` | `JDialog` (modal) | `LoginGui.form` | Autenticación, creación de cuenta y recuperación de contraseña |
| `recordar2GUI` | `JDialog` (modal) | `recordar2GUI.form` | Cuadrícula 4x4 de imágenes para clave de recuperación |
| `ConfiguracionGui` | `JDialog` (modal) | `ConfiguracionGui.form` | Edición de perfil docente y tabla de grupos/asignaturas |
| `RegistroGui` | `JDialog` (modal) | `RegistroGui.form` | Formulario para crear o modificar un encuentro de clase |
| `SelectorFecha` | `JDialog` (modal) | `SelectorFecha.form` | Calendario visual mensual para elegir fecha (`DD/MM/AAAA`) |

### 3.2 Diagrama de Flujo y Navegación

```text
[Inicio: VentanaPrincipal.main()]
        │
        ▼ (modal)
+---------------+  "Crear usuario" / "Olvidé mi contraseña"  +------------------+
|   LoginGui    | ─────────────────────────────────────────► |   recordar2GUI   |
+---------------+                                            | (4x4 imágenes)   |
        │                                                    +------------------+
        │ isAutenticado() == true
        ▼ (pasa String usuario)
+-----------------------------------------------------------------------+
|                       VentanaPrincipal(usuario)                       |
|  - Tabla jTable1 (doble clic / clic der. Modificar o Eliminar / Supr) |
|  - "Ver todos" / "Ver recientes"                                      |
|  - Botones consulta (JOptionPane): Perfil, Horario, Asignaturas,      |
|    Mis grupos                                                         |
+----+-------------------------------+-------------------------------+--+
     │                               │                               │
     │ Botón "Registrar Clase"       │ Botón "⚙"                     │ "Cerrar sesión"
     │ o doble clic / Modificar      │                               │
     ▼ (modal: ControlRegistro)      ▼ (modal: String usuario)       ▼
+------------------+         +--------------------+          [Dispose + nuevo LoginGui]
|   RegistroGui    |         |  ConfiguracionGui  |
+------------------+         +--------------------+
     │
     │ Clic en icono calendario (jLabel10)
     ▼ (modal: LocalDate inicial)
+------------------+
|  SelectorFecha   |
+------------------+
```

### 3.3 Correlación Detallada: Ventanas, Constructores, Eventos y Capas

| Origen | Evento / Acción | Destino y Constructor invocado | Parámetros pasados | Retorno / Efecto al cerrar | Capas `CONTROL` / `MODELO` usadas |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `VentanaPrincipal.main` | Arranque de app | `new LoginGui(null, true)` | `parent=null`, `modal=true` | Si `isAutenticado()` abre `VentanaPrincipal(usuario)`; si no, `System.exit(0)` | `ControlSeguridad`, `Usuario` |
| `LoginGui` | `btnCrearActionPerformed` -> `crearUsuario()` | `new recordar2GUI(null, true, "Imágenes de recuperación")` | `titulo` | `sel.isConfirmado()` y `sel.getSeleccion()` (`String[4]`) | `ControlSeguridad.registrar()` |
| `LoginGui` | `btnOlvideActionPerformed` -> `recuperar()` | `new recordar2GUI(null, true, "Recuperar contraseña")` | `titulo` | Hasta 3 intentos; valida con `verificarImagenes()` y cambia clave | `ControlSeguridad.verificarImagenes()`, `cambiarPassword()` |
| `VentanaPrincipal` | `jButton1` ("Registrar Clase") o `modificarSeleccionado()` | `new RegistroGui(this, true, control)` | `this`, `true`, `ControlRegistro control` (y `dialogo.setRegistro(r)` si edita) | Al cerrar llama `cargarTabla()` para refrescar `jTable1` | `ControlRegistro`, `ConfiguracionDAO`, `Registro`, `Configuracion` |
| `VentanaPrincipal` | `jButton2` ("⚙") -> `configurarDocente()` | `new ConfiguracionGui(this, true, control.getUsuario())` | `this`, `true`, `String usuario` | Edita/guarda docente y grupos en BD y `.dat` | `ConfiguracionDAO`, `Persistencia`, `Validaciones`, `Configuracion` |
| `VentanaPrincipal` | `jButton3` ("Cerrar sesion") | `dispose()` + `new LoginGui(null, true)` | — | Si autentica, abre nueva `VentanaPrincipal(usuario)` | `ControlSeguridad` |
| `VentanaPrincipal` | `jButton5/6/7/8` (Perfil, Grupos, Horario, Asignaturas) | `JOptionPane.showMessageDialog` | — | Muestra información o `JTable` de solo lectura | `ConfiguracionDAO`, `ControlRegistro` |
| `RegistroGui` | Clic en `jLabel10` -> `elegirFecha()` | `SelectorFecha.elegir(this, inicial)` | `Component padre`, `LocalDate inicial` | Devuelve `LocalDate` elegida (o `null`) y formatea `DD/MM/AAAA` en `jTextField3` | `java.time.LocalDate` |

---

## Sección 4: Análisis Profundo por Paquete, Clase y Método

### 4.1 Paquete `MODELO`

#### 1. `MODELO.Usuario`
- **Ubicación:** `src/MODELO/Usuario.java` (Idéntica al profesor)
- **Responsabilidad:** Representar las credenciales de acceso y las 4 palabras clave de recuperación por imágenes. Implementa `Serializable`.
- **Atributos:** `private String codigo`, `private String password`, `public String[] claves` (tamaño 4).
- **Constructores:** `Usuario()` (inicializa cadenas vacías y `claves = new String[4]`) y `Usuario(String codigo, String password)`.
- **Métodos principales:** Getters/setters (`getCodigo`, `setCodigo`, `getPassword`, `setPassword`, `getClaves`, `setClaves`), `toString()`, y `main(String[] args)` de prueba con `SEGURIDAD` y `Persistencia`.

```java
// Fragmento literal de src/MODELO/Usuario.java (líneas 21-30)
   public Usuario() {
        this.codigo = "";
        this.password = "";
        claves = new String[4];
    }
    public Usuario(String codigo, String password) {
        this.codigo = codigo;
        this.password = password;
        claves = new String[4];
    }
```

#### 2. `MODELO.Configuracion`
- **Ubicación:** `src/MODELO/Configuracion.java` (Idéntica al profesor)
- **Responsabilidad:** Almacenar la ficha del docente y la configuración de sus `nroAsignaturas` asignaturas/grupos mediante 14 arreglos paralelos. Implementa `Serializable`.
- **Atributos:** `identificacion`, `apellidos`, `nombres`, `programa`, `nroAsignaturas` (`private`), más los arreglos `public`: `Asignaturas`, `grupos`, `diaSemana`, `hora`, `modalidad`, `plataformaLMS`, `sede`, `programaGrupo`, `plataVideo`, `estMatriculados`, `voceroId`, `voceroApNombre`, `voceroemail`, `voceroTel`.
- **Constructor:** `Configuracion(int identificacion, String apellidos, String nombres, String programa, int nroAsignaturas)`.
- **Métodos principales:** Getters/setters indexados `(dato, indice)` para cada arreglo, `toString()` formateado y `main` de prueba con 5 asignaturas.

```java
// Fragmento literal de src/MODELO/Configuracion.java (líneas 27-43)
    public Configuracion(int identificacion,
                         String apellidos,
                         String nombres,
                         String programa,
                         int nroAsignaturas) {

        this.identificacion = identificacion;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.programa = programa;
        this.nroAsignaturas = nroAsignaturas;

        // Crear los arreglos
        Asignaturas = new String[nroAsignaturas];
        grupos = new String[nroAsignaturas];
        diaSemana = new String[nroAsignaturas];
        hora = new int[nroAsignaturas];
```

#### 3. `MODELO.Registro`
- **Ubicación:** `src/MODELO/Registro.java` (Adaptada del profesor)
- **Responsabilidad:** Modelar un encuentro o sesión de clase dictada. Implementa `Serializable`.
- **Atributos:** Los 19 originales (`asignatura`, `nroEncuentro`, `grupo`, `hora`, `modalidad`, `plataformaLMS`, `sede`, `programaGrupo`, `plataVideo`, `estMatriculados`, `actividad`, `trabajoRealizado`, `ano`, `mes`, `dia`, `nroAsistentes`, `nroNoAsisten`, `nombreNosAsisten`, `observaciones`) más 3 añadidos: `tipoEncuentro` (`"Hora estipulada"` por defecto), `docenteId` y `usuario`.
- **Constructor y Métodos:** Constructor de 19 parámetros (compatible con el del profesor), getters/setters para los 22 atributos y `toString()`.

```java
// Fragmento literal de src/MODELO/Registro.java (líneas 30-34)
  // Datos adicionales usados por la interfaz (no forman parte del constructor del profesor)
  private String tipoEncuentro = "Hora estipulada"; // Hora estipulada / Recuperacion
  private int docenteId;
  private String usuario = ""; // cuenta (tabla seguridad) duena del registro
```

---

### 4.2 Paquete `CONTROL`

#### 1. `CONTROL.Conexion`
- **Ubicación:** `src/CONTROL/Conexion.java` (Adaptada del profesor)
- **Responsabilidad:** Proveer conexiones JDBC a SQLite y garantizar la creación/migración del esquema de tablas.
- **Métodos:**
  - `conectar()`: carga `org.sqlite.JDBC`, prioriza `C:/MIBD/RegBD.db` si existe y en caso contrario crea la carpeta `datos` y usa `jdbc:sqlite:datos/RegBD.db`; luego invoca `crearTablas(cn)`.
  - `crearTablas(Connection cn)`: ejecuta `CREATE TABLE IF NOT EXISTS` para `registro`, `seguridad`, `docente` y `asignatura_config`, más `ALTER TABLE ... ADD COLUMN usuario TEXT` capturando `SQLException` si ya existe.

```java
// Fragmento literal de src/CONTROL/Conexion.java (líneas 27-40)
        String url = URL_LOCAL;
        File bdProfe = new File(RUTA_PROFE);
        if (bdProfe.exists()) {
            url = "jdbc:sqlite:" + RUTA_PROFE;
        } else {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
        }

        Connection cn = DriverManager.getConnection(url);
        crearTablas(cn);
        return cn;
```

#### 2. `CONTROL.Persistencia`
- **Ubicación:** `src/CONTROL/Persistencia.java` (Adaptada del profesor)
- **Responsabilidad:** Serializar y deserializar objetos Java (`SecretKey`, `Configuracion`, `ControlRegistro`) en archivos binarios.
- **Atributos y Constructores:** `carpeta`, `nombreArchivo`, streams de entrada/salida. `Persistencia()` apunta a `"datos/"` y `"secretKey.dat"`.
- **Métodos:** `existeArchivo()`, `abrirArchivoSalida()` (con `c.mkdirs()`), `cerrarArchivoSalida()`, `abrirArchivoEntrada()`, `cerrarArchivoEntrada()`, `guardar(Object)`, `cargar()`.

#### 3. `CONTROL.SEGURIDAD`
- **Ubicación:** `src/CONTROL/SEGURIDAD.java` (Idéntica al profesor)
- **Responsabilidad:** Utilidad criptográfica estática AES-256 en modo GCM (`AES/GCM/NoPadding`, tag de 128 bits).
- **Métodos:** `generarLlave()`, `encriptar(String texto, SecretKey secretKey)`, `desencriptar(String textoCifradoBase64, SecretKey secretKey)`.

```java
// Fragmento literal de src/CONTROL/SEGURIDAD.java (líneas 33-46)
    public static String encriptar(String texto, SecretKey secretKey)
            throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // El Cipher genera automáticamente un IV seguro
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();

        byte[] textoCifrado = cipher.doFinal(
                texto.getBytes(StandardCharsets.UTF_8)
        );
```

#### 4. `CONTROL.SeguridadDAO`
- **Ubicación:** `src/CONTROL/SeguridadDAO.java` (Adaptada del profesor)
- **Responsabilidad:** Operaciones CRUD sobre la tabla `seguridad` en SQLite.
- **Métodos:** `adicionar(Usuario)`, `modificar(Usuario)`, `eliminar(String usuario)`, `buscar(String usuario)` y `consultarTodos()`. La mejora crítica respecto al original es que `buscar` y `consultarTodos` ahora cargan `CL1, CL2, CL3, CL4` en `u.claves[0..3]`.

#### 5. `CONTROL.ControlSeguridad`
- **Ubicación:** `src/CONTROL/ControlSeguridad.java` (Nuevo complemento)
- **Responsabilidad:** Orquestar la autenticación, el registro de usuarios, el cifrado/descifrado transparente y la recuperación de contraseña mediante imágenes.
- **Atributos:** `private static SecretKey llave`, `private final SeguridadDAO dao = new SeguridadDAO()`.
- **Métodos clave:**
  - `llave()`: carga o genera `datos/secretKey.dat` de forma `synchronized`.
  - `registrar(codigo, password, imagenes)`: valida 4 imágenes no vacías, verifica que el usuario no exista, cifra clave e imágenes y llama a `dao.adicionar(u)`.
  - `validarLogin(codigo, password)`: compara `password` contra `descifrar(u.getPassword())` y ejecuta `migrarSiHaceFalta(u)`.
  - `verificarImagenes(codigo, respuestas)` y `cambiarPassword(codigo, nueva)`.

```java
// Fragmento literal de src/CONTROL/ControlSeguridad.java (líneas 95-108)
    public boolean validarLogin(String codigo, String password) {
        if (codigo == null || codigo.isBlank() || password == null) {
            return false;
        }
        Usuario u = dao.buscar(codigo.trim());
        if (u == null) {
            return false;
        }
        boolean ok = password.equals(descifrar(u.getPassword()));
        if (ok) {
            migrarSiHaceFalta(u);
        }
        return ok;
    }
```

#### 6. `CONTROL.RegistroDAO`
- **Ubicación:** `src/CONTROL/RegistroDAO.java` (Nuevo complemento)
- **Responsabilidad:** Acceso a datos SQLite para la tabla `registro`, siempre acotado al campo `usuario`.
- **Métodos:** `adicionar(Registro)` (verifica duplicado previo con `existe()`), `modificar(Registro)`, `eliminar(usuario, asignatura, grupo, nroEncuentro)`, `buscar(...)`, `existe(...)`, `consultarTodos(usuario)` (ordenado descendente por año, mes, día, encuentro e id) y `siguienteEncuentro(usuario, asignatura, grupo)` (`COALESCE(MAX(nro_encuentro), 0) + 1`).

```java
// Fragmento literal de src/CONTROL/RegistroDAO.java (líneas 130-141)
    public int siguienteEncuentro(String usuario, String asignatura, String grupo) throws SQLException {
        String sql = "SELECT COALESCE(MAX(nro_encuentro), 0) + 1 FROM registro WHERE usuario=? AND asignatura=? AND grupo=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, asignatura);
            ps.setString(3, grupo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        }
    }
```

#### 7. `CONTROL.ControlRegistro`
- **Ubicación:** `src/CONTROL/ControlRegistro.java` (Adaptada del profesor)
- **Responsabilidad:** Mantener el catálogo en memoria (`TreeMap<String, Registro>`) y sincronizarlo con SQLite para el usuario autenticado.
- **Atributos:** `control` (`TreeMap`), `transient RegistroDAO dao`, `usuario`.
- **Métodos:**
  - En memoria: `crearLlave(asignatura, grupo, nroEncuentro)`, `add`, `buscar` (con y sin grupo), `delete` (con y sin grupo), `modify`, `imprimir`, `size`, `isEmpty`, `llenar`.
  - Con BD: `exigirUsuario()`, `cargarDesdeBD()`, `guardarEnBD(Registro)`, `modificarEnBD(Registro)`, `eliminarEnBD(...)`, `siguienteEncuentro(...)`, `listar()` (ordena del más reciente al más antiguo con `Comparator`).

#### 8. `CONTROL.ConfiguracionDAO`
- **Ubicación:** `src/CONTROL/ConfiguracionDAO.java` (Nuevo complemento)
- **Responsabilidad:** Cargar y guardar la configuración del docente (`docente` + `asignatura_config`) por `usuario` en SQLite dentro de una transacción (`cn.setAutoCommit(false)` / `cn.commit()`).
- **Métodos:** `generarIdUsuario(String)`, `cargar()` / `cargar(String usuario)` (si no existe registro de docente, siembra `ejemplo()` para `"beymar"` o usuario vacío, o crea una plantilla de 3 grupos para usuarios nuevos), `guardar(String usuario, Configuracion)`, `indiceDe(Configuracion, asignatura, grupo)`, `ejemplo()`.

#### 9. `CONTROL.Validaciones`
- **Ubicación:** `src/CONTROL/Validaciones.java` (Nuevo complemento)
- **Responsabilidad:** Proveer validaciones estáticas de texto, números y formatos (`esVacio`, `noEsVacio`, `esEntero`, `esEnteroPositivo`, `esDouble`, `esEmail`, `esTelefono`, etc.), además de métodos de lectura por consola (`leerEntero`, `leerCadena`, etc.) heredados de utilidades de clase.

---

### 4.3 Paquete `VISTA`

#### 1. `VISTA.VentanaPrincipal` (`JFrame`)
- **Ubicación:** `src/VISTA/VentanaPrincipal.java`
- **Responsabilidad:** Ventana principal tras el login. Muestra el panel lateral de consultas y la tabla `jTable1` de registros recientes/todos.
- **Atributos:** `control` (`ControlRegistro`), `tabla`, `modelo`, `mostrados`, `verTodos`.
- **Métodos:** `main` (abre `LoginGui`), `iniciar()` (configura tabla, menú contextual clic derecho, doble clic, tecla `DELETE`, toggle `jLabel5` "Ver todos"), `cargarTabla()`, `abrirRegistro(Registro)`, `modificarSeleccionado()`, `eliminarSeleccionado()`, `mostrarPerfilDocente()`, `mostrarHorario()`, `mostrarAsignaturas()`, `mostrarGrupos()`, `configurarDocente()`, `salir()`.

#### 2. `VISTA.LoginGui` (`JDialog`)
- **Ubicación:** `src/VISTA/LoginGui.java`
- **Responsabilidad:** Autenticación, registro de nuevos usuarios y flujo de recuperación de contraseña.
- **Atributos:** `control` (`ControlSeguridad`), `autenticado`, `usuarioAutenticado`, `MAX_INTENTOS_IMAGENES = 3`.
- **Métodos:** `ingresar()`, `crearUsuario()` (diálogo de campos + llamada modal a `recordar2GUI`), `recuperar()` (pide usuario, hasta 3 intentos en `recordar2GUI`, y luego pide nueva contraseña).

#### 3. `VISTA.recordar2GUI` (`JDialog`)
- **Ubicación:** `src/VISTA/recordar2GUI.java` (Adaptada del profesor)
- **Responsabilidad:** Desafío gráfico de 4x4 imágenes (1 imagen por fila) usado tanto al crear usuario como al recuperar contraseña.
- **Atributos:** `public static String[] clV = new String[4]`, `misBotones[16]`, `contadorSel`, `confirmado`.
- **Métodos:** `inicializarBotones()` (asigna `fila`, `columna`, `nombreImagen` y `ActionListener` que deshabilita la fila elegida, marca borde verde y habilita `btnConfirmar` al llegar a 4), `reiniciar()`, `buscarImagen(ruta)`, `isConfirmado()`, `getSeleccion()`.

```java
// Fragmento literal de src/VISTA/recordar2GUI.java (líneas 114-122)
                botonPresionado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 160, 0), 4));
                botonPresionado.setDisabledIcon(botonPresionado.getIcon());

                String nombreImagen = (String) botonPresionado.getClientProperty("nombreImagen");
                int posult = nombreImagen.indexOf('.');
                String nombreI = nombreImagen.substring(1, posult);
                clV[fila] = nombreI;
                contadorSel++;
                btnConfirmar.setEnabled(contadorSel == 4);
```

#### 4. `VISTA.ConfiguracionGui` (`JDialog`)
- **Ubicación:** `src/VISTA/ConfiguracionGui.java`
- **Responsabilidad:** Editar los datos del docente y una matriz dinámica de 14 filas × 1..6 columnas (`Grupo 1`..`Grupo 6`).
- **Métodos:** `cargarDatos()` (intenta `ConfiguracionDAO` y si falla o es null recurre a `Persistencia` con `config_<usuario>.dat`), `configurarColumnasTabla(...)`, `eliminarColumnaEspecifica(...)`, `btnAgregarGrupoActionPerformed`, `btnQuitarGrupoActionPerformed`, `btnGuardarActionPerformed` (valida campos con `Validaciones` y guarda en SQLite y en archivo `.dat`).

#### 5. `VISTA.RegistroGui` (`JDialog`)
- **Ubicación:** `src/VISTA/RegistroGui.java`
- **Responsabilidad:** Crear un nuevo `Registro` o modificar uno existente (`setRegistro(Registro r)`).
- **Atributos:** `control`, `config`, `original` (si no es `null`, está en modo modificación y bloquea el cambio de grupo/asignatura y número de encuentro), `guardado`.
- **Métodos:** `iniciar()` (carga `Configuracion` del docente, configura los hasta 5 radio buttons de grupo visibles y el clic en `jLabel10` para abrir `SelectorFecha`), `seleccionarGrupo()` (autocompleta sede, vocero y `siguienteEncuentro`), `construirRegistro()` (valida encuentro ≥ 1, fecha `DD/MM/AAAA`, ausentes ≤ matriculados, temas no vacíos), `guardar()`.

#### 6. `VISTA.SelectorFecha` (`JDialog`)
- **Ubicación:** `src/VISTA/SelectorFecha.java`
- **Responsabilidad:** Calendario modal reutilizable en Swing puro que retorna un `LocalDate` mediante `SelectorFecha.elegir(Component padre, LocalDate inicial)`.
- **Métodos:** `configurarCalendario()`, `moverMes(int delta)`, `pintarDias()` (calcula huecos desde lunes con `YearMonth`), `elegir(...)`.

---

## Sección 5: Observaciones Técnicas del Análisis Estático

Durante la inspección de solo lectura se identificaron los siguientes detalles técnicos relevantes (sin haber modificado ningún archivo):

1. **Teléfono del vocero como `int` al validar en `ConfiguracionGui.java` (línea 665):**  
   `cfg.setVoceroTel(Validaciones.esEntero(telStr) ? Long.parseLong(telStr) : 0L, i);`  
   Al validar con `esEntero` (`Integer.parseInt`), números de teléfono colombianos de 10 dígitos (ej. `3001234567`, mayores a `Integer.MAX_VALUE = 2147483647`) no pasan `esEntero` y se guardan como `0L`, aunque el atributo en `Configuracion` y SQLite sí soporta `long`/`INTEGER`. Usar `Validaciones.esTelefono(telStr)` resolvería ese límite.
2. **Parámetro `-Dregistro.datos` documentado en `LEEME.txt`:**  
   `LEEME.txt` menciona la opción `-Dregistro.datos=RUTA`, pero `Conexion.java` usa directamente `"jdbc:sqlite:datos/RegBD.db"` sin consultar `System.getProperty("registro.datos")`.
3. **Método heredado `Validaciones.estaVacio(String x)` (líneas 11-20):**  
   Evalúa `x.length()==0` antes de `if(x==null)`, lo que arrojaría `NullPointerException` si se invocara con `null`. No afecta a la interfaz actual porque `ConfiguracionGui` utiliza `Validaciones.esVacio(...)`, que sí valida `texto == null` primero.
4. **Límite de 5 grupos en `RegistroGui.java` vs 6 en `ConfiguracionGui.java`:**  
   `ConfiguracionGui` permite agregar hasta 6 grupos, mientras que `RegistroGui.radiosGrupo()` solo enlaza 5 `JRadioButton` (`jRadioButton1`..`jRadioButton5`). Si un docente configura un sexto grupo, no aparecerá en los botones de radio de `RegistroGui`.
