/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODELO;

import java.io.Serializable;

 
public class Registro implements Serializable{
  private String asignatura;
  private int nroEncuentro;
  private String grupo;
private int  hora; //hora militar
private String  modalidad; // presencial/remota
private String  plataformaLMS; // zoom/teams 
private String  sede;
private  String  programaGrupo;
private String  plataVideo;
private int  estMatriculados;
private String actividad;
private String trabajoRealizado;
private int ano;
private int mes;
private int dia;
private int nroAsistentes;
private int nroNoAsisten;
private String nombreNosAsisten;
private String observaciones;
  // Datos adicionales usados por la interfaz (no forman parte del constructor del profesor)
  private String tipoEncuentro = "Hora estipulada"; // Hora estipulada / Recuperacion
  private int docenteId;
  private String usuario = ""; // cuenta (tabla seguridad) duena del registro

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Registro(String asignatura,
                    int nroEncuentro,
                    String grupo,
                   
                    int hora,
                    String modalidad,
                    String plataformaLMS,
                    String sede,
                    String programaGrupo,
                    String plataVideo,
                    int estMatriculados,
                    String actividad,
                    String trabajoRealizado,
                    int ano,
                    int mes,
                    int dia,
                    int nroAsistentes,
                    int nroNoAsisten,
                    String nombreNosAsisten,
                    String observaciones) {

        this.asignatura = asignatura;
        this.nroEncuentro = nroEncuentro;
        this.grupo = grupo;
        
        this.hora = hora;
        this.modalidad = modalidad;
        this.plataformaLMS = plataformaLMS;
        this.sede = sede;
        this.programaGrupo = programaGrupo;
        this.plataVideo = plataVideo;
        this.estMatriculados = estMatriculados;
        this.actividad = actividad;
        this.trabajoRealizado = trabajoRealizado;
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
        this.nroAsistentes = nroAsistentes;
        this.nroNoAsisten = nroNoAsisten;
        this.nombreNosAsisten = nombreNosAsisten;
        this.observaciones = observaciones;
    }


    // =====================================================
    // GET Y SET
    // =====================================================

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getNroEncuentro() {
        return nroEncuentro;
    }

    public void setNroEncuentro(int nroEncuentro) {
        this.nroEncuentro = nroEncuentro;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

  


    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getPlataformaLMS() {
        return plataformaLMS;
    }

    public void setPlataformaLMS(String plataformaLMS) {
        this.plataformaLMS = plataformaLMS;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getProgramaGrupo() {
        return programaGrupo;
    }

    public void setProgramaGrupo(String programaGrupo) {
        this.programaGrupo = programaGrupo;
    }

    public String getPlataVideo() {
        return plataVideo;
    }

    public void setPlataVideo(String plataVideo) {
        this.plataVideo = plataVideo;
    }

    public int getEstMatriculados() {
        return estMatriculados;
    }

    public void setEstMatriculados(int estMatriculados) {
        this.estMatriculados = estMatriculados;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getTrabajoRealizado() {
        return trabajoRealizado;
    }

    public void setTrabajoRealizado(String trabajoRealizado) {
        this.trabajoRealizado = trabajoRealizado;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getNroAsistentes() {
        return nroAsistentes;
    }

    public void setNroAsistentes(int nroAsistentes) {
        this.nroAsistentes = nroAsistentes;
    }

    public int getNroNoAsisten() {
        return nroNoAsisten;
    }

    public void setNroNoAsisten(int nroNoAsisten) {
        this.nroNoAsisten = nroNoAsisten;
    }

    public String getNombreNosAsisten() {
        return nombreNosAsisten;
    }

    public void setNombreNosAsisten(String nombreNosAsisten) {
        this.nombreNosAsisten = nombreNosAsisten;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public String getTipoEncuentro() {
        return tipoEncuentro;
    }

    public void setTipoEncuentro(String tipoEncuentro) {
        this.tipoEncuentro = tipoEncuentro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(int docenteId) {
        this.docenteId = docenteId;
    }


    // =====================================================
    // TOSTRING
    // =====================================================

    @Override
    public String toString() {

        String r = "";

        r += "Asignatura: " + asignatura + "\n";
        r += "Nro. Encuentro: " + nroEncuentro + "\n";
        r += "Grupo: " + grupo + "\n";
       
        r += "Hora: " + hora + "\n";
        r += "Modalidad: " + modalidad + "\n";
        r += "Plataforma LMS: " + plataformaLMS + "\n";
        r += "Sede: " + sede + "\n";
        r += "Programa Grupo: " + programaGrupo + "\n";
        r += "Plataforma Video: " + plataVideo + "\n";
        r += "Estudiantes Matriculados: " + estMatriculados + "\n";
        r += "Actividad: " + actividad + "\n";
        r += "Trabajo Realizado: " + trabajoRealizado + "\n";
        r += "Fecha: " + dia + "/" + mes + "/" + ano + "\n";
        r += "Nro. Asistentes: " + nroAsistentes + "\n";
        r += "Nro. No Asisten: " + nroNoAsisten + "\n";
        r += "Nombres No Asisten: " + nombreNosAsisten + "\n";
        r += "Observaciones: " + observaciones + "\n";
        r += "Tipo de encuentro: " + tipoEncuentro + "\n";

        return r;
    }
}
