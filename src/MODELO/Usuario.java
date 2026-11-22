/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODELO;

import CONTROL.*;
import static CONTROL.SEGURIDAD.generarLlave;
import java.io.Serializable;
import javax.crypto.SecretKey;

/**
 *
 * @author Usuario
 */
public class Usuario implements Serializable{
  private String codigo;
  private String password;
  public  String[] claves;
  
   public Usuario() {
        this.codigo = "";
        this.password = "";
        claves = new String[4];
    }
    public Usuario(String codigo, String password) {
        this.codigo = codigo;
        this.password = password;
        claves = new String[4];
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario{" + "codigo=" + codigo +
                ", password=" + password + '}';
    }
    public String[] getClaves(){
        return claves;
    }
    public void setClaves(String cla[]){
         claves=cla;
    }
    public static void main(String[] args) {
        String pcn="secreto";
        Usuario us=null;
        
        // CARGA DE LA PERSISTENCIA
         SecretKey secretKey = generarLlave();
           Persistencia per = new Persistencia();
         try{
        String pcenc=SEGURIDAD.encriptar(pcn, secretKey);
          us= new Usuario("wdiaz", pcenc);
      
      
        
        per.abrirArchivoSalida();
        per.guardar(us);
        per.cerrarArchivoSalida();
         }catch(Exception e){
             System.out.println(e);
         }
         us=null;
         per.abrirArchivoEntrada();
         us= (Usuario)per.cargar();
         per.cerrarArchivoEntrada();
         String usd=null;
         try{
            usd=SEGURIDAD.desencriptar(us.getPassword(), secretKey);
          }catch(Exception e){
              
          }
         System.out.println(usd);
    }
  
}
