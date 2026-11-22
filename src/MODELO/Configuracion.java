package MODELO;

import java.io.Serializable;

 
public class Configuracion implements Serializable {
 private int identificacion;
 private String apellidos;
 private String nombres;
 private String programa;
 public String[] Asignaturas;
 private int nroAsignaturas;
 public String[] grupos;
 public String[] diaSemana;
 public int[] hora; //hora militar
 public String[] modalidad; // presencial/remota
 public String[] plataformaLMS; // zoom/teams 
 public String[] sede;
 public  String[] programaGrupo;
 public String[] plataVideo;
 public int[] estMatriculados;
 public int[] voceroId;
 public String[] voceroApNombre;
 public String[] voceroemail;
 public long[] voceroTel;

    public Configuracion(int identificacion,
                         String apellidos,
                         String nombres,
                         String programa,
                         int nroAsignaturas) {

        this.identificacion = identificacion;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.programa = programa;
        this.nroAsignaturas = nroAsignaturas;

        // Crear los arreglos
        Asignaturas = new String[nroAsignaturas];
        grupos = new String[nroAsignaturas];
        diaSemana = new String[nroAsignaturas];
        hora = new int[nroAsignaturas];
        modalidad = new String[nroAsignaturas];
        plataformaLMS = new String[nroAsignaturas];
        sede = new String[nroAsignaturas];
        programaGrupo = new String[nroAsignaturas];
        plataVideo = new String[nroAsignaturas];
        estMatriculados = new int[nroAsignaturas];
        voceroId = new int[nroAsignaturas];
        voceroApNombre = new String[nroAsignaturas];
        voceroemail = new String[nroAsignaturas];
        voceroTel = new long[nroAsignaturas];
    }


    // =====================================================
    // GET Y SET DE LOS ATRIBUTOS PRIVATE
    // =====================================================

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public int getNroAsignaturas() {
        return nroAsignaturas;
    }


    // =====================================================
    // ASIGNATURAS
    // =====================================================

    public void setAsignatura(String dato, int indice) {
        Asignaturas[indice] = dato;
    }

    public String getAsignatura(int indice) {
        return Asignaturas[indice];
    }


    // =====================================================
    // GRUPOS
    // =====================================================

    public void setGrupo(String dato, int indice) {
        grupos[indice] = dato;
    }

    public String getGrupo(int indice) {
        return grupos[indice];
    }


    // =====================================================
    // DIA DE LA SEMANA
    // =====================================================

    public void setDiaSemana(String dato, int indice) {
        diaSemana[indice] = dato;
    }

    public String getDiaSemana(int indice) {
        return diaSemana[indice];
    }


    // =====================================================
    // HORA
    // =====================================================

    public void setHora(int dato, int indice) {
        hora[indice] = dato;
    }

    public int getHora(int indice) {
        return hora[indice];
    }


    // =====================================================
    // MODALIDAD
    // =====================================================

    public void setModalidad(String dato, int indice) {
        modalidad[indice] = dato;
    }

    public String getModalidad(int indice) {
        return modalidad[indice];
    }


    // =====================================================
    // PLATAFORMA LMS
    // =====================================================

    public void setPlataformaLMS(String dato, int indice) {
        plataformaLMS[indice] = dato;
    }

    public String getPlataformaLMS(int indice) {
        return plataformaLMS[indice];
    }


    // =====================================================
    // SEDE
    // =====================================================

    public void setSede(String dato, int indice) {
        sede[indice] = dato;
    }

    public String getSede(int indice) {
        return sede[indice];
    }


    // =====================================================
    // PROGRAMA DEL GRUPO
    // =====================================================

    public void setProgramaGrupo(String dato, int indice) {
        programaGrupo[indice] = dato;
    }

    public String getProgramaGrupo(int indice) {
        return programaGrupo[indice];
    }


    // =====================================================
    // PLATAFORMA DE VIDEO
    // =====================================================

    public void setPlataVideo(String dato, int indice) {
        plataVideo[indice] = dato;
    }

    public String getPlataVideo(int indice) {
        return plataVideo[indice];
    }


    // =====================================================
    // ESTUDIANTES MATRICULADOS
    // =====================================================

    public void setEstMatriculados(int dato, int indice) {
        estMatriculados[indice] = dato;
    }

    public int getEstMatriculados(int indice) {
        return estMatriculados[indice];
    }


    // =====================================================
    // IDENTIFICACION DEL VOCERO
    // =====================================================

    public void setVoceroId(int dato, int indice) {
        voceroId[indice] = dato;
    }

    public int getVoceroId(int indice) {
        return voceroId[indice];
    }


    // =====================================================
    // APELLIDOS Y NOMBRE DEL VOCERO
    // =====================================================

    public void setVoceroApNombre(String dato, int indice) {
        voceroApNombre[indice] = dato;
    }

    public String getVoceroApNombre(int indice) {
        return voceroApNombre[indice];
    }


    // =====================================================
    // EMAIL DEL VOCERO
    // =====================================================

    public void setVoceroemail(String dato, int indice) {
        voceroemail[indice] = dato;
    }

    public String getVoceroemail(int indice) {
        return voceroemail[indice];
    }


    // =====================================================
    // TELEFONO DEL VOCERO
    // =====================================================

    public void setVoceroTel(long dato, int indice) {
        voceroTel[indice] = dato;
    }

    public long getVoceroTel(int indice) {
        return voceroTel[indice];
    }


    // =====================================================
    // TOSTRING
    // =====================================================

    @Override
    public String toString() {

        String resultado = "";

        resultado += "IDENTIFICACION: " + identificacion + "\n";
        resultado += "APELLIDOS: " + apellidos + "\n";
        resultado += "NOMBRES: " + nombres + "\n";
        resultado += "PROGRAMA: " + programa + "\n";
        resultado += "NRO. ASIGNATURAS: " + nroAsignaturas + "\n";

        resultado += "\n========================================\n";
        resultado += "DATOS DE LAS ASIGNATURAS\n";
        resultado += "========================================\n";

        for (int i = 0; i < nroAsignaturas; i++) {

            resultado += "\nASIGNATURA " + (i + 1) + "\n";
            resultado += "----------------------------------------\n";

            resultado += "Asignatura: " + Asignaturas[i] + "\n";
            resultado += "Grupo: " + grupos[i] + "\n";
            resultado += "Dia: " + diaSemana[i] + "\n";
            resultado += "Hora: " + hora[i] + "\n";
            resultado += "Modalidad: " + modalidad[i] + "\n";
            resultado += "Plataforma LMS: " + plataformaLMS[i] + "\n";
            resultado += "Sede: " + sede[i] + "\n";
            resultado += "Programa grupo: " + programaGrupo[i] + "\n";
            resultado += "Plataforma video: " + plataVideo[i] + "\n";
            resultado += "Estudiantes matriculados: "
                    + estMatriculados[i] + "\n";

            resultado += "Vocero ID: " + voceroId[i] + "\n";
            resultado += "Vocero: " + voceroApNombre[i] + "\n";
            resultado += "Email vocero: " + voceroemail[i] + "\n";
            resultado += "Telefono vocero: " + voceroTel[i] + "\n";
        }

        return resultado;
    }
    public static void main(String[] args) {

        // =====================================================
        // CREAR CONFIGURACION
        // =====================================================

        Configuracion conf = new Configuracion(
                12345678,
                "Diaz",
                "William",
                "Ingenieria de Sistemas",
                5
        );


        // =====================================================
        // ASIGNATURA 0
        // PROGRAMACION ORIENTADA A OBJETOS - 3303B1
        // =====================================================

        conf.setAsignatura(
                "Programacion Orientada a Objetos", 0);

        conf.setGrupo("3303B1", 0);

        conf.setDiaSemana("Lunes", 0);

        conf.setHora(1800, 0);

        conf.setModalidad("presencial", 0);

        conf.setPlataformaLMS("Teams", 0);

        conf.setSede("Norte", 0);

        conf.setProgramaGrupo(
                "Ingenieria de Sistemas", 0);

        conf.setPlataVideo("Teams", 0);

        conf.setEstMatriculados(20, 0);

        conf.setVoceroId(1001001, 0);

        conf.setVoceroApNombre(
                "Laura Martinez", 0);

        conf.setVoceroemail(
                "laura.martinez@universidad.edu.co", 0);

        conf.setVoceroTel(
                3001234567L, 0);


        // =====================================================
        // ASIGNATURA 1
        // ESTRUCTURAS DE DATOS - 3303B2
        // =====================================================

        conf.setAsignatura(
                "Estructuras de Datos", 1);

        conf.setGrupo("3303B2", 1);

        conf.setDiaSemana("Martes", 1);

        conf.setHora(1400, 1);

        conf.setModalidad("presencial", 1);

        conf.setPlataformaLMS("Teams", 1);

        conf.setSede("Sur", 1);

        conf.setProgramaGrupo(
                "Ingenieria de Sistemas", 1);

        conf.setPlataVideo("Teams", 1);

        conf.setEstMatriculados(22, 1);

        conf.setVoceroId(1001002, 1);

        conf.setVoceroApNombre(
                "Sebastian Gomez", 1);

        conf.setVoceroemail(
                "sebastian.gomez@universidad.edu.co", 1);

        conf.setVoceroTel(
                3012345678L, 1);


        // =====================================================
        // ASIGNATURA 2
        // CIRCUITOS DIGITALES - 4303A2
        // =====================================================

        conf.setAsignatura(
                "Circuitos Digitales", 2);

        conf.setGrupo("4303A2", 2);

        conf.setDiaSemana("Miercoles", 2);

        conf.setHora(800, 2);

        conf.setModalidad("presencial", 2);

        conf.setPlataformaLMS("Teams", 2);

        conf.setSede("Norte", 2);

        conf.setProgramaGrupo(
                "Ingenieria Electronica", 2);

        conf.setPlataVideo("Teams", 2);

        conf.setEstMatriculados(18, 2);

        conf.setVoceroId(1001003, 2);

        conf.setVoceroApNombre(
                "Juan David Ruiz", 2);

        conf.setVoceroemail(
                "juan.ruiz@universidad.edu.co", 2);

        conf.setVoceroTel(
                3023456789L, 2);


        // =====================================================
        // ASIGNATURA 3
        // INVESTIGACION DE OPERACIONES - 4303B2
        // =====================================================

        conf.setAsignatura(
                "Investigacion de Operaciones", 3);

        conf.setGrupo("4303B2", 3);

        conf.setDiaSemana("Jueves", 3);

        conf.setHora(1000, 3);

        conf.setModalidad("remota", 3);

        conf.setPlataformaLMS("Zoom", 3);

        conf.setSede("Sur", 3);

        conf.setProgramaGrupo(
                "Ingenieria Industrial", 3);

        conf.setPlataVideo("Zoom", 3);

        conf.setEstMatriculados(17, 3);

        conf.setVoceroId(1001004, 3);

        conf.setVoceroApNombre(
                "Paula Herrera", 3);

        conf.setVoceroemail(
                "paula.herrera@universidad.edu.co", 3);

        conf.setVoceroTel(
                3034567890L, 3);


        // =====================================================
        // ASIGNATURA 4
        // BASES DE DATOS - 4303C2
        // =====================================================

        conf.setAsignatura(
                "Bases de Datos", 4);

        conf.setGrupo("4303C2", 4);

        conf.setDiaSemana("Viernes", 4);

        conf.setHora(1600, 4);

        conf.setModalidad("presencial", 4);

        conf.setPlataformaLMS("Teams", 4);

        conf.setSede("Norte", 4);

        conf.setProgramaGrupo(
                "Ingenieria de Sistemas", 4);

        conf.setPlataVideo("Teams", 4);

        conf.setEstMatriculados(19, 4);

        conf.setVoceroId(1001005, 4);

        conf.setVoceroApNombre(
                "Valentina Sanchez", 4);

        conf.setVoceroemail(
                "valentina.sanchez@universidad.edu.co", 4);

        conf.setVoceroTel(
                3045678901L, 4);


        // =====================================================
        // MOSTRAR TODA LA CONFIGURACION
        // =====================================================

        System.out.println(conf);
    }
}

 


