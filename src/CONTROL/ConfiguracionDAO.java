package CONTROL;

import MODELO.Configuracion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfiguracionDAO {

    public static int generarIdUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return 16789456;
        }
        usuario = usuario.trim();
        if (Validaciones.esEnteroPositivo(usuario)) {
            try {
                return Integer.parseInt(usuario);
            } catch (NumberFormatException ignored) {}
        }
        return Math.abs(usuario.toLowerCase().hashCode() % 89999999) + 10000000;
    }

    public Configuracion cargar() throws SQLException {
        return cargar("");
    }

    public Configuracion cargar(String usuario) throws SQLException {
        if (usuario == null) {
            usuario = "";
        }
        usuario = usuario.trim();

        try (Connection cn = Conexion.conectar()) {
            asegurarColumnas(cn);
            if (!hayDocente(cn, usuario)) {
                if (usuario.isEmpty() || "beymar".equalsIgnoreCase(usuario)) {
                    guardar(usuario, ejemplo());
                } else {
                    int idNuevo = generarIdUsuario(usuario);
                    String nomCap = usuario.substring(0, 1).toUpperCase() + (usuario.length() > 1 ? usuario.substring(1) : "");
                    Configuracion confNuevo = new Configuracion(idNuevo, "", nomCap, "Ingenieria de Sistemas", 3);
                    for (int g = 0; g < 3; g++) {
                        confNuevo.setGrupo("G" + (g + 1), g);
                        confNuevo.setAsignatura("", g);
                        confNuevo.setDiaSemana("Lunes", g);
                        confNuevo.setHora(700, g);
                        confNuevo.setModalidad("presencial", g);
                        confNuevo.setPlataformaLMS("Teams", g);
                        confNuevo.setSede("Norte", g);
                        confNuevo.setProgramaGrupo("Ingenieria de Sistemas", g);
                        confNuevo.setPlataVideo("Teams", g);
                        confNuevo.setEstMatriculados(0, g);
                        confNuevo.setVoceroId(0, g);
                        confNuevo.setVoceroApNombre("", g);
                        confNuevo.setVoceroemail("", g);
                        confNuevo.setVoceroTel(0L, g);
                    }
                    guardar(usuario, confNuevo);
                }
            }
        }

        try (Connection cn = Conexion.conectar()) {
            asegurarColumnas(cn);
            Configuracion conf = null;
            String sqlDocente = usuario.isEmpty()
                    ? "SELECT identificacion, apellidos, nombres, programa FROM docente LIMIT 1"
                    : "SELECT identificacion, apellidos, nombres, programa FROM docente WHERE usuario = ? LIMIT 1";

            try (PreparedStatement ps = cn.prepareStatement(sqlDocente)) {
                if (!usuario.isEmpty()) {
                    ps.setString(1, usuario);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("identificacion");
                        String ap = rs.getString("apellidos");
                        String no = rs.getString("nombres");
                        String pr = rs.getString("programa");
                        int n = contar(cn, usuario);
                        if (n <= 0) {
                            n = 3;
                        }
                        conf = new Configuracion(id, ap, no, pr, n);
                    }
                }
            }

            if (conf == null) {
                return null;
            }

            String sqlAsig = usuario.isEmpty()
                    ? "SELECT * FROM asignatura_config ORDER BY indice"
                    : "SELECT * FROM asignatura_config WHERE usuario = ? ORDER BY indice";

            try (PreparedStatement ps = cn.prepareStatement(sqlAsig)) {
                if (!usuario.isEmpty()) {
                    ps.setString(1, usuario);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    int i = 0;
                    while (rs.next() && i < conf.getNroAsignaturas()) {
                        conf.setAsignatura(rs.getString("asignatura"), i);
                        conf.setGrupo(rs.getString("grupo"), i);
                        conf.setDiaSemana(rs.getString("dia_semana"), i);
                        conf.setHora(rs.getInt("hora"), i);
                        conf.setModalidad(rs.getString("modalidad"), i);
                        conf.setPlataformaLMS(rs.getString("plataforma_lms"), i);
                        conf.setSede(rs.getString("sede"), i);
                        conf.setProgramaGrupo(rs.getString("programa_grupo"), i);
                        conf.setPlataVideo(rs.getString("plata_video"), i);
                        conf.setEstMatriculados(rs.getInt("est_matriculados"), i);
                        conf.setVoceroId(rs.getInt("vocero_id"), i);
                        conf.setVoceroApNombre(rs.getString("vocero_nombre"), i);
                        conf.setVoceroemail(rs.getString("vocero_email"), i);
                        conf.setVoceroTel(rs.getLong("vocero_tel"), i);
                        i++;
                    }
                }
            }
            return conf;
        }
    }

    public void guardar(Configuracion c) throws SQLException {
        guardar("", c);
    }

    public void guardar(String usuario, Configuracion c) throws SQLException {
        if (usuario == null) {
            usuario = "";
        }
        usuario = usuario.trim();

        try (Connection cn = Conexion.conectar()) {
            asegurarColumnas(cn);
            cn.setAutoCommit(false);

            if (usuario.isEmpty()) {
                try (Statement st = cn.createStatement()) {
                    st.executeUpdate("DELETE FROM docente");
                    st.executeUpdate("DELETE FROM asignatura_config");
                }
            } else {
                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM docente WHERE usuario = ?")) {
                    ps.setString(1, usuario);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM asignatura_config WHERE usuario = ?")) {
                    ps.setString(1, usuario);
                    ps.executeUpdate();
                }
            }

            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO docente (identificacion, apellidos, nombres, programa, usuario) VALUES (?,?,?,?,?)")) {
                ps.setInt(1, c.getIdentificacion());
                ps.setString(2, c.getApellidos());
                ps.setString(3, c.getNombres());
                ps.setString(4, c.getPrograma());
                ps.setString(5, usuario);
                ps.executeUpdate();
            }

            String sql = "INSERT INTO asignatura_config (indice, asignatura, grupo, dia_semana, hora, modalidad, "
                    + "plataforma_lms, sede, programa_grupo, plata_video, est_matriculados, vocero_id, "
                    + "vocero_nombre, vocero_email, vocero_tel, usuario) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                for (int i = 0; i < c.getNroAsignaturas(); i++) {
                    int k = 1;
                    ps.setInt(k++, i);
                    ps.setString(k++, c.getAsignatura(i) != null ? c.getAsignatura(i) : "");
                    ps.setString(k++, c.getGrupo(i) != null ? c.getGrupo(i) : "");
                    ps.setString(k++, c.getDiaSemana(i) != null ? c.getDiaSemana(i) : "");
                    ps.setInt(k++, c.getHora(i));
                    ps.setString(k++, c.getModalidad(i) != null ? c.getModalidad(i) : "");
                    ps.setString(k++, c.getPlataformaLMS(i) != null ? c.getPlataformaLMS(i) : "");
                    ps.setString(k++, c.getSede(i) != null ? c.getSede(i) : "");
                    ps.setString(k++, c.getProgramaGrupo(i) != null ? c.getProgramaGrupo(i) : "");
                    ps.setString(k++, c.getPlataVideo(i) != null ? c.getPlataVideo(i) : "");
                    ps.setInt(k++, c.getEstMatriculados(i));
                    ps.setInt(k++, c.getVoceroId(i));
                    ps.setString(k++, c.getVoceroApNombre(i) != null ? c.getVoceroApNombre(i) : "");
                    ps.setString(k++, c.getVoceroemail(i) != null ? c.getVoceroemail(i) : "");
                    ps.setLong(k++, c.getVoceroTel(i));
                    ps.setString(k++, usuario != null ? usuario : "");
                    ps.executeUpdate();
                }
            }
            cn.commit();
        }
    }

    public static int indiceDe(Configuracion c, String asignatura, String grupo) {
        if (c == null) {
            return -1;
        }
        for (int i = 0; i < c.getNroAsignaturas(); i++) {
            if (grupo.equalsIgnoreCase(c.getGrupo(i)) && asignatura.equalsIgnoreCase(c.getAsignatura(i))) {
                return i;
            }
        }
        return -1;
    }

    private void asegurarColumnas(Connection cn) {
        try (Statement st = cn.createStatement()) {
            st.execute("ALTER TABLE docente ADD COLUMN usuario TEXT");
        } catch (SQLException ignored) {}
        try (Statement st = cn.createStatement()) {
            st.execute("ALTER TABLE asignatura_config ADD COLUMN usuario TEXT");
        } catch (SQLException ignored) {}
    }

    private boolean hayDocente(Connection cn, String usuario) throws SQLException {
        String sql = usuario.isEmpty()
                ? "SELECT 1 FROM docente LIMIT 1"
                : "SELECT 1 FROM docente WHERE usuario = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            if (!usuario.isEmpty()) {
                ps.setString(1, usuario);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int contar(Connection cn, String usuario) throws SQLException {
        String sql = usuario.isEmpty()
                ? "SELECT COUNT(*) FROM asignatura_config"
                : "SELECT COUNT(*) FROM asignatura_config WHERE usuario = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            if (!usuario.isEmpty()) {
                ps.setString(1, usuario);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public static Configuracion ejemplo() {
        Configuracion conf = new Configuracion(16789456, "Diaz", "William", "Ingenieria de Sistemas", 5);

        conf.setAsignatura("Programacion 1", 0);
        conf.setGrupo("2303A1", 0);
        conf.setDiaSemana("Lunes", 0);
        conf.setHora(1400, 0);
        conf.setModalidad("presencial", 0);
        conf.setPlataformaLMS("Teams", 0);
        conf.setSede("Norte", 0);
        conf.setProgramaGrupo("Ingenieria de Sistemas", 0);
        conf.setPlataVideo("Teams", 0);
        conf.setEstMatriculados(20, 0);
        conf.setVoceroId(1001001, 0);
        conf.setVoceroApNombre("Carlos Perez", 0);
        conf.setVoceroemail("carlos.perez@universidad.edu.co", 0);
        conf.setVoceroTel(3001234567L, 0);

        conf.setAsignatura("Programacion 2", 1);
        conf.setGrupo("3303B-2", 1);
        conf.setDiaSemana("Martes", 1);
        conf.setHora(700, 1);
        conf.setModalidad("presencial", 1);
        conf.setPlataformaLMS("Teams", 1);
        conf.setSede("Norte", 1);
        conf.setProgramaGrupo("Ingenieria de Sistemas", 1);
        conf.setPlataVideo("Teams", 1);
        conf.setEstMatriculados(22, 1);
        conf.setVoceroId(1001002, 1);
        conf.setVoceroApNombre("Sebastian Gomez", 1);
        conf.setVoceroemail("sebastian.gomez@universidad.edu.co", 1);
        conf.setVoceroTel(3012345678L, 1);

        conf.setAsignatura("Circuitos Digitales", 2);
        conf.setGrupo("4303A2", 2);
        conf.setDiaSemana("Miercoles", 2);
        conf.setHora(800, 2);
        conf.setModalidad("presencial", 2);
        conf.setPlataformaLMS("Teams", 2);
        conf.setSede("Norte", 2);
        conf.setProgramaGrupo("Ingenieria Electronica", 2);
        conf.setPlataVideo("Teams", 2);
        conf.setEstMatriculados(18, 2);
        conf.setVoceroId(1001003, 2);
        conf.setVoceroApNombre("Juan David Ruiz", 2);
        conf.setVoceroemail("juan.ruiz@universidad.edu.co", 2);
        conf.setVoceroTel(3023456789L, 2);

        conf.setAsignatura("Investigacion de Operaciones", 3);
        conf.setGrupo("4303B2", 3);
        conf.setDiaSemana("Jueves", 3);
        conf.setHora(1000, 3);
        conf.setModalidad("remota", 3);
        conf.setPlataformaLMS("Zoom", 3);
        conf.setSede("Sur", 3);
        conf.setProgramaGrupo("Ingenieria Industrial", 3);
        conf.setPlataVideo("Zoom", 3);
        conf.setEstMatriculados(17, 3);
        conf.setVoceroId(1001004, 3);
        conf.setVoceroApNombre("Paula Herrera", 3);
        conf.setVoceroemail("paula.herrera@universidad.edu.co", 3);
        conf.setVoceroTel(3034567890L, 3);

        conf.setAsignatura("Bases de Datos", 4);
        conf.setGrupo("4303C2", 4);
        conf.setDiaSemana("Viernes", 4);
        conf.setHora(1600, 4);
        conf.setModalidad("presencial", 4);
        conf.setPlataformaLMS("Teams", 4);
        conf.setSede("Norte", 4);
        conf.setProgramaGrupo("Ingenieria de Sistemas", 4);
        conf.setPlataVideo("Teams", 4);
        conf.setEstMatriculados(19, 4);
        conf.setVoceroId(1001005, 4);
        conf.setVoceroApNombre("Valentina Sanchez", 4);
        conf.setVoceroemail("valentina.sanchez@universidad.edu.co", 4);
        conf.setVoceroTel(3045678901L, 4);

        return conf;
    }
}
