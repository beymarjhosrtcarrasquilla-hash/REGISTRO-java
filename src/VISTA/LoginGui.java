/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package VISTA;

import CONTROL.ControlSeguridad;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Ventana de inicio de sesion, creacion de usuario y recuperacion de contrasena.
 * Compatible con la pestana Design de NetBeans.
 *
 * @author beyma
 */
public class LoginGui extends javax.swing.JDialog {

    private static final int MAX_INTENTOS_IMAGENES = 3;

    private final ControlSeguridad control;
    private boolean autenticado = false;
    private String usuarioAutenticado = "";

    public LoginGui(java.awt.Frame parent, boolean modal) {
        this(parent, modal, new ControlSeguridad());
    }

    public LoginGui(java.awt.Frame parent, boolean modal, ControlSeguridad control) {
        super(parent, modal);
        this.control = control;
        initComponents();
        getRootPane().setDefaultButton(btnIngresar);
        actualizarBotonCrear();
        setLocationRelativeTo(parent);
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public String getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private void actualizarBotonCrear() {
        btnCrear.setVisible(true);
    }

    private void info(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Registro de clases",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Registro de clases",
                JOptionPane.ERROR_MESSAGE);
    }

    private void ingresar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            error("Escriba su usuario y contraseña.");
            return;
        }

        if (control.validarLogin(usuario, clave)) {
            autenticado = true;
            usuarioAutenticado = usuario;
            dispose();
        } else {
            error("Usuario o contraseña incorrectos.");
            txtClave.setText("");
            txtClave.requestFocusInWindow();
        }
    }

    private void crearUsuario() {
        JTextField usu = new JTextField(16);
        JPasswordField c1 = new JPasswordField(16);
        JPasswordField c2 = new JPasswordField(16);

        Object[] campos = {
            "Usuario", usu,
            "Contraseña", c1,
            "Confirmar contraseña", c2
        };

        if (JOptionPane.showConfirmDialog(
                this, campos, "Crear usuario",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }

        String usuario = usu.getText().trim();
        String clave = new String(c1.getPassword());
        String confirmacion = new String(c2.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            error("Escriba el usuario y la contraseña.");
            return;
        }

        if (!clave.equals(confirmacion)) {
            error("Las contraseñas no coinciden.");
            return;
        }

        if (control.existeUsuario(usuario)) {
            error("Ese usuario ya existe.");
            return;
        }

        info("Elija las 4 imágenes de recuperación, una por fila.");

        recordar2GUI sel = new recordar2GUI(
                null, true, "Imágenes de recuperación");
        sel.setVisible(true);

        if (!sel.isConfirmado()) {
            error("No se creó el usuario: faltó confirmar las imágenes.");
            return;
        }

        if (control.registrar(usuario, clave, sel.getSeleccion())) {
            info("Usuario creado. Ya puede iniciar sesión.");
            txtUsuario.setText(usuario);
            txtClave.setText("");
            txtClave.requestFocusInWindow();
            actualizarBotonCrear();
        } else {
            error("No se pudo crear el usuario.");
        }
    }

    private void recuperar() {
        String sugerido = txtUsuario.getText().trim();
        String usuario = (String) JOptionPane.showInputDialog(
                this,
                "Escriba su nombre o código de usuario:",
                "Recuperar contraseña",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                sugerido);

        if (usuario == null) {
            return;
        }

        usuario = usuario.trim();

        if (!control.existeUsuario(usuario)) {
            error("El usuario no existe.");
            return;
        }

        boolean coincide = false;

        for (int intento = 1;
                intento <= MAX_INTENTOS_IMAGENES && !coincide;
                intento++) {

            recordar2GUI sel = new recordar2GUI(
                    null, true, "Recuperar contraseña");
            sel.setVisible(true);

            if (!sel.isConfirmado()) {
                return;
            }

            coincide = control.verificarImagenes(
                    usuario, sel.getSeleccion());

            if (!coincide) {
                error("Las imágenes no coinciden."
                        + (intento < MAX_INTENTOS_IMAGENES
                        ? " Intento " + intento + " de "
                        + MAX_INTENTOS_IMAGENES + "." : " Se agotaron los intentos."));
            }
        }

        if (!coincide) {
            return;
        }

        JPasswordField n1 = new JPasswordField(16);
        JPasswordField n2 = new JPasswordField(16);

        Object[] campos = {
            "Nueva contraseña", n1,
            "Confirmar contraseña", n2
        };

        while (true) {
            if (JOptionPane.showConfirmDialog(
                    this, campos, "Nueva contraseña",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
                return;
            }

            String nueva = new String(n1.getPassword());
            String confirmacion = new String(n2.getPassword());

            if (nueva.isEmpty()) {
                error("La contraseña no puede estar vacía.");
            } else if (!nueva.equals(confirmacion)) {
                error("Las contraseñas no coinciden.");
            } else {
                if (control.cambiarPassword(usuario, nueva)) {
                    info("Contraseña actualizada correctamente.");
                    txtUsuario.setText(usuario);
                    txtClave.setText("");
                    txtClave.requestFocusInWindow();
                } else {
                    error("No se pudo guardar la nueva contraseña.");
                }
                return;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFondo = new javax.swing.JPanel();
        panelEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelTarjeta = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblClave = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnOlvide = new javax.swing.JButton();
        btnCrear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inicio de sesión - Registro de clases");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelFondo.setBackground(new java.awt.Color(245, 248, 253));
        panelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelEncabezado.setBackground(new java.awt.Color(0, 51, 153));
        panelEncabezado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Registro de clases");
        panelEncabezado.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 22, -1, -1));

        panelFondo.add(panelEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 80));

        panelTarjeta.setBackground(new java.awt.Color(255, 255, 255));
        panelTarjeta.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(225, 231, 240), 1, true));
        panelTarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(45, 55, 72));
        lblUsuario.setText("Usuario");
        panelTarjeta.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelTarjeta.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 42, 320, 36));

        lblClave.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblClave.setForeground(new java.awt.Color(45, 55, 72));
        lblClave.setText("Contraseña");
        panelTarjeta.add(lblClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtClave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtClave.addActionListener(this::txtClaveActionPerformed);
        panelTarjeta.add(txtClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 112, 320, 36));

        btnIngresar.setBackground(new java.awt.Color(0, 102, 255));
        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);
        panelTarjeta.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 162, 320, 38));

        btnOlvide.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnOlvide.setForeground(new java.awt.Color(0, 102, 255));
        btnOlvide.setText("Olvidé mi contraseña");
        btnOlvide.setBorderPainted(false);
        btnOlvide.setContentAreaFilled(false);
        btnOlvide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOlvide.addActionListener(this::btnOlvideActionPerformed);
        panelTarjeta.add(btnOlvide, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 320, 28));

        btnCrear.setBackground(new java.awt.Color(235, 243, 255));
        btnCrear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCrear.setForeground(new java.awt.Color(0, 51, 153));
        btnCrear.setText("Crear usuario");
        btnCrear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCrear.addActionListener(this::btnCrearActionPerformed);
        panelTarjeta.add(btnCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 246, 320, 34));

        panelFondo.add(panelTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 380, 305));

        getContentPane().add(panelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        ingresar();
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnOlvideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOlvideActionPerformed
        recuperar();
    }//GEN-LAST:event_btnOlvideActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        crearUsuario();
    }//GEN-LAST:event_btnCrearActionPerformed

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
        ingresar();
    }//GEN-LAST:event_txtClaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGui dialog = new LoginGui(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnOlvide;
    private javax.swing.JLabel lblClave;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelEncabezado;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelTarjeta;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
