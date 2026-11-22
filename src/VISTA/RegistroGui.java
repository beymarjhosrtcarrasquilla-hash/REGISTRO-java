/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package VISTA;

import CONTROL.ControlRegistro;
import CONTROL.ConfiguracionDAO;
import MODELO.Configuracion;
import MODELO.Registro;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author beyma
 */
public class RegistroGui extends javax.swing.JDialog {

    /**
     * Creates new form RegistroGui
     */
    public RegistroGui(java.awt.Frame parent, boolean modal) {
        this(parent, modal, new ControlRegistro());
    }

    public RegistroGui(java.awt.Frame parent, boolean modal, ControlRegistro control) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(this);
        this.control = control;
        iniciar();
        this.setLocationRelativeTo(parent);
    }

    // ---------------------------------------------------------------
    // LOGICA DE LA VENTANA (fuera del codigo generado por el disenador)
    // ---------------------------------------------------------------
    private final ControlRegistro control;
    private Configuracion config;
    private Registro original;      // distinto de null cuando se esta modificando
    private boolean guardado = false;

    /** true si se guardo o modifico un registro en esta ventana. */
    public boolean isGuardado() {
        return guardado;
    }

    private JRadioButton[] radiosGrupo() {
        return new JRadioButton[]{jRadioButton1, jRadioButton2, jRadioButton3, jRadioButton4, jRadioButton5};
    }

    private JRadioButton[] radiosPlataforma() {
        return new JRadioButton[]{jRadioButton6, jRadioButton7, jRadioButton8, jRadioButton9};
    }

    private void iniciar() {
        setTitle("Registro de clases");

        jButton1.setToolTipText("Regresar");
        jButton2.setToolTipText("Guardar");
        jButton3.setToolTipText("Limpiar");

        // Calendario: al pulsar el icono se abre el selector y la fecha elegida se escribe en el campo
        jLabel10.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.setToolTipText("Elegir fecha en el calendario");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                elegirFecha();
            }
        });

        for (JRadioButton rb : radiosGrupo()) {
            rb.addActionListener(e -> seleccionarGrupo());
        }

        try {
            config = new ConfiguracionDAO().cargar(control.getUsuario());
        } catch (SQLException ex) {
            error("No se pudo cargar la configuracion del docente:\n" + ex.getMessage());
        }
        if (config != null) {
            jTextField5.setText(String.valueOf(config.getIdentificacion()));
            jTextField9.setText(config.getApellidos());
            jTextField8.setText(config.getNombres());
            jTextField6.setText(config.getPrograma());

            // Actualizar dinámicamente los radio buttons según los grupos y asignaturas configurados
            JRadioButton[] radios = radiosGrupo();
            int nro = config.getNroAsignaturas();
            for (int i = 0; i < radios.length; i++) {
                if (i < nro && config.getGrupo(i) != null && !config.getGrupo(i).trim().isEmpty()) {
                    String asig = config.getAsignatura(i) != null ? config.getAsignatura(i).trim() : "";
                    radios[i].setText(config.getGrupo(i).trim() + (asig.isEmpty() ? "" : " " + asig));
                    radios[i].setVisible(true);
                } else {
                    radios[i].setVisible(false);
                }
            }
        }
        limpiar();
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Registro de clases", JOptionPane.ERROR_MESSAGE);
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Registro de clases", JOptionPane.INFORMATION_MESSAGE);
    }

    // "3303B2 Prog ll" -> {"3303B2", "Prog ll"}  (grupo, asignatura)
    private static String[] partirRadio(JRadioButton rb) {
        String t = rb.getText().trim();
        int i = t.indexOf(' ');
        return i < 0 ? new String[]{t, ""} : new String[]{t.substring(0, i), t.substring(i + 1).trim()};
    }

    private JRadioButton radioSeleccionado(JRadioButton[] radios) {
        for (JRadioButton rb : radios) {
            if (rb.isSelected()) {
                return rb;
            }
        }
        return null;
    }

    // Al elegir asignatura/grupo: sede y vocero salen de la configuracion
    private void seleccionarGrupo() {
        JRadioButton rb = radioSeleccionado(radiosGrupo());
        if (rb == null) {
            jTextField4.setText("");
            jTextField2.setText("");
            return;
        }
        String[] ga = partirRadio(rb);
        int idx = ConfiguracionDAO.indiceDe(config, ga[1], ga[0]);
        jTextField4.setText(idx >= 0 ? config.getSede(idx) : "");
        jTextField2.setText(idx >= 0 ? config.getVoceroApNombre(idx) : "");
        if (original == null) {
            try {
                jTextField1.setText(String.valueOf(control.siguienteEncuentro(ga[1], ga[0])));
            } catch (SQLException ex) {
                error("No se pudo consultar el siguiente encuentro:\n" + ex.getMessage());
            }
        }
    }

    private static String hoy() {
        LocalDate d = LocalDate.now();
        return String.format("%02d/%02d/%04d", d.getDayOfMonth(), d.getMonthValue(), d.getYear());
    }

    /** Deja el formulario como nuevo (o restaura el registro original si se esta modificando). */
    private void limpiar() {
        if (original != null) {
            cargarRegistro(original);
            return;
        }
        buttonGroup1.clearSelection();
        jTextField1.setText("0");
        jTextField3.setText(hoy());
        jTextField7.setText("0");
        jTextField4.setText("");
        jTextField2.setText("");
        jTextArea1.setText("");
        jTextArea2.setText("");
        jTextArea3.setText("");
        jTextArea4.setText("");
        jRadioButton6.setSelected(true);
        jRadioButton11.setSelected(true);
    }

    /** Abre la ventana en modo modificacion con los datos de un registro existente. */
    public void setRegistro(Registro r) {
        original = r;
        setTitle("Modificar registro");
        cargarRegistro(r);
    }

    private void cargarRegistro(Registro r) {
        JRadioButton elegido = null;
        for (JRadioButton rb : radiosGrupo()) {
            String[] ga = partirRadio(rb);
            if (ga[0].equalsIgnoreCase(r.getGrupo()) && ga[1].equalsIgnoreCase(r.getAsignatura())) {
                elegido = rb;
            }
        }
        if (elegido == null) {
            error("El grupo " + r.getGrupo() + " / " + r.getAsignatura() + " no esta disponible en esta ventana.");
            return;
        }
        elegido.setSelected(true);
        seleccionarGrupo();
        jTextField4.setText(r.getSede());
        jTextField1.setText(String.valueOf(r.getNroEncuentro()));
        jTextField3.setText(String.format("%02d/%02d/%04d", r.getDia(), r.getMes(), r.getAno()));
        jTextField7.setText(String.valueOf(r.getNroNoAsisten()));
        jTextArea1.setText(r.getTrabajoRealizado());
        jTextArea2.setText(r.getActividad());
        jTextArea3.setText(r.getNombreNosAsisten());
        jTextArea4.setText(r.getObservaciones());
        jRadioButton6.setSelected(true);
        for (JRadioButton rb : radiosPlataforma()) {
            if (rb.getText().trim().equalsIgnoreCase(r.getPlataVideo())) {
                rb.setSelected(true);
            }
        }
        if ("Recuperacion".equalsIgnoreCase(r.getTipoEncuentro())) {
            jRadioButton10.setSelected(true);
        } else {
            jRadioButton11.setSelected(true);
        }
        // La llave del registro (asignatura, grupo y encuentro) no se cambia al modificar
        for (JRadioButton rb : radiosGrupo()) {
            rb.setEnabled(false);
        }
        jTextField1.setEditable(false);
    }

    // Abre el calendario (parte de la fecha ya escrita, si es valida) y escribe la fecha elegida
    // con el mismo formato que usa la carga de registros: DD/MM/AAAA
    private void elegirFecha() {
        int[] f = leerFecha(jTextField3.getText());
        LocalDate inicial = (f == null) ? LocalDate.now() : LocalDate.of(f[2], f[1], f[0]);
        LocalDate elegida = SelectorFecha.elegir(this, inicial);
        if (elegida != null) {
            jTextField3.setText(String.format("%02d/%02d/%04d",
                    elegida.getDayOfMonth(), elegida.getMonthValue(), elegida.getYear()));
        }
    }

    // Convierte "dd/mm/aa" o "dd/mm/aaaa" a {dia, mes, ano}; null si no es valida
    private static int[] leerFecha(String texto) {
        try {
            String[] p = texto.trim().split("[/\\-]");
            if (p.length != 3) {
                return null;
            }
            int d = Integer.parseInt(p[0].trim());
            int m = Integer.parseInt(p[1].trim());
            int a = Integer.parseInt(p[2].trim());
            if (a < 100) {
                a += 2000;
            }
            LocalDate.of(a, m, d);
            return new int[]{d, m, a};
        } catch (RuntimeException e) {
            return null;
        }
    }

    // Valida los campos y arma el Registro; devuelve null (con mensaje) si hay errores
    private Registro construirRegistro() {
        JRadioButton rbGrupo = radioSeleccionado(radiosGrupo());
        if (rbGrupo == null) {
            error("Seleccione la asignatura y el grupo.");
            return null;
        }
        String[] ga = partirRadio(rbGrupo);
        int nro;
        try {
            nro = Integer.parseInt(jTextField1.getText().trim());
            if (nro < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            error("El numero de clase / encuentro debe ser un entero mayor o igual a 1.");
            jTextField1.requestFocus();
            return null;
        }
        int[] f = leerFecha(jTextField3.getText());
        if (f == null) {
            error("La fecha no es valida. Use el formato DD/MM/AA (por ejemplo 26/10/2026).");
            jTextField3.requestFocus();
            return null;
        }
        int noAsisten = 0;
        String txtNo = jTextField7.getText().trim();
        if (!txtNo.isEmpty()) {
            try {
                noAsisten = Integer.parseInt(txtNo);
                if (noAsisten < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                error("El numero de estudiantes que no asisten debe ser un entero mayor o igual a 0.");
                jTextField7.requestFocus();
                return null;
            }
        }
        if (jTextArea1.getText().trim().isEmpty()) {
            error("Escriba los temas de la clase dictada / realizada.");
            jTextArea1.requestFocus();
            return null;
        }
        JRadioButton rbPlat = radioSeleccionado(radiosPlataforma());
        String plataforma = rbPlat == null ? "N/A" : rbPlat.getText().trim();
        int idx = ConfiguracionDAO.indiceDe(config, ga[1], ga[0]);
        int matriculados = idx >= 0 ? config.getEstMatriculados(idx) : 0;
        if (matriculados > 0 && noAsisten > matriculados) {
            error("Los estudiantes que no asisten (" + noAsisten + ") superan los matriculados (" + matriculados + ").");
            jTextField7.requestFocus();
            return null;
        }
        Registro r = new Registro(
                ga[1],
                nro,
                ga[0],
                idx >= 0 ? config.getHora(idx) : 0,
                plataforma.equals("N/A") ? "presencial" : "remota",
                idx >= 0 ? config.getPlataformaLMS(idx) : "",
                jTextField4.getText().trim(),
                idx >= 0 ? config.getProgramaGrupo(idx) : jTextField6.getText().trim(),
                plataforma,
                matriculados,
                jTextArea2.getText().trim(),
                jTextArea1.getText().trim(),
                f[2], f[1], f[0],
                Math.max(0, matriculados - noAsisten),
                noAsisten,
                jTextArea3.getText().trim(),
                jTextArea4.getText().trim());
        r.setTipoEncuentro(jRadioButton10.isSelected() ? "Recuperacion" : "Hora estipulada");
        r.setDocenteId(config != null ? config.getIdentificacion() : 0);
        return r;
    }

    private void guardar() {
        Registro r = construirRegistro();
        if (r == null) {
            return;
        }
        try {
            if (original != null) {
                if (!control.modificarEnBD(r)) {
                    error("No se encontro el registro a modificar (pudo haber sido eliminado).");
                    return;
                }
                guardado = true;
                aviso("Registro modificado correctamente.");
                dispose();
            } else {
                if (!control.guardarEnBD(r)) {
                    error("Ya existe un registro de " + r.getAsignatura() + " " + r.getGrupo()
                            + " para el encuentro " + r.getNroEncuentro() + ".\n"
                            + "Cambie el numero de encuentro o modifique el registro existente.");
                    return;
                }
                guardado = true;
                aviso("Registro guardado correctamente.");
                limpiar();
            }
        } catch (SQLException ex) {
            error("No se pudo guardar en la base de datos:\n" + ex.getMessage());
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
        grupoPlataforma = new javax.swing.ButtonGroup();
        grupoTipo = new javax.swing.ButtonGroup();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 700, 20));

        jLabel5.setText("Asignatura:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        jTextField3.setText("DD/MM/AA");
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 110, -1));

        jLabel6.setText("Nombres");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, -1));

        jTextField4.setEditable(false);
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 180, -1));

        jLabel7.setText("ID Docente");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 20));

        jTextField5.setEditable(false);
        jTextField5.addActionListener(this::jTextField5ActionPerformed);
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 180, -1));

        jLabel8.setText("Programa");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 70, -1));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("3303B2 Prog ll");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, -1, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("3303B2 Seminario ");
        jRadioButton2.addActionListener(this::jRadioButton2ActionPerformed);
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, -1, -1));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("4303A Prog ll");
        jRadioButton3.addActionListener(this::jRadioButton3ActionPerformed);
        getContentPane().add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, -1, -1));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("4303B Prog ll");
        getContentPane().add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, -1, -1));

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("4303C Prog ll ");
        getContentPane().add(jRadioButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, -1, -1));

        jLabel9.setText("Fecha");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, -1, -1));

        jTextField6.setEditable(false);
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 200, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/calendarioc.png"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 90, 100));

        jLabel11.setText("Nombre, no asisten");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, -1, 20));

        jTextField7.addActionListener(this::jTextField7ActionPerformed);
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 70, -1));

        jLabel12.setText("Nro no asisten");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        jLabel13.setText("Temas clase dictada - realizada:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 360, 40));

        jLabel14.setText("Actividades:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 90, 20));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 360, 40));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 240, 70));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Registro de clases");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jLabel1.setText("Sede");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        jTextField8.setEditable(false);
        jTextField8.addActionListener(this::jTextField8ActionPerformed);
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 180, -1));

        jLabel16.setText("Apellidos");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jTextField9.setEditable(false);
        jTextField9.addActionListener(this::jTextField9ActionPerformed);
        getContentPane().add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 180, -1));

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));
        jPanel1.setForeground(new java.awt.Color(51, 51, 255));

        jTextField1.setText("0");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" clase / encuentro nro");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(499, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 50));

        jLabel2.setText("Modalidad plataforma ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 210, -1, -1));

        grupoPlataforma.add(jRadioButton6);
        jRadioButton6.setText("N/A");
        jRadioButton6.addActionListener(this::jRadioButton6ActionPerformed);
        getContentPane().add(jRadioButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 290, -1, -1));

        grupoPlataforma.add(jRadioButton7);
        jRadioButton7.setText("Meet");
        jRadioButton7.addActionListener(this::jRadioButton7ActionPerformed);
        getContentPane().add(jRadioButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 270, -1, -1));

        grupoPlataforma.add(jRadioButton8);
        jRadioButton8.setText("Teams ");
        jRadioButton8.addActionListener(this::jRadioButton8ActionPerformed);
        getContentPane().add(jRadioButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 230, -1, -1));

        grupoPlataforma.add(jRadioButton9);
        jRadioButton9.setText("Zoom");
        jRadioButton9.addActionListener(this::jRadioButton9ActionPerformed);
        getContentPane().add(jRadioButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 250, -1, -1));

        jTextField2.setEditable(false);
        jTextField2.addActionListener(this::jTextField2ActionPerformed);
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 300, -1));

        jLabel17.setText("Tipo de Encuentro:");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        grupoTipo.add(jRadioButton10);
        jRadioButton10.setText("Recuperacion");
        jRadioButton10.addActionListener(this::jRadioButton10ActionPerformed);
        getContentPane().add(jRadioButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, -1, -1));

        grupoTipo.add(jRadioButton11);
        jRadioButton11.setText("Hora estipulada");
        jRadioButton11.addActionListener(this::jRadioButton11ActionPerformed);
        getContentPane().add(jRadioButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        jLabel18.setText("Vocero:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/prof.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, 60, 80));
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 400, -1, -1));

        jLabel23.setText("");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 70, 10));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/Boton regresar.png"))); // NOI18N
        jButton1.addActionListener(this::jButton1ActionPerformed);
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 320, 40, 40));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/nguardar.png"))); // NOI18N
        jButton2.addActionListener(this::jButton2ActionPerformed);
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 440, 40, 40));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VISTA/boton de limpiar.png"))); // NOI18N
        jButton3.addActionListener(this::jButton3ActionPerformed);
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 380, 40, 40));

        jLabel20.setText("Observaciones:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, -1, -1));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, 200, 110));

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton9ActionPerformed

    private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton10ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton11ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        guardar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        limpiar();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(RegistroGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegistroGui dialog = new RegistroGui(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup grupoPlataforma;
    private javax.swing.ButtonGroup grupoTipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
