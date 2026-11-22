/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;

import MODELO.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeguridadDAO {

    // =====================================================
    // ADICIONAR
    // =====================================================
    public boolean adicionar(Usuario us) {

        String sql = """
                INSERT INTO seguridad (usuario, password, CL1, CL2, CL3, CL4)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, us.getCodigo());
            ps.setString(2, us.getPassword());
            ps.setString(3, us.claves[0]);
            ps.setString(4, us.claves[1]);
            ps.setString(5, us.claves[2]);
            ps.setString(6, us.claves[3]);
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al adicionar: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // MODIFICAR
    // =====================================================
    public boolean modificar(Usuario us) {
         
        String sql = """
                UPDATE seguridad
                SET password = ?,
                    CL1 = ?,
                    CL2 = ?, 
                    CL3 = ?,
                    CL4 = ?  
                WHERE usuario = ?
                """;

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, us.getPassword());
            ps.setString(2, us.claves[0]);
            ps.setString(3, us.claves[1]);
            ps.setString(4, us.claves[2]);
            ps.setString(5, us.claves[3]);
            ps.setString(6, us.getCodigo());
            int filas = ps.executeUpdate();
               
            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    public boolean eliminar(String usuario) {

        String sql = """
                DELETE FROM seguridad
                WHERE usuario = ?
                """;

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // BUSCAR POR USUARIO
    // =====================================================
    public Usuario buscar(String usuario) {
        Usuario r = null;
        String sql = """
                SELECT usuario, password, CL1, CL2, CL3, CL4
                FROM seguridad
                WHERE usuario = ?
                """;

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                r = new Usuario();
                r.setCodigo(rs.getString("usuario"));
                r.setPassword(rs.getString("password"));
                r.claves[0] = rs.getString("CL1");
                r.claves[1] = rs.getString("CL2");
                r.claves[2] = rs.getString("CL3");
                r.claves[3] = rs.getString("CL4");

                return r;
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar: " + e.getMessage());
        }

        return null;
    }

    // =====================================================
    // CONSULTAR TODOS
    // =====================================================
    public ArrayList<Usuario> consultarTodos() {

        ArrayList<Usuario> lista = new ArrayList<>();

        String sql = """
                SELECT usuario, password, CL1, CL2, CL3, CL4
                FROM seguridad
                ORDER BY usuario
                """;

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                 
                u.setCodigo(rs.getString("usuario"));
                u.setPassword(rs.getString("password"));
                u.claves[0] = rs.getString("CL1");
                u.claves[1] = rs.getString("CL2");
                u.claves[2] = rs.getString("CL3");
                u.claves[3] = rs.getString("CL4");

                lista.add(u);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar: " + e.getMessage());
        }

        return lista;
    }

    public static void main(String[] args) {

        SeguridadDAO dao = new SeguridadDAO();

        if (dao.adicionar(new Usuario("juan", "secreto"))) {
            System.out.println("Usuario agregado");
        }

        Usuario u = dao.buscar("wdiaz");

        if (u != null) {

            System.out.println("Usuario: " + u.getCodigo());
            System.out.println("Password: " + u.getPassword());

        } else {

            System.out.println("Usuario no encontrado");
        }
  
        if (dao.modificar(new Usuario("juan", "secreto"))) {
            System.out.println("Usuario modificado");
        }

        // CONSULTAR TODOS
        System.out.println("\nUsuarios registrados:");

        for (Usuario datos : dao.consultarTodos()) {

            System.out.println(
                    "Usuario: " + datos.getCodigo() +
                    " - Password: " + datos.getPassword());
        }

        // ELIMINAR
        if (dao.eliminar("juan")) {
            System.out.println("Usuario eliminado");
        }
    }
}
