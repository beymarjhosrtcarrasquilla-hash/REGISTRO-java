/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;

import MODELO.Registro;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

 

 

public class ControlRegistro implements Serializable{

    private TreeMap<String, Registro> control;

    // Acceso a SQLite (no se serializa)
    private transient RegistroDAO dao;

    // Usuario autenticado: dueno de los registros que se leen y escriben en SQLite
    private String usuario = "";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ControlRegistro() {
        control = new TreeMap<>();
    }

    // Control de los registros de un usuario (el que inicio sesion)
    public ControlRegistro(String usuario) {
        this();
        this.usuario = usuario == null ? "" : usuario.trim();
    }

    public String getUsuario() {
        return usuario;
    }

    // Sin usuario no se toca la BD: evita mezclar registros de cuentas distintas
    private void exigirUsuario() throws SQLException {
        if (usuario.isEmpty()) {
            throw new SQLException("No hay un usuario autenticado.");
        }
    }

    private RegistroDAO dao() {
        if (dao == null) {
            dao = new RegistroDAO();
        }
        return dao;
    }


    // =====================================================
    // CREAR LLAVE (asignatura + grupo + nro de encuentro)
    // =====================================================

    private String crearLlave(String asignatura, String grupo, int nroEncuentro) {

        return asignatura.trim().toUpperCase()
                + "-"
                + grupo.trim().toUpperCase()
                + "-"
                + nroEncuentro;
    }

    private String crearLlave(Registro r) {

        return crearLlave(
                r.getAsignatura(),
                r.getGrupo(),
                r.getNroEncuentro()
        );
    }


    // =====================================================
    // ADD
    // =====================================================

    public boolean add(Registro r) {

        String llave = crearLlave(r);

        // No permite registros repetidos
        if (control.containsKey(llave)) {
            return false;
        }

        control.put(llave, r);

        return true;
    }


    // =====================================================
    // BUSCAR
    // =====================================================

    public Registro buscar(String asignatura, String grupo, int nroEncuentro) {

        return control.get(crearLlave(asignatura, grupo, nroEncuentro));
    }

    // Version original (sin grupo): devuelve el primero que coincida
    public Registro buscar(String asignatura, int nroEncuentro) {

        for (Registro r : control.values()) {
            if (r.getAsignatura().trim().equalsIgnoreCase(asignatura.trim())
                    && r.getNroEncuentro() == nroEncuentro) {
                return r;
            }
        }
        return null;
    }


    // =====================================================
    // DELETE
    // =====================================================

    public boolean delete(String asignatura, String grupo, int nroEncuentro) {

        return control.remove(crearLlave(asignatura, grupo, nroEncuentro)) != null;
    }

    // Version original (sin grupo): elimina el primero que coincida
    public boolean delete(String asignatura, int nroEncuentro) {

        Registro r = buscar(asignatura, nroEncuentro);

        if (r == null) {
            return false;
        }

        return delete(r.getAsignatura(), r.getGrupo(), r.getNroEncuentro());
    }


    // =====================================================
    // MODIFY
    // =====================================================

    public boolean modify(Registro r) {

        String llave = crearLlave(r);

        if (!control.containsKey(llave)) {
            return false;
        }

        control.put(llave, r);

        return true;
    }


    // =====================================================
    // PERSISTENCIA EN SQLITE
    // Cada operacion escribe en la BD y luego recarga la memoria.
    // =====================================================

    // Reemplaza el contenido en memoria con lo que hay en la BD
    public void cargarDesdeBD() throws SQLException {

        exigirUsuario();

        TreeMap<String, Registro> nuevo = new TreeMap<>();

        for (Registro r : dao().consultarTodos(usuario)) {
            nuevo.put(crearLlave(r), r);
        }

        control = nuevo;
    }

    // false si ya existe un registro con la misma asignatura, grupo y encuentro
    public boolean guardarEnBD(Registro r) throws SQLException {

        exigirUsuario();
        r.setUsuario(usuario); // el dueno siempre es el usuario autenticado

        boolean ok = dao().adicionar(r);

        if (ok) {
            cargarDesdeBD();
        }

        return ok;
    }

    public boolean modificarEnBD(Registro r) throws SQLException {

        exigirUsuario();
        r.setUsuario(usuario);

        boolean ok = dao().modificar(r);

        if (ok) {
            cargarDesdeBD();
        }

        return ok;
    }

    public boolean eliminarEnBD(String asignatura, String grupo, int nroEncuentro)
            throws SQLException {

        exigirUsuario();

        boolean ok = dao().eliminar(usuario, asignatura, grupo, nroEncuentro);

        if (ok) {
            cargarDesdeBD();
        }

        return ok;
    }

    public int siguienteEncuentro(String asignatura, String grupo) throws SQLException {

        exigirUsuario();

        return dao().siguienteEncuentro(usuario, asignatura, grupo);
    }

    // Registros ordenados del mas reciente al mas antiguo
    public ArrayList<Registro> listar() {

        ArrayList<Registro> lista = new ArrayList<>(control.values());

        lista.sort(Comparator
                .comparingInt(Registro::getAno)
                .thenComparingInt(Registro::getMes)
                .thenComparingInt(Registro::getDia)
                .thenComparingInt(Registro::getNroEncuentro)
                .reversed());

        return lista;
    }


    // =====================================================
    // IMPRIMIR
    // =====================================================

    public void imprimir() {

        for (Map.Entry<String, Registro> dato : control.entrySet()) {

            System.out.println("LLAVE: " + dato.getKey());
            System.out.println(dato.getValue());

            System.out.println(
                    "=============================================="
            );
        }
    }


    // =====================================================
    // CANTIDAD DE REGISTROS
    // =====================================================

    public int size() {
        return control.size();
    }


    // =====================================================
    // VERIFICAR SI ESTA VACIO
    // =====================================================

    public boolean isEmpty() {
        return control.isEmpty();
    }


    // =====================================================
    // LLENAR DATOS DE PRUEBA
    // =====================================================

    public void llenar() {

        // =================================================
        // PROGRAMACION ORIENTADA A OBJETOS
        // Grupo 3303B1
        // Ingeniería de Sistemas
        // =================================================

        add(new Registro(
                "Programacion Orientada a Objetos",
                1,
                "3303B1",
                 
                1800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                20,
                "Introduccion a clases, objetos, atributos y metodos",
                "Ejercicios de modelado de clases en Java",
                2026,
                8,
                4,
                19,
                1,
                "Laura Martinez",
                "Participacion activa del grupo"
        ));

        add(new Registro(
                "Programacion Orientada a Objetos",
                2,
                "3303B1",
              
                1800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                20,
                "Constructores, encapsulamiento y metodos get y set",
                "Taller de construccion de clases",
                2026,
                8,
                11,
                18,
                2,
                "Carlos Perez, Diana Lopez",
                "Se reforzo el concepto de encapsulamiento"
        ));

        add(new Registro(
                "Programacion Orientada a Objetos",
                3,
                "3303B1",
              
                1800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                20,
                "Relaciones entre clases y arreglos de objetos",
                "Laboratorio de relaciones uno a muchos",
                2026,
                8,
                18,
                20,
                0,
                "",
                "Todos los estudiantes asistieron"
        ));

        add(new Registro(
                "Programacion Orientada a Objetos",
                4,
                "3303B1",
                 
                1800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                20,
                "Herencia y reutilizacion de clases",
                "Taller de superclases y subclases",
                2026,
                8,
                25,
                19,
                1,
                "Andres Ramirez",
                "Buen desarrollo de los ejercicios"
        ));


        // =================================================
        // ESTRUCTURAS DE DATOS
        // Grupo 3303B2
        // Ingeniería de Sistemas
        // =================================================

        add(new Registro(
                "Estructuras de Datos",
                1,
                "3303B2",
                
                1400,
                "presencial",
                "Teams",
                "Sur",
                "Ingenieria de Sistemas",
                "Teams",
                22,
                "Introduccion a estructuras de datos y tipos abstractos",
                "Ejercicios de repaso de arreglos y objetos",
                2026,
                8,
                5,
                21,
                1,
                "Sebastian Gomez",
                "El grupo desarrollo correctamente los ejercicios"
        ));

        add(new Registro(
                "Estructuras de Datos",
                2,
                "3303B2",
                
                1400,
                "presencial",
                "Teams",
                "Sur",
                "Ingenieria de Sistemas",
                "Teams",
                22,
                "Listas simplemente enlazadas",
                "Laboratorio de nodos y enlaces",
                2026,
                8,
                12,
                20,
                2,
                "Maria Torres, Julian Castro",
                "Se reforzo el manejo de referencias"
        ));

        add(new Registro(
                "Estructuras de Datos",
                3,
                "3303B2",
                
                1400,
                "presencial",
                "Teams",
                "Sur",
                "Ingenieria de Sistemas",
                "Teams",
                22,
                "Listas doblemente enlazadas",
                "Taller de insercion, busqueda y eliminacion",
                2026,
                8,
                19,
                22,
                0,
                "",
                "Asistencia completa"
        ));

        add(new Registro(
                "Estructuras de Datos",
                4,
                "3303B2",
                
                1400,
                "presencial",
                "Teams",
                "Sur",
                "Ingenieria de Sistemas",
                "Teams",
                22,
                "Pilas y colas",
                "Ejercicio practico de implementacion de pilas",
                2026,
                8,
                26,
                21,
                1,
                "Natalia Rojas",
                "Se realizaron ejercicios adicionales"
        ));


        // =================================================
        // CIRCUITOS DIGITALES
        // Grupo 4303A2
        // Ingeniería Electrónica
        // =================================================

        add(new Registro(
                "Circuitos Digitales",
                1,
                "4303A2",
                 
                800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria Electronica",
                "Teams",
                18,
                "Sistemas numericos y conversion binaria",
                "Ejercicios de conversion decimal, binaria y hexadecimal",
                2026,
                8,
                6,
                17,
                1,
                "Juan David Ruiz",
                "Buen desempeño general"
        ));

        add(new Registro(
                "Circuitos Digitales",
                2,
                "4303A2",
               
                800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria Electronica",
                "Teams",
                18,
                "Compuertas logicas",
                "Laboratorio de compuertas AND, OR y NOT",
                2026,
                8,
                13,
                18,
                0,
                "",
                "Laboratorio realizado satisfactoriamente"
        ));

        add(new Registro(
                "Circuitos Digitales",
                3,
                "4303A2",
                
                800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria Electronica",
                "Teams",
                18,
                "Algebra de Boole",
                "Taller de simplificacion de expresiones logicas",
                2026,
                8,
                20,
                16,
                2,
                "Camilo Arias, Sofia Mora",
                "Se realizaron ejercicios adicionales"
        ));

        add(new Registro(
                "Circuitos Digitales",
                4,
                "4303A2",
                
                800,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria Electronica",
                "Teams",
                18,
                "Mapas de Karnaugh",
                "Ejercicio de simplificacion de circuitos",
                2026,
                8,
                27,
                17,
                1,
                "Daniel Vargas",
                "Se requiere reforzar mapas de cuatro variables"
        ));


        // =================================================
        // INVESTIGACION DE OPERACIONES
        // Grupo 4303B2
        // Ingeniería Industrial
        // =================================================

        add(new Registro(
                "Investigacion de Operaciones",
                1,
                "4303B2",
                 
                1000,
                "remota",
                "Zoom",
                "Sur",
                "Ingenieria Industrial",
                "Zoom",
                17,
                "Introduccion a modelos de optimizacion",
                "Ejercicios de formulacion de modelos",
                2026,
                8,
                7,
                16,
                1,
                "Paula Herrera",
                "Buena participacion durante la sesion"
        ));

        add(new Registro(
                "Investigacion de Operaciones",
                2,
                "4303B2",
                 
                1000,
                "remota",
                "Zoom",
                "Sur",
                "Ingenieria Industrial",
                "Zoom",
                17,
                "Programacion lineal",
                "Taller de formulacion de funcion objetivo y restricciones",
                2026,
                8,
                14,
                15,
                2,
                "Diego Leon, Carolina Gil",
                "Se resolvieron casos empresariales"
        ));

        add(new Registro(
                "Investigacion de Operaciones",
                3,
                "4303B2",
                 
                1000,
                "remota",
                "Zoom",
                "Sur",
                "Ingenieria Industrial",
                "Zoom",
                17,
                "Metodo grafico de programacion lineal",
                "Ejercicio de solucion grafica",
                2026,
                8,
                21,
                17,
                0,
                "",
                "Actividad completada satisfactoriamente"
        ));

        add(new Registro(
                "Investigacion de Operaciones",
                4,
                "4303B2",
                 
                1000,
                "remota",
                "Zoom",
                "Sur",
                "Ingenieria Industrial",
                "Zoom",
                17,
                "Metodo Simplex",
                "Laboratorio de solucion de modelos de optimizacion",
                2026,
                8,
                28,
                16,
                1,
                "Felipe Martinez",
                "Se requiere continuar practicando el metodo Simplex"
        ));


        // =================================================
        // BASES DE DATOS
        // Grupo 4303C2
        // Ingeniería de Sistemas
        // =================================================

        add(new Registro(
                "Bases de Datos",
                1,
                "4303C2",
               
                1600,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                19,
                "Introduccion a bases de datos y modelo relacional",
                "Ejercicio de identificacion de entidades y atributos",
                2026,
                8,
                8,
                18,
                1,
                "Valentina Sanchez",
                "Buen nivel de participacion"
        ));

        add(new Registro(
                "Bases de Datos",
                2,
                "4303C2",
                 
                1600,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                19,
                "Modelo entidad relacion",
                "Taller de diagramas entidad relacion",
                2026,
                8,
                15,
                19,
                0,
                "",
                "Los estudiantes completaron el modelo propuesto"
        ));

        add(new Registro(
                "Bases de Datos",
                3,
                "4303C2",
                
                1600,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                19,
                "Lenguaje SQL y consultas SELECT",
                "Laboratorio de consultas SQL",
                2026,
                8,
                22,
                17,
                2,
                "Mateo Ortiz, Laura Jimenez",
                "Se reforzaron consultas con condiciones"
        ));

        add(new Registro(
                "Bases de Datos",
                4,
                "4303C2",
                
                1600,
                "presencial",
                "Teams",
                "Norte",
                "Ingenieria de Sistemas",
                "Teams",
                19,
                "Consultas SQL con funciones y agrupamiento",
                "Laboratorio de consultas GROUP BY",
                2026,
                8,
                29,
                18,
                1,
                "Andres Salazar",
                "Buen resultado en el laboratorio"
        ));
    }
    public static void main(String[] args) {
        Persistencia per= new Persistencia("datos/", "registro.dat");
        ControlRegistro cre = new ControlRegistro();
        per.abrirArchivoSalida();
        cre.llenar();
        per.guardar(cre);
        per.cerrarArchivoSalida();
        cre=null;
        per.abrirArchivoEntrada();
        cre= (ControlRegistro)per.cargar();
        per.cerrarArchivoEntrada();
        System.out.println("TOTAL REGISTROS: " + cre.size());

        cre.imprimir();
    }
}
