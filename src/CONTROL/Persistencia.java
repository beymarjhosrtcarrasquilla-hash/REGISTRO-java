/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;



import java.io.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Persistencia {
 public String carpeta;
 public String nombreArchivo;
 FileOutputStream basesalida;
 ObjectOutputStream salida;
 FileInputStream baseentrada;
 ObjectInputStream entrada;
 public Persistencia() {
        this.carpeta = "datos/";
        this.nombreArchivo = "secretKey.dat";
 }
 public Persistencia(String carpeta, String nombreArchivo) {
        this.carpeta = carpeta;
        this.nombreArchivo = nombreArchivo;
 }
 public boolean existeArchivo() {

    String archivo = carpeta + nombreArchivo;

    File f = new File(archivo);

    return f.exists();
}
 
 public void abrirArchivoSalida(){
    String archivo = carpeta+nombreArchivo;
    try{
     File c = new File(carpeta);
     if (!c.exists()) c.mkdirs();
     basesalida = new FileOutputStream(archivo);
     salida = new ObjectOutputStream(basesalida);
    
    
    }catch(Exception e){
        System.out.println("ERROR "+e); 
    } 
 }
 public void cerrarArchivoSalida(){
 
  try{
     salida.close();
     basesalida.close();

    }catch(Exception e){
        System.out.println("ERROR "+e); 
    }
 }
 public void abrirArchivoEntrada(){
    String archivo = carpeta+nombreArchivo;
    try{
     baseentrada = new FileInputStream(archivo);
     entrada = new ObjectInputStream(baseentrada);
    
    
    }catch(Exception e){
        System.out.println("ERROR "+e); 
    } 
 }
 public void cerrarArchivoEntrada(){
 
  try{
     entrada.close();
     baseentrada.close();

    }catch(Exception e){
        System.out.println("ERROR "+e); 
    }
 }
 public void guardar(Object x){   
    try{
    salida.writeObject(x); 
    }catch(Exception e){
        System.out.println("ERROR "+e); 
    }       
}  
public Object cargar(){
  Object resultado=null;
  try{
    resultado =  entrada.readObject();
    
    }catch(Exception e){
        System.out.println("ERROR "+e); 
    }
  return resultado;
}

}
