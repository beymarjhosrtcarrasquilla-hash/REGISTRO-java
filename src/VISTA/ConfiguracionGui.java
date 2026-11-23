/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package VISTA;

import CONTROL.ConfiguracionDAO;
import CONTROL.Persistencia;
import CONTROL.Validaciones;
import MODELO.Configuracion;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana de CONFIGURACION DATO NO REPETIBLES.
 * Diseñada en NetBeans con Matisse GUI Builder.
 */
public class ConfiguracionGui extends javax.swing.JDialog {

    private static final int MAX_GRUPOS = 6;
    private boolean actualizandoCombo = false;
    private String usuarioSesion = "";

    private static final String[] FILAS_ITEM = {
        "Grupo",
        "Asignatura",
        "Dia",
        "hora",
        "Modalidad(P/R)",
        "Plataforma LMS",
        "Sede",
        "Programa",
        "Plataf videoConf",
        "Matriculados",
        "id Vocero",
        "nombre,apellido vocero",
        "email vocero",
        "telefono vocero"
    };

    /**
     * Creates new form ConfiguracionGui
     */
    public ConfiguracionGui(java.awt.Frame parent, boolean modal) {
        this(parent, modal, "");
    }

    public ConfiguracionGui(java.awt.Frame parent, boolean modal, String usuarioSesion) {
        super(parent, modal);
        this.usuarioSesion = usuarioSesion != null ? usuarioSesion.trim() : "";
        initComponents();
        this.setSize(860, 620);
        this.setPreferredSize(new java.awt.Dimension(860, 620));
        this.setLocationRelativeTo(parent);
        estilizarTabla();
        configurarColumnasTabla(3, false);
        cargarDatos();
    }

    private void estilizarTabla() {
        jTable1.setRowHeight(21);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        jTable1.getTableHeader().setForeground(Color.BLACK);
        jTable1.setGridColor(new Color(220, 226, 238));
        jTable1.setShowGrid(true);
        jTable1.setSelectionBackground(new Color(204, 229, 255));
        jTable1.setSelectionForeground(Color.BLACK);

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        headerRenderer.setForeground(Color.BLACK);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jTable1.getTableHeader().setDefaultRenderer(headerRenderer);

        // Al hacer clic en una columna de la tabla, sincroniza el selector [ Grupo X | v ]
        jTable1.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizandoCombo) {
                int col = jTable1.getSelectedColumn();
                if (col > 0) {
                    actualizandoCombo = true;
                    cboNroAsignaturas.setSelectedItem("Grupo " + col);
                    actualizandoCombo = false;
                }
            }
        });
    }

    private void actualizarComboGrupos(int cantidadGrupos, int seleccionado) {
        actualizandoCombo = true;
        cboNroAsignaturas.removeAllItems();
        for (int i = 1; i <= cantidadGrupos; i++) {
            cboNroAsignaturas.addItem("Grupo " + i);
        }
        if (seleccionado >= 1 && seleccionado <= cantidadGrupos) {
            cboNroAsignaturas.setSelectedItem("Grupo " + seleccionado);
        }
        txtNroAsignaturas.setText(String.valueOf(cantidadGrupos));
        actualizandoCombo = false;
    }

    private void configurarColumnasTabla(int cantidadGrupos, boolean preservarDatos) {
        DefaultTableModel prevModel = (DefaultTableModel) jTable1.getModel();
        int colsPrevias = prevModel.getColumnCount() - 1;

        Object[][] datosPrevios = null;
        if (preservarDatos && colsPrevias > 0) {
            datosPrevios = new Object[FILAS_ITEM.length][colsPrevias];
            for (int r = 0; r < FILAS_ITEM.length; r++) {
                for (int c = 0; c < colsPrevias; c++) {
                    datosPrevios[r][c] = prevModel.getValueAt(r, c + 1);
                }
            }
        }

        String[] columnas = new String[cantidadGrupos + 1];
        columnas[0] = "item";
        for (int i = 1; i <= cantidadGrupos; i++) {
            columnas[i] = "Grupo " + i;
        }

        DefaultTableModel modelo = new DefaultTableModel(columnas, FILAS_ITEM.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Columna 'item' es de solo lectura
            }
        };

        for (int i = 0; i < FILAS_ITEM.length; i++) {
            modelo.setValueAt(FILAS_ITEM[i], i, 0);
            if (datosPrevios != null) {
                for (int c = 0; c < Math.min(cantidadGrupos, colsPrevias); c++) {
                    modelo.setValueAt(datosPrevios[i][c], i, c + 1);
                }
            }
        }

        jTable1.setModel(modelo);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
        for (int i = 1; i <= cantidadGrupos; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(130);
        }

        actualizarComboGrupos(cantidadGrupos, 1);
    }

    private void eliminarColumnaEspecifica(int colAEliminar) {
        DefaultTableModel prevModel = (DefaultTableModel) jTable1.getModel();
        int colsPrevias = prevModel.getColumnCount() - 1;
        int nuevasCols = colsPrevias - 1;

        Object[][] datosNuevos = new Object[FILAS_ITEM.length][nuevasCols];

        for (int r = 0; r < FILAS_ITEM.length; r++) {
            int destCol = 0;
            for (int c = 1; c <= colsPrevias; c++) {
                if (c == colAEliminar) {
                    continue; // se salta la columna a eliminar
                }
                datosNuevos[r][destCol] = prevModel.getValueAt(r, c);
                destCol++;
            }
        }

        String[] columnas = new String[nuevasCols + 1];
        columnas[0] = "item";
        for (int i = 1; i <= nuevasCols; i++) {
            columnas[i] = "Grupo " + i;
        }

        DefaultTableModel nuevoModelo = new DefaultTableModel(columnas, FILAS_ITEM.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        for (int i = 0; i < FILAS_ITEM.length; i++) {
            nuevoModelo.setValueAt(FILAS_ITEM[i], i, 0);
            for (int c = 0; c < nuevasCols; c++) {
                nuevoModelo.setValueAt(datosNuevos[i][c], i, c + 1);
            }
        }

        jTable1.setModel(nuevoModelo);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
        for (int i = 1; i <= nuevasCols; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(130);
        }

        int selNuevo = Math.max(1, Math.min(colAEliminar, nuevasCols));
        actualizarComboGrupos(nuevasCols, selNuevo);
        jTable1.changeSelection(0, selNuevo, false, false);
    }

    private void cargarDatos() {
        Configuracion cfg = null;
        try {
            ConfiguracionDAO dao = new ConfiguracionDAO();
            cfg = dao.cargar(usuarioSesion);
        } catch (Exception e) {
            System.out.println("Error cargando de BD: " + e.getMessage());
        }

        if (cfg == null) {
            String archPersistencia = (!usuarioSesion.isEmpty()) ? "config_" + usuarioSesion + ".dat" : "config.dat";
            Persistencia p = new Persistencia("datos/", archPersistencia);
            if (p.existeArchivo()) {
                p.abrirArchivoEntrada();
                Object obj = p.cargar();
                p.cerrarArchivoEntrada();
                if (obj instanceof Configuracion) {
                    cfg = (Configuracion) obj;
                }
            }
        }

        if (cfg == null) {
            int idNuevo = ConfiguracionDAO.generarIdUsuario(usuarioSesion);
            txtIdentificacion.setText(String.valueOf(idNuevo));
            return;
        }

        if (cfg.getIdentificacion() > 0) {
            txtIdentificacion.setText(String.valueOf(cfg.getIdentificacion()));
        } else {
            int idNuevo = ConfiguracionDAO.generarIdUsuario(usuarioSesion);
            txtIdentificacion.setText(String.valueOf(idNuevo));
        }
        if (cfg.getNombres() != null) {
            txtNombre.setText(cfg.getNombres());
        }
        if (cfg.getApellidos() != null) {
            txtApellidos.setText(cfg.getApellidos());
        }

        String prog = cfg.getPrograma();
        if ("Ingenieria de Sistemas".equalsIgnoreCase(prog)) {
            rdoSistemas.setSelected(true);
        } else if ("Ingenieria Electronica".equalsIgnoreCase(prog)) {
            rdoElectronica.setSelected(true);
        } else if ("Ingenieria Industrial".equalsIgnoreCase(prog)) {
            rdoIndustrial.setSelected(true);
        }

        int cantidad = cfg.getNroAsignaturas();
        if (cantidad <= 0 || cantidad > MAX_GRUPOS) {
            cantidad = 3;
        }

        configurarColumnasTabla(cantidad, false);
        actualizarComboGrupos(cantidad, 1);

        for (int i = 0; i < cantidad; i++) {
            int tCol = i + 1;
            if (cfg.getGrupo(i) != null) {
                jTable1.setValueAt(cfg.getGrupo(i), 0, tCol);
            }
            if (cfg.getAsignatura(i) != null) {
                jTable1.setValueAt(cfg.getAsignatura(i), 1, tCol);
            }
            if (cfg.getDiaSemana(i) != null) {
                jTable1.setValueAt(cfg.getDiaSemana(i), 2, tCol);
            }
            if (cfg.getHora(i) > 0) {
                jTable1.setValueAt(String.valueOf(cfg.getHora(i)), 3, tCol);
            }
            if (cfg.getModalidad(i) != null) {
                jTable1.setValueAt(cfg.getModalidad(i), 4, tCol);
            }
            if (cfg.getPlataformaLMS(i) != null) {
                jTable1.setValueAt(cfg.getPlataformaLMS(i), 5, tCol);
            }
            if (cfg.getSede(i) != null) {
                jTable1.setValueAt(cfg.getSede(i), 6, tCol);
            }
            if (cfg.getProgramaGrupo(i) != null) {
                jTable1.setValueAt(cfg.getProgramaGrupo(i), 7, tCol);
            }
            if (cfg.getPlataVideo(i) != null) {
                jTable1.setValueAt(cfg.getPlataVideo(i), 8, tCol);
            }
            if (cfg.getEstMatriculados(i) > 0) {
                jTable1.setValueAt(String.valueOf(cfg.getEstMatriculados(i)), 9, tCol);
            }
            if (cfg.getVoceroId(i) > 0) {
                jTable1.setValueAt(String.valueOf(cfg.getVoceroId(i)), 10, tCol);
            }
            if (cfg.getVoceroApNombre(i) != null) {
                jTable1.setValueAt(cfg.getVoceroApNombre(i), 11, tCol);
            }
            if (cfg.getVoceroemail(i) != null) {
                jTable1.setValueAt(cfg.getVoceroemail(i), 12, tCol);
            }
            if (cfg.getVoceroTel(i) > 0) {
                jTable1.setValueAt(String.valueOf(cfg.getVoceroTel(i)), 13, tCol);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        panelFondo = new javax.swing.JPanel();
        panelCabecera = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdentificacion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNroAsignaturas = new javax.swing.JTextField();
        rdoSistemas = new javax.swing.JRadioButton();
        rdoElectronica = new javax.swing.JRadioButton();
        rdoIndustrial = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        btnAgregarGrupo = new javax.swing.JButton();
        btnQuitarGrupo = new javax.swing.JButton();
        cboNroAsignaturas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnCargar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        panelPie = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración - Registro de clases");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelFondo.setBackground(new java.awt.Color(245, 248, 253));
        panelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelCabecera.setBackground(new java.awt.Color(0, 51, 153));
        panelCabecera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CONFIGURACION DATO NO REPETIBLES");
        panelCabecera.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 820, 30));

        panelFondo.add(panelCabecera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 153));
        jLabel2.setText("Identificación");
        panelFondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 60, 110, 18));

        txtIdentificacion.setEditable(false);
        txtIdentificacion.setBackground(new java.awt.Color(240, 240, 240));
        txtIdentificacion.setFocusable(false);
        panelFondo.add(txtIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 80, 110, 26));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Nombre");
        panelFondo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 130, 18));
        panelFondo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 130, 26));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Apellidos");
        panelFondo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 60, 140, 18));
        panelFondo.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 80, 140, 26));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("NRO de asignaturas");
        panelFondo.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 120, 18));

        txtNroAsignaturas.setEditable(false);
        txtNroAsignaturas.setBackground(new java.awt.Color(255, 255, 255));
        txtNroAsignaturas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNroAsignaturas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNroAsignaturas.setText("3");
        txtNroAsignaturas.setToolTipText("Total de asignaturas configuradas");
        txtNroAsignaturas.addActionListener(this::txtNroAsignaturasActionPerformed);
        panelFondo.add(txtNroAsignaturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 60, 40, 26));

        buttonGroup1.add(rdoSistemas);
        rdoSistemas.setText("Ingenieria de Sistemas");
        panelFondo.add(rdoSistemas, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 116, 180, 22));

        buttonGroup1.add(rdoElectronica);
        rdoElectronica.setText("Ingenieria Electronica");
        panelFondo.add(rdoElectronica, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 116, 180, 22));

        buttonGroup1.add(rdoIndustrial);
        rdoIndustrial.setText("Ingenieria Industrial");
        panelFondo.add(rdoIndustrial, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 116, 180, 22));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Ingrese las asignaturas una en cada linea:");
        panelFondo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 150, 350, 22));

        btnAgregarGrupo.setBackground(new java.awt.Color(0, 85, 170));
        btnAgregarGrupo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarGrupo.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarGrupo.setText("Agg grupo");
        btnAgregarGrupo.setToolTipText("Agregar nuevo grupo");
        btnAgregarGrupo.addActionListener(this::btnAgregarGrupoActionPerformed);
        panelFondo.add(btnAgregarGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 146, 95, 28));

        btnQuitarGrupo.setBackground(new java.awt.Color(192, 57, 43));
        btnQuitarGrupo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQuitarGrupo.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitarGrupo.setText("Eliminar");
        btnQuitarGrupo.setToolTipText("Eliminar grupo seleccionado");
        btnQuitarGrupo.addActionListener(this::btnQuitarGrupoActionPerformed);
        panelFondo.add(btnQuitarGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 146, 85, 28));

        cboNroAsignaturas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboNroAsignaturas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Grupo 1", "Grupo 2", "Grupo 3" }));
        cboNroAsignaturas.setToolTipText("Grupo seleccionado");
        cboNroAsignaturas.addActionListener(this::cboNroAsignaturasActionPerformed);
        panelFondo.add(cboNroAsignaturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 146, 135, 28));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "item", "Grupo 1", "Grupo 2", "Grupo 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        panelFondo.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 180, 800, 320));

        btnGuardar.setBackground(new java.awt.Color(0, 51, 153));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);
        panelFondo.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 515, 105, 34));

        btnCargar.setBackground(new java.awt.Color(102, 102, 102));
        btnCargar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCargar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar.setText("Cargar");
        btnCargar.addActionListener(this::btnCargarActionPerformed);
        panelFondo.add(btnCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 515, 105, 34));

        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/Boton regresar.png"))); // NOI18N
        btnRegresar.setBorderPainted(false);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);
        panelFondo.add(btnRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 510, 45, 41));

        panelPie.setBackground(new java.awt.Color(0, 51, 153));
        panelPie.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelFondo.add(panelPie, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 565, 860, 15));

        getContentPane().add(panelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 580));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboNroAsignaturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNroAsignaturasActionPerformed
        if (actualizandoCombo) {
            return;
        }
        Object itemSel = cboNroAsignaturas.getSelectedItem();
        if (itemSel == null) {
            return;
        }
        String selStr = itemSel.toString().trim();
        if (selStr.startsWith("Grupo ")) {
            try {
                int col = Integer.parseInt(selStr.replace("Grupo ", "").trim());
                if (col > 0 && col < jTable1.getColumnCount()) {
                    jTable1.changeSelection(0, col, false, false);
                }
            } catch (Exception ignored) {
            }
        }
    }//GEN-LAST:event_cboNroAsignaturasActionPerformed

    private void btnAgregarGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarGrupoActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int actual = model.getColumnCount() - 1;
        if (actual >= MAX_GRUPOS) {
            JOptionPane.showMessageDialog(this,
                    "Ha alcanzado el número máximo de grupos permitidos (" + MAX_GRUPOS + ").",
                    "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nuevo = actual + 1;
        String codGrupo = JOptionPane.showInputDialog(
                this,
                "Ingrese el código o nombre del nuevo grupo (ej. 3303B):",
                "Agregar nuevo grupo",
                JOptionPane.QUESTION_MESSAGE
        );

        if (codGrupo == null) {
            return; // Usuario canceló
        }

        codGrupo = codGrupo.trim();
        configurarColumnasTabla(nuevo, true);

        if (!codGrupo.isEmpty()) {
            jTable1.setValueAt(codGrupo, 0, nuevo);
        }

        actualizarComboGrupos(nuevo, nuevo);
        jTable1.changeSelection(0, nuevo, false, false);
    }//GEN-LAST:event_btnAgregarGrupoActionPerformed

    private void btnQuitarGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarGrupoActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int actual = model.getColumnCount() - 1;
        if (actual <= 1) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos 1 grupo configurado.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int colAEliminar = 1;
        String grupoSel = (String) cboNroAsignaturas.getSelectedItem();
        if (grupoSel != null && grupoSel.startsWith("Grupo ")) {
            try {
                colAEliminar = Integer.parseInt(grupoSel.replace("Grupo ", "").trim());
            } catch (Exception e) {
                colAEliminar = actual;
            }
        } else {
            int sel = jTable1.getSelectedColumn();
            if (sel > 0 && sel <= actual) {
                colAEliminar = sel;
            }
        }

        Object codEliminar = jTable1.getValueAt(0, colAEliminar);
        String detalle = (codEliminar != null && !codEliminar.toString().trim().isEmpty())
                ? "Grupo " + colAEliminar + " [" + codEliminar.toString().trim() + "]"
                : "Grupo " + colAEliminar;

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar el " + detalle + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            eliminarColumnaEspecifica(colAEliminar);
        }
    }//GEN-LAST:event_btnQuitarGrupoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (jTable1.isEditing() && jTable1.getCellEditor() != null) {
            jTable1.getCellEditor().stopCellEditing();
        }
        String idStr = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();

        if (Validaciones.esVacio(idStr)) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la identificación del docente.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtIdentificacion.requestFocus();
            return;
        }
        if (!Validaciones.esEnteroPositivo(idStr)) {
            JOptionPane.showMessageDialog(this, "La identificación debe ser un número entero válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtIdentificacion.requestFocus();
            return;
        }
        if (Validaciones.esVacio(nombre)) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del docente.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if (Validaciones.esVacio(apellidos)) {
            JOptionPane.showMessageDialog(this, "Debe ingresar los apellidos del docente.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtApellidos.requestFocus();
            return;
        }

        String programa = "";
        if (rdoSistemas.isSelected()) {
            programa = "Ingenieria de Sistemas";
        } else if (rdoElectronica.isSelected()) {
            programa = "Ingenieria Electronica";
        } else if (rdoIndustrial.isSelected()) {
            programa = "Ingenieria Industrial";
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un programa académico.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int cantidad = model.getColumnCount() - 1;

        Configuracion cfg = new Configuracion(
                Integer.parseInt(idStr),
                apellidos,
                nombre,
                programa,
                cantidad
        );

        for (int i = 0; i < cantidad; i++) {
            int tCol = i + 1;
            cfg.setGrupo(getValorCelda(0, tCol), i);
            cfg.setAsignatura(getValorCelda(1, tCol), i);
            cfg.setDiaSemana(getValorCelda(2, tCol), i);

            String horaStr = getValorCelda(3, tCol);
            cfg.setHora(Validaciones.esEntero(horaStr) ? Integer.parseInt(horaStr) : 0, i);

            cfg.setModalidad(getValorCelda(4, tCol), i);
            cfg.setPlataformaLMS(getValorCelda(5, tCol), i);
            cfg.setSede(getValorCelda(6, tCol), i);
            cfg.setProgramaGrupo(getValorCelda(7, tCol), i);
            cfg.setPlataVideo(getValorCelda(8, tCol), i);

            String matStr = getValorCelda(9, tCol);
            cfg.setEstMatriculados(Validaciones.esEntero(matStr) ? Integer.parseInt(matStr) : 0, i);

            String vocIdStr = getValorCelda(10, tCol);
            cfg.setVoceroId(Validaciones.esEntero(vocIdStr) ? Integer.parseInt(vocIdStr) : 0, i);

            cfg.setVoceroApNombre(getValorCelda(11, tCol), i);
            cfg.setVoceroemail(getValorCelda(12, tCol), i);

            String telStr = getValorCelda(13, tCol);
            if (!telStr.isEmpty()) {
                if (!Validaciones.esSoloDigitos(telStr) || !Validaciones.esLong(telStr)) {
                    JOptionPane.showMessageDialog(this,
                            "El teléfono del vocero en el Grupo " + tCol + " debe contener solo dígitos numéricos válidos.",
                            "Validación", JOptionPane.WARNING_MESSAGE);
                    jTable1.changeSelection(13, tCol, false, false);
                    jTable1.requestFocus();
                    return;
                }
                cfg.setVoceroTel(Long.parseLong(telStr), i);
            } else {
                cfg.setVoceroTel(0L, i);
            }
        }

        boolean guardadoBD = false;
        try {
            ConfiguracionDAO dao = new ConfiguracionDAO();
            dao.guardar(usuarioSesion, cfg);
            guardadoBD = true;
        } catch (Exception ex) {
            System.out.println("Error guardando en BD: " + ex.getMessage());
        }

        boolean guardadoPersistencia = false;
        String archPersistencia = (!usuarioSesion.isEmpty()) ? "config_" + usuarioSesion + ".dat" : "config.dat";
        try {
            Persistencia p = new Persistencia("datos/", archPersistencia);
            p.abrirArchivoSalida();
            p.guardar(cfg);
            p.cerrarArchivoSalida();
            guardadoPersistencia = true;
        } catch (Exception ex) {
            System.out.println("Error guardando persistencia: " + ex.getMessage());
        }

        if (guardadoBD || guardadoPersistencia) {
            JOptionPane.showMessageDialog(this,
                    "Configuración guardada exitosamente.\n"
                    + "• Base de datos SQLite: " + (guardadoBD ? "OK" : "Error") + "\n"
                    + "• Archivo " + archPersistencia + ": " + (guardadoPersistencia ? "OK" : "Error"),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la configuración en la base de datos y archivo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private String getValorCelda(int fila, int col) {
        Object v = jTable1.getValueAt(fila, col);
        return v == null ? "" : v.toString().trim();
    }

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        cargarDatos();
        JOptionPane.showMessageDialog(this,
                "Datos de configuración cargados correctamente.",
                "Carga completa", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnCargarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void txtNroAsignaturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroAsignaturasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroAsignaturasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConfiguracionGui dialog = new ConfiguracionGui(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAgregarGrupo;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitarGrupo;
    private javax.swing.JButton btnRegresar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboNroAsignaturas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelPie;
    private javax.swing.JRadioButton rdoElectronica;
    private javax.swing.JRadioButton rdoIndustrial;
    private javax.swing.JRadioButton rdoSistemas;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtIdentificacion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNroAsignaturas;
    // End of variables declaration//GEN-END:variables
}
