package CONTROL;

import MODELO.Registro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegistroDAO {

    private static final String COLUMNAS =
            "asignatura, nro_encuentro, grupo, hora, modalidad, plataforma_lms, sede, "
            + "programa_grupo, plata_video, est_matriculados, actividad, trabajo_realizado, "
            + "ano, mes, dia, nro_asistentes, nro_no_asisten, nombres_no_asisten, "
            + "observaciones, tipo_encuentro, docente_id, usuario";

    // ADICIONAR
    public boolean adicionar(Registro r) throws SQLException {
        if (existe(r.getUsuario(), r.getAsignatura(), r.getGrupo(), r.getNroEncuentro())) {
            return false;
        }
        String sql = "INSERT INTO registro (" + COLUMNAS + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            llenar(ps, r);
            ps.executeUpdate();
            return true;
        }
    }

    // MODIFICAR
    public boolean modificar(Registro r) throws SQLException {
        String sql = "UPDATE registro SET hora=?, modalidad=?, plataforma_lms=?, sede=?, "
                + "programa_grupo=?, plata_video=?, est_matriculados=?, actividad=?, "
                + "trabajo_realizado=?, ano=?, mes=?, dia=?, nro_asistentes=?, "
                + "nro_no_asisten=?, nombres_no_asisten=?, observaciones=?, "
                + "tipo_encuentro=?, docente_id=? "
                + "WHERE usuario=? AND asignatura=? AND grupo=? AND nro_encuentro=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            int i = 1;
            ps.setInt(i++, r.getHora());
            ps.setString(i++, r.getModalidad());
            ps.setString(i++, r.getPlataformaLMS());
            ps.setString(i++, r.getSede());
            ps.setString(i++, r.getProgramaGrupo());
            ps.setString(i++, r.getPlataVideo());
            ps.setInt(i++, r.getEstMatriculados());
            ps.setString(i++, r.getActividad());
            ps.setString(i++, r.getTrabajoRealizado());
            ps.setInt(i++, r.getAno());
            ps.setInt(i++, r.getMes());
            ps.setInt(i++, r.getDia());
            ps.setInt(i++, r.getNroAsistentes());
            ps.setInt(i++, r.getNroNoAsisten());
            ps.setString(i++, r.getNombreNosAsisten());
            ps.setString(i++, r.getObservaciones());
            ps.setString(i++, r.getTipoEncuentro());
            ps.setInt(i++, r.getDocenteId());
            ps.setString(i++, r.getUsuario());
            ps.setString(i++, r.getAsignatura());
            ps.setString(i++, r.getGrupo());
            ps.setInt(i++, r.getNroEncuentro());
            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR
    public boolean eliminar(String usuario, String asignatura, String grupo, int nroEncuentro) throws SQLException {
        String sql = "DELETE FROM registro WHERE usuario=? AND asignatura=? AND grupo=? AND nro_encuentro=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, asignatura);
            ps.setString(3, grupo);
            ps.setInt(4, nroEncuentro);
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR
    public Registro buscar(String usuario, String asignatura, String grupo, int nroEncuentro) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM registro WHERE usuario=? AND asignatura=? AND grupo=? AND nro_encuentro=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, asignatura);
            ps.setString(3, grupo);
            ps.setInt(4, nroEncuentro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? leer(rs) : null;
            }
        }
    }

    // EXISTE
    public boolean existe(String usuario, String asignatura, String grupo, int nroEncuentro) throws SQLException {
        String sql = "SELECT 1 FROM registro WHERE usuario=? AND asignatura=? AND grupo=? AND nro_encuentro=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, asignatura);
            ps.setString(3, grupo);
            ps.setInt(4, nroEncuentro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CONSULTAR TODOS
    public ArrayList<Registro> consultarTodos(String usuario) throws SQLException {
        ArrayList<Registro> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM registro WHERE usuario=? "
                + "ORDER BY ano DESC, mes DESC, dia DESC, nro_encuentro DESC, id DESC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leer(rs));
                }
            }
        }
        return lista;
    }

    // SIGUIENTE ENCUENTRO
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

    private void llenar(PreparedStatement ps, Registro r) throws SQLException {
        int i = 1;
        ps.setString(i++, r.getAsignatura());
        ps.setInt(i++, r.getNroEncuentro());
        ps.setString(i++, r.getGrupo());
        ps.setInt(i++, r.getHora());
        ps.setString(i++, r.getModalidad());
        ps.setString(i++, r.getPlataformaLMS());
        ps.setString(i++, r.getSede());
        ps.setString(i++, r.getProgramaGrupo());
        ps.setString(i++, r.getPlataVideo());
        ps.setInt(i++, r.getEstMatriculados());
        ps.setString(i++, r.getActividad());
        ps.setString(i++, r.getTrabajoRealizado());
        ps.setInt(i++, r.getAno());
        ps.setInt(i++, r.getMes());
        ps.setInt(i++, r.getDia());
        ps.setInt(i++, r.getNroAsistentes());
        ps.setInt(i++, r.getNroNoAsisten());
        ps.setString(i++, r.getNombreNosAsisten());
        ps.setString(i++, r.getObservaciones());
        ps.setString(i++, r.getTipoEncuentro());
        ps.setInt(i++, r.getDocenteId());
        ps.setString(i++, r.getUsuario());
    }

    private Registro leer(ResultSet rs) throws SQLException {
        Registro r = new Registro(
                rs.getString("asignatura"),
                rs.getInt("nro_encuentro"),
                rs.getString("grupo"),
                rs.getInt("hora"),
                rs.getString("modalidad"),
                rs.getString("plataforma_lms"),
                rs.getString("sede"),
                rs.getString("programa_grupo"),
                rs.getString("plata_video"),
                rs.getInt("est_matriculados"),
                rs.getString("actividad"),
                rs.getString("trabajo_realizado"),
                rs.getInt("ano"),
                rs.getInt("mes"),
                rs.getInt("dia"),
                rs.getInt("nro_asistentes"),
                rs.getInt("nro_no_asisten"),
                rs.getString("nombres_no_asisten"),
                rs.getString("observaciones"));
        r.setTipoEncuentro(rs.getString("tipo_encuentro"));
        r.setDocenteId(rs.getInt("docente_id"));
        r.setUsuario(rs.getString("usuario"));
        return r;
    }
}
