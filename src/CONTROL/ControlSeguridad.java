package CONTROL;

import MODELO.Usuario;
import javax.crypto.SecretKey;

public class ControlSeguridad {

    private static SecretKey llave;
    private final SeguridadDAO dao = new SeguridadDAO();

    // ---------------------------------------------------------------- llave
    private static synchronized SecretKey llave() {
        if (llave != null) {
            return llave;
        }
        Persistencia per = new Persistencia(); // datos/secretKey.dat
        if (per.existeArchivo()) {
            per.abrirArchivoEntrada();
            Object o = per.cargar();
            per.cerrarArchivoEntrada();
            if (o instanceof SecretKey) {
                llave = (SecretKey) o;
                return llave;
            }
        }
        SecretKey nueva = SEGURIDAD.generarLlave();
        per.abrirArchivoSalida();
        per.guardar(nueva);
        per.cerrarArchivoSalida();
        llave = nueva;
        return llave;
    }

    private static String cifrar(String texto) throws Exception {
        return SEGURIDAD.encriptar(texto, llave());
    }

    private static String descifrar(String guardado) {
        if (guardado == null) {
            return null;
        }
        try {
            return SEGURIDAD.desencriptar(guardado, llave());
        } catch (Exception e) {
            return guardado;
        }
    }

    private static boolean esCifrado(String guardado) {
        try {
            SEGURIDAD.desencriptar(guardado, llave());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ------------------------------------------------------------- consultas
    public boolean existenUsuarios() {
        return !dao.consultarTodos().isEmpty();
    }

    public boolean existeUsuario(String codigo) {
        return codigo != null && !codigo.isBlank() && dao.buscar(codigo.trim()) != null;
    }

    // -------------------------------------------------------------- registro
    public boolean registrar(String codigo, String password, String[] imagenes) {
        if (codigo == null || codigo.isBlank() || password == null || password.isEmpty()
                || imagenes == null || imagenes.length != 4) {
            return false;
        }
        for (String s : imagenes) {
            if (s == null || s.isBlank()) {
                return false;
            }
        }
        codigo = codigo.trim();
        if (dao.buscar(codigo) != null) {
            return false;
        }
        try {
            Usuario u = new Usuario(codigo, cifrar(password));
            for (int i = 0; i < 4; i++) {
                u.claves[i] = cifrar(imagenes[i]);
            }
            return dao.adicionar(u);
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------------------- login
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

    private void migrarSiHaceFalta(Usuario u) {
        try {
            boolean cambio = false;
            if (!esCifrado(u.getPassword())) {
                u.setPassword(cifrar(u.getPassword()));
                cambio = true;
            }
            for (int i = 0; i < 4; i++) {
                if (u.claves[i] != null && !esCifrado(u.claves[i])) {
                    u.claves[i] = cifrar(u.claves[i]);
                    cambio = true;
                }
            }
            if (cambio) {
                dao.modificar(u);
            }
        } catch (Exception e) {
            System.out.println("No se pudo migrar el usuario: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------ recuperacion
    public boolean verificarImagenes(String codigo, String[] respuestas) {
        if (codigo == null || respuestas == null || respuestas.length != 4) {
            return false;
        }
        Usuario u = dao.buscar(codigo.trim());
        if (u == null) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            String esperada = descifrar(u.claves[i]);
            if (esperada == null || respuestas[i] == null || !esperada.equals(respuestas[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean cambiarPassword(String codigo, String nueva) {
        if (codigo == null || nueva == null || nueva.isEmpty()) {
            return false;
        }
        Usuario u = dao.buscar(codigo.trim());
        if (u == null) {
            return false;
        }
        try {
            u.setPassword(cifrar(nueva));
            return dao.modificar(u);
        } catch (Exception e) {
            System.out.println("Error al cambiar la contrasena: " + e.getMessage());
            return false;
        }
    }
}
