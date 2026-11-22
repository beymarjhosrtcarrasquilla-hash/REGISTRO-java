package VISTA;

import CONTROL.ControlRegistro;
import CONTROL.ConfiguracionDAO;
import MODELO.Configuracion;
import MODELO.Registro;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 * Ventana principal del sistema de registro de clases.
 * Compatible con la pestana Design de NetBeans.
 *
 * @author beyma
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        this("");
    }

    // Ventana de la cuenta que inicio sesion: solo muestra sus registros
    public VentanaPrincipal(String usuario) {
        control = new ControlRegistro(usuario);
        initComponents();
        iniciar();
    }

    // ---------------------------------------------------------------
    // LOGICA DE LA VENTANA (fuera del codigo generado por el disenador)
    // ---------------------------------------------------------------
    private static final int RECIENTES = 10;

    private final ControlRegistro control;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ArrayList<Registro> mostrados = new ArrayList<>();
    private boolean verTodos = false;

    private void iniciar() {
        setTitle("Registro de clases - " + control.getUsuario());
        setLocationRelativeTo(null);

        // Si el icono no pudo cargarse, se muestra un texto alternativo
        javax.swing.Icon ic = jButton5.getIcon();
        if (ic == null || (ic instanceof javax.swing.ImageIcon ii
                && ii.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE)) {
            jButton5.setIcon(null);
            jButton5.setText("\uD83D\uDC64");
        }

        // Ajuste del boton de configuracion (engranaje) para que no se trunque a "..." en Nimbus Look & Feel
        jButton2.setBorder(null);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI Symbol", java.awt.Font.BOLD, 22));
        jButton2.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        // Usamos la tabla jTable1 definida en el formulario Design
        tabla = jTable1;
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        int[] anchos = {75, 90, 55, 65, 95, 70, 200};
        for (int i = 0; i < anchos.length && i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        JPopupMenu menu = new JPopupMenu();
        JMenuItem miModificar = new JMenuItem("Modificar");
        JMenuItem miEliminar = new JMenuItem("Eliminar");
        miModificar.addActionListener(e -> modificarSeleccionado());
        miEliminar.addActionListener(e -> eliminarSeleccionado());
        menu.add(miModificar);
        menu.add(miEliminar);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    modificarSeleccionado();
                }
            }

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = tabla.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        tabla.setRowSelectionInterval(fila, fila);
                        menu.show(tabla, e.getX(), e.getY());
                    }
                }
            }
        });
        tabla.getInputMap(javax.swing.JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "eliminar");
        tabla.getActionMap().put("eliminar", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eliminarSeleccionado();
            }
        });

        // "Ver todos" alterna entre los registros recientes y todos los registros
        jLabel5.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                verTodos = !verTodos;
                jLabel5.setText(verTodos ? "Ver recientes" : "Ver todos");
                jLabel1.setText(verTodos ? "Todos los registros " : "Registros recientes ");
                cargarTabla();
            }
        });

        cargarTabla();
    }

    /** Vuelve a leer la BD y llena la tabla. */
    private void cargarTabla() {
        try {
            control.cargarDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer la base de datos:\n" + ex.getMessage(),
                    "Registro de clases", JOptionPane.ERROR_MESSAGE);
        }
        ArrayList<Registro> todos = control.listar();
        mostrados = verTodos || todos.size() <= RECIENTES
                ? todos : new ArrayList<>(todos.subList(0, RECIENTES));
        modelo.setRowCount(0);
        for (Registro r : mostrados) {
            modelo.addRow(new Object[]{
                String.format("%02d/%02d/%04d", r.getDia(), r.getMes(), r.getAno()),
                r.getAsignatura(),
                r.getGrupo(),
                r.getNroEncuentro(),
                r.getTipoEncuentro(),
                r.getNroNoAsisten(),
                r.getTrabajoRealizado()
            });
        }
    }

    private Registro registroSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= mostrados.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.",
                    "Registro de clases", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return mostrados.get(fila);
    }

    private void abrirRegistro(Registro r) {
        RegistroGui dialogo = new RegistroGui(this, true, control);
        if (r != null) {
            dialogo.setRegistro(r);
        }
        dialogo.setVisible(true);
        cargarTabla();
    }

    private void modificarSeleccionado() {
        Registro r = registroSeleccionado();
        if (r != null) {
            abrirRegistro(r);
        }
    }

    private void eliminarSeleccionado() {
        Registro r = registroSeleccionado();
        if (r == null) {
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el registro de " + r.getAsignatura() + " " + r.getGrupo()
                + ", encuentro " + r.getNroEncuentro()
                + " (" + String.format("%02d/%02d/%04d", r.getDia(), r.getMes(), r.getAno()) + ")?",
                "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (!control.eliminarEnBD(r.getAsignatura(), r.getGrupo(), r.getNroEncuentro())) {
                JOptionPane.showMessageDialog(this, "El registro ya no existe.",
                        "Registro de clases", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar:\n" + ex.getMessage(),
                    "Registro de clases", JOptionPane.ERROR_MESSAGE);
        }
        cargarTabla();
    }

    private Configuracion cargarConfiguracion() {
        try {
            return new ConfiguracionDAO().cargar(control.getUsuario());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la configuración:\n" + ex.getMessage(),
                    "Registro de clases", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void mostrarTablaDialogo(String titulo, String[] columnas, Object[][] filas, int ancho, int alto) {
        DefaultTableModel m = new DefaultTableModel(filas, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable t = new JTable(m);
        t.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(ancho, alto));
        JOptionPane.showMessageDialog(this, sp, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarPerfilDocente() {
        Configuracion c = cargarConfiguracion();
        if (c == null) {
            return;
        }
        String info = "Identificación: " + c.getIdentificacion() + "\n"
                + "Nombres: " + c.getNombres() + "\n"
                + "Apellidos: " + c.getApellidos() + "\n"
                + "Programa: " + c.getPrograma() + "\n"
                + "Grupos asignados: " + c.getNroAsignaturas() + "\n"
                + "Clases registradas: " + control.size();
        JOptionPane.showMessageDialog(this, info, "Perfil del docente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarHorario() {
        Configuracion c = cargarConfiguracion();
        if (c == null) {
            return;
        }
        Object[][] filas = new Object[c.getNroAsignaturas()][6];
        for (int i = 0; i < c.getNroAsignaturas(); i++) {
            int h = c.getHora(i);
            String horaTxt = String.format("%02d:%02d", h / 100, h % 100);
            filas[i] = new Object[]{
                c.getDiaSemana(i),
                horaTxt,
                c.getAsignatura(i),
                c.getGrupo(i),
                c.getModalidad(i),
                c.getSede(i)
            };
        }
        mostrarTablaDialogo("Horario del docente",
                new String[]{"Día", "Hora", "Asignatura", "Grupo", "Modalidad", "Sede"},
                filas, 560, 160);
    }

    private void mostrarAsignaturas() {
        Configuracion c = cargarConfiguracion();
        if (c == null) {
            return;
        }
        Object[][] filas = new Object[c.getNroAsignaturas()][6];
        for (int i = 0; i < c.getNroAsignaturas(); i++) {
            int registrados = 0;
            try {
                registrados = Math.max(0, control.siguienteEncuentro(c.getAsignatura(i), c.getGrupo(i)) - 1);
            } catch (SQLException ex) {
                // se deja en 0 si falla la consulta
            }
            filas[i] = new Object[]{
                c.getAsignatura(i),
                c.getGrupo(i),
                c.getProgramaGrupo(i),
                c.getPlataformaLMS(i),
                c.getEstMatriculados(i),
                registrados
            };
        }
        mostrarTablaDialogo("Asignaturas asignadas",
                new String[]{"Asignatura", "Grupo", "Programa", "Plataforma LMS", "Matriculados", "Clases registradas"},
                filas, 620, 160);
    }

    private void mostrarGrupos() {
        Configuracion c = cargarConfiguracion();
        if (c == null) {
            return;
        }
        Object[][] filas = new Object[c.getNroAsignaturas()][6];
        for (int i = 0; i < c.getNroAsignaturas(); i++) {
            filas[i] = new Object[]{
                c.getGrupo(i),
                c.getAsignatura(i),
                c.getSede(i),
                c.getEstMatriculados(i),
                c.getVoceroApNombre(i),
                c.getVoceroTel(i)
            };
        }
        mostrarTablaDialogo("Mis grupos y voceros",
                new String[]{"Grupo", "Asignatura", "Sede", "Matriculados", "Vocero", "Teléfono vocero"},
                filas, 600, 160);
    }

    private void configurarDocente() {
        ConfiguracionGui dialog = new ConfiguracionGui(this, true, control.getUsuario());
        dialog.setVisible(true);
    }

    private void salir() {
        if (JOptionPane.showConfirmDialog(this, "¿Desea cerrar la aplicación?",
                "Registro de clases", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
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

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de clases - " + control.getUsuario());
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Registros recientes ");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, -1, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Consulte o corrija registros anteriores");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 102, 255));
        jButton1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Registrar Clase");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 180, 40));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Administra tus clases y consulta tus registros academicos");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, -1, -1));

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));

        jButton3.setBackground(new java.awt.Color(0, 51, 153));
        jButton3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("➜] Cerrar sesion");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setBackground(new java.awt.Color(0, 51, 153));
        jButton4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("⬅️  salir ");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/perfil.png"))); // NOI18N
        jButton5.setToolTipText("Perfil del docente");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setBackground(new java.awt.Color(0, 51, 153));
        jButton6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("👥  Mis grupos");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setBackground(new java.awt.Color(0, 51, 153));
        jButton7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(" ◷ Horario");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setBackground(new java.awt.Color(0, 51, 153));
        jButton8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("📚 Asignaturas");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(29, 29, 29)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel4.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, 530));
        jPanel4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 650, 10));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 255));
        jLabel5.setText("Ver todos");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 580, -1, -1));

        jPanel3.setBackground(new java.awt.Color(0, 51, 153));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Registro de clases");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jButton2.setBackground(new java.awt.Color(0, 51, 153));
        jButton2.setFont(new java.awt.Font("Segoe UI Symbol", 1, 22)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("⚙");
        jButton2.setToolTipText("Configuración del docente");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 40, 40));

        jPanel4.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 100));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Asignatura", "Grupo", "Encuentro", "Tipo", "No asisten", "Temas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("Doble clic: modificar | Clic derecho: modificar o eliminar | Supr: eliminar");
        jTable1.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, 620, 190));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Gestion academica");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        abrirRegistro(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        configurarDocente();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "¿Desea cerrar la sesión actual?",
                "Cerrar sesión", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        setVisible(false);
        dispose();

        LoginGui login = new LoginGui(null, true);
        login.setVisible(true);

        if (login.isAutenticado()) {
            new VentanaPrincipal(login.getUsuarioAutenticado()).setVisible(true);
        } else {
            System.exit(0);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        salir();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        mostrarPerfilDocente();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        mostrarGrupos();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        mostrarHorario();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        mostrarAsignaturas();
    }//GEN-LAST:event_jButton8ActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Primero el inicio de sesion; la ventana principal solo se abre si se autentica
            LoginGui login = new LoginGui(null, true);
            login.setVisible(true);
            if (login.isAutenticado()) {
                new VentanaPrincipal(login.getUsuarioAutenticado()).setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
