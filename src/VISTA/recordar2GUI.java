/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package VISTA;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class recordar2GUI extends javax.swing.JDialog {

    public static String[] clV = new String[4];

    private JButton[] misBotones = new JButton[16];
    int contadorSel;
    Container contPane;
    private boolean confirmado = false;

    public recordar2GUI(java.awt.Frame parent, boolean modal) {
        this(parent, modal, "Seleccione una imagen de cada fila");
    }

    public recordar2GUI(java.awt.Frame parent, boolean modal, String titulo) {
        super(parent, modal);
        initComponents();
        this.setTitle(titulo);
        this.setResizable(false);
        contadorSel = 0;
        clV = new String[4];
        inicializarBotones();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String[] getSeleccion() {
        return clV.clone();
    }

    private static File buscarImagen(String ruta) {
        File f = new File(ruta);
        if (f.exists()) {
            return f;
        }
        File fLocal = new File("imagenes/" + f.getName());
        if (fLocal.exists()) {
            return fLocal;
        }
        return null;
    }

    private void reiniciar() {
        contadorSel = 0;
        clV = new String[4];
        for (JButton b : misBotones) {
            b.setEnabled(true);
            b.setBorder((javax.swing.border.Border) b.getClientProperty("bordeOriginal"));
        }
        btnConfirmar.setEnabled(false);
    }

    public void inicializarBotones() {
        contPane = panelCuadricula;
        misBotones = new JButton[]{
            jButton1, jButton2, jButton3, jButton4,
            jButton5, jButton6, jButton7, jButton8,
            jButton9, jButton10, jButton11, jButton12,
            jButton13, jButton14, jButton15, jButton16
        };

        String[] rutasImagenes = {
            "C:/imagenes/aleon.jpg",
            "C:/imagenes/atigre.jpg",
            "C:/imagenes/ajirafa.jpg",
            "C:/imagenes/aperro.jpg",
            "C:/imagenes/eplancha.jpg",
            "C:/imagenes/enevera.jpg",
            "C:/imagenes/elavadora.jpg",
            "C:/imagenes/eventilador.jpg",
            "C:/imagenes/rcamiseta.jpg",
            "C:/imagenes/rzapatos.jpg",
            "C:/imagenes/rpantalon.jpg",
            "C:/imagenes/rgorra.jpg",
            "C:/imagenes/vauto.jpg",
            "C:/imagenes/vcamion.jpg",
            "C:/imagenes/vlancha.jpg",
            "C:/imagenes/vtren.jpg"
        };

        ActionListener detectorClic = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton botonPresionado = (JButton) e.getSource();
                int fila = (int) botonPresionado.getClientProperty("fila");
                if (clV[fila] != null) {
                    return;
                }
                for (Component componente : contPane.getComponents()) {
                    if (componente instanceof JButton) {
                        JButton boton = (JButton) componente;
                        int f = (int) boton.getClientProperty("fila");
                        if (f == fila) {
                            boton.setEnabled(false);
                        }
                    }
                }

                botonPresionado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 160, 0), 4));
                botonPresionado.setDisabledIcon(botonPresionado.getIcon());

                String nombreImagen = (String) botonPresionado.getClientProperty("nombreImagen");
                int posult = nombreImagen.indexOf('.');
                String nombreI = nombreImagen.substring(1, posult);
                clV[fila] = nombreI;
                contadorSel++;
                btnConfirmar.setEnabled(contadorSel == 4);
            }
        };

        for (int i = 0; i < misBotones.length; i++) {
            String nombreArchivo = new File(rutasImagenes[i]).getName();
            if (misBotones[i].getIcon() == null) {
                File archivo = buscarImagen(rutasImagenes[i]);
                if (archivo != null) {
                    misBotones[i].setIcon(new ImageIcon(archivo.getAbsolutePath()));
                } else {
                    misBotones[i].setText(nombreArchivo);
                }
            }
            misBotones[i].setText("");
            misBotones[i].putClientProperty("bordeOriginal", misBotones[i].getBorder());
            misBotones[i].putClientProperty("fila", i / 4);
            misBotones[i].putClientProperty("columna", i % 4);
            misBotones[i].putClientProperty("nombreImagen", nombreArchivo);
            for (ActionListener al : misBotones[i].getActionListeners()) {
                misBotones[i].removeActionListener(al);
            }
            misBotones[i].addActionListener(detectorClic);
        }

        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblInstruccion = new javax.swing.JLabel();
        panelCuadricula = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        panelPie = new javax.swing.JPanel();
        btnReiniciar = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleccione una imagen de cada fila");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeVent(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(6, 6));

        lblInstruccion.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInstruccion.setText("  Elija UNA imagen de cada fila (4 en total) y pulse Confirmar");
        getContentPane().add(lblInstruccion, java.awt.BorderLayout.PAGE_START);

        panelCuadricula.setLayout(new java.awt.GridLayout(4, 4, 10, 10));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/aleon.jpg"))); // NOI18N
        jButton1.setToolTipText("León");
        panelCuadricula.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/atigre.jpg"))); // NOI18N
        jButton2.setToolTipText("Tigre");
        panelCuadricula.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ajirafa.jpg"))); // NOI18N
        jButton3.setToolTipText("Jirafa");
        panelCuadricula.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/aperro.jpg"))); // NOI18N
        jButton4.setToolTipText("Perro");
        panelCuadricula.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eplancha.jpg"))); // NOI18N
        jButton5.setToolTipText("Plancha");
        panelCuadricula.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/enevera.jpg"))); // NOI18N
        jButton6.setToolTipText("Nevera");
        panelCuadricula.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/elavadora.jpg"))); // NOI18N
        jButton7.setToolTipText("Lavadora");
        panelCuadricula.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eventilador.jpg"))); // NOI18N
        jButton8.setToolTipText("Ventilador");
        panelCuadricula.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rcamiseta.jpg"))); // NOI18N
        jButton9.setToolTipText("Camiseta");
        panelCuadricula.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rzapatos.jpg"))); // NOI18N
        jButton10.setToolTipText("Zapatos");
        panelCuadricula.add(jButton10);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rpantalon.jpg"))); // NOI18N
        jButton11.setToolTipText("Pantalón");
        panelCuadricula.add(jButton11);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rgorra.jpg"))); // NOI18N
        jButton12.setToolTipText("Gorra");
        panelCuadricula.add(jButton12);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vauto.jpg"))); // NOI18N
        jButton13.setToolTipText("Auto");
        panelCuadricula.add(jButton13);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vcamion.jpg"))); // NOI18N
        jButton14.setToolTipText("Camión");
        panelCuadricula.add(jButton14);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vlancha.jpg"))); // NOI18N
        jButton15.setToolTipText("Lancha");
        panelCuadricula.add(jButton15);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vtren.jpg"))); // NOI18N
        jButton16.setToolTipText("Tren");
        panelCuadricula.add(jButton16);

        getContentPane().add(panelCuadricula, java.awt.BorderLayout.CENTER);

        panelPie.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 8));

        btnReiniciar.setText("Reiniciar selección");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });
        panelPie.add(btnReiniciar);

        btnConfirmar.setText("Confirmar");
        btnConfirmar.setEnabled(false);
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        panelPie.add(btnConfirmar);

        getContentPane().add(panelPie, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resizeVent(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_resizeVent
    }//GEN-LAST:event_resizeVent

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        reiniciar();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        if (contadorSel == 4) {
            confirmado = true;
            dispose();
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(recordar2GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(recordar2GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(recordar2GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(recordar2GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                recordar2GUI dialog = new recordar2GUI(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JPanel panelCuadricula;
    private javax.swing.JPanel panelPie;
    // End of variables declaration//GEN-END:variables
}
