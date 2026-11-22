/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final String URL_LOCAL = "jdbc:sqlite:datos/RegBD.db";
    private static final String RUTA_PROFE = "C:/MIBD/RegBD.db";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Error driver SQLite: " + e.getMessage());
        }

        // Si existe la base de datos en C:/MIBD (ruta del profesor), usa esa.
        // Si no, usa la carpeta local datos/ del proyecto.
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
    }

    private static void crearTablas(Connection cn) {
        try (Statement st = cn.createStatement()) {
            // Tabla registro
            st.execute("CREATE TABLE IF NOT EXISTS registro ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "asignatura TEXT, "
                    + "grupo TEXT, "
                    + "nro_encuentro INTEGER, "
                    + "hora INTEGER, "
                    + "modalidad TEXT, "
                    + "plataforma_lms TEXT, "
                    + "sede TEXT, "
                    + "programa_grupo TEXT, "
                    + "plata_video TEXT, "
                    + "est_matriculados INTEGER, "
                    + "actividad TEXT, "
                    + "trabajo_realizado TEXT, "
                    + "ano INTEGER, "
                    + "mes INTEGER, "
                    + "dia INTEGER, "
                    + "nro_asistentes INTEGER, "
                    + "nro_no_asisten INTEGER, "
                    + "nombres_no_asisten TEXT, "
                    + "observaciones TEXT, "
                    + "tipo_encuentro TEXT, "
                    + "docente_id INTEGER, "
                    + "usuario TEXT)");

            // Tabla seguridad
            st.execute("CREATE TABLE IF NOT EXISTS seguridad ("
                    + "usuario TEXT PRIMARY KEY, "
                    + "password TEXT, "
                    + "CL1 TEXT, CL2 TEXT, CL3 TEXT, CL4 TEXT)");

            // Tabla docente (por usuario)
            st.execute("CREATE TABLE IF NOT EXISTS docente ("
                    + "usuario TEXT PRIMARY KEY, "
                    + "identificacion INTEGER, "
                    + "apellidos TEXT, "
                    + "nombres TEXT, "
                    + "programa TEXT)");

            // Tabla asignatura_config (por usuario)
            st.execute("CREATE TABLE IF NOT EXISTS asignatura_config ("
                    + "usuario TEXT, "
                    + "indice INTEGER, "
                    + "asignatura TEXT, "
                    + "grupo TEXT, "
                    + "dia_semana TEXT, "
                    + "hora INTEGER, "
                    + "modalidad TEXT, "
                    + "plataforma_lms TEXT, "
                    + "sede TEXT, "
                    + "programa_grupo TEXT, "
                    + "plata_video TEXT, "
                    + "est_matriculados INTEGER, "
                    + "vocero_id INTEGER, "
                    + "vocero_nombre TEXT, "
                    + "vocero_email TEXT, "
                    + "vocero_tel INTEGER, "
                    + "PRIMARY KEY (usuario, indice))");

            // Migracion segura: asegurar que exista la columna usuario si la tabla fue creada anteriormente
            try {
                st.execute("ALTER TABLE docente ADD COLUMN usuario TEXT");
            } catch (SQLException ignored) {}
            try {
                st.execute("ALTER TABLE asignatura_config ADD COLUMN usuario TEXT");
            } catch (SQLException ignored) {}
        } catch (SQLException e) {
            System.out.println("Error creando tablas: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Connection conexion = Conexion.conectar();
            System.out.println("Conexion exitosa con la base de datos");
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            System.out.println(e.getMessage());
        }
    }
}
