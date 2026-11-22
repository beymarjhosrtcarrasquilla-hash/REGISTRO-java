/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SEGURIDAD {

    private static final int TAG_LENGTH_BIT = 128;

    // Generar una llave AES de 256 bits
    public static SecretKey generarLlave() {
        SecretKey r=null;
    try{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        r=keyGen.generateKey();
    }catch(Exception e){
        
    }

        return r;
    }

    // Encriptar
    public static String encriptar(String texto, SecretKey secretKey)
            throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // El Cipher genera automáticamente un IV seguro
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();

        byte[] textoCifrado = cipher.doFinal(
                texto.getBytes(StandardCharsets.UTF_8)
        );

        // Guardamos IV + texto cifrado
        byte[] resultado = new byte[iv.length + textoCifrado.length];

        System.arraycopy(iv, 0, resultado, 0, iv.length);

        System.arraycopy(
                textoCifrado,
                0,
                resultado,
                iv.length,
                textoCifrado.length
        );

        // Convertimos todo a Base64
        return Base64.getEncoder().encodeToString(resultado);
    }

    // Desencriptar
    public static String desencriptar(
            String textoCifradoBase64,
            SecretKey secretKey) throws Exception {

        // Recuperar IV + texto cifrado
        byte[] datos = Base64.getDecoder().decode(textoCifradoBase64);

        // En GCM normalmente el IV es de 12 bytes
        int longitudIV = 12;

        byte[] iv = new byte[longitudIV];

        System.arraycopy(
                datos,
                0,
                iv,
                0,
                longitudIV
        );

        // Obtener solamente el texto cifrado
        byte[] textoCifrado = new byte[datos.length - longitudIV];

        System.arraycopy(
                datos,
                longitudIV,
                textoCifrado,
                0,
                textoCifrado.length
        );

        // Configurar GCM con el mismo IV
        GCMParameterSpec spec =
                new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                spec
        );

        byte[] textoOriginal = cipher.doFinal(textoCifrado);

        return new String(
                textoOriginal,
                StandardCharsets.UTF_8
        );
    }

    // Programa de prueba
    public static void main(String[] args) throws Exception {

        String palabraClave ="secreto";
        Persistencia per = new Persistencia();
        // Generar la llave
        SecretKey secretKey = generarLlave();
        per.abrirArchivoSalida();
        per.guardar(secretKey);
        per.cerrarArchivoSalida();
        // Encriptar
        String textoCifrado =
                encriptar(palabraClave, secretKey);

        System.out.println("Texto original:");
        System.out.println(palabraClave);

        System.out.println("\nTexto cifrado:");
        System.out.println(textoCifrado);
        per.abrirArchivoEntrada();
        secretKey =(SecretKey )per.cargar();
        per.cerrarArchivoEntrada();
        // Desencriptar utilizando LA MISMA llave
        String textoDesencriptado =
                desencriptar(textoCifrado, secretKey);

        System.out.println("\nTexto desencriptado:");
        System.out.println(textoDesencriptado);
    }
}