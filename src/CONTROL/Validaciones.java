/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROL;

import java.util.Scanner;
import java.util.regex.Pattern;
public class Validaciones {
     private static final Scanner SC = new Scanner(System.in);
    public static boolean estaVacio(String x){
        boolean r=false;
        if(x.length()==0)
            r=true;
        if(x==null)
            r=true;
      if(x.compareTo(" ")==0)
          r=true;
     return r;   
    }

    // Patrones precompilados (más eficientes que compilar en cada llamada)
    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");
    private static final Pattern PATRON_SOLO_LETRAS =
            Pattern.compile("^[\\p{L} ]+$"); // incluye tildes y ñ
    private static final Pattern PATRON_SOLO_DIGITOS =
            Pattern.compile("^\\d+$");
    private static final Pattern PATRON_ALFANUMERICO =
            Pattern.compile("^[\\p{L}\\d ]+$");
    private static final Pattern PATRON_ISBN =
            Pattern.compile("^(97[89])?-?\\d{1,5}-?\\d{1,7}-?\\d{1,7}-?[\\dXx]$");

    // Evita instanciar la clase: todo es estático
    private Validaciones() { }

    // =====================================================
    //  1. VALIDACIONES BÁSICAS DE CADENA
    // =====================================================

    /** true si la cadena es null, vacía o solo tiene espacios. */
    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /** true si la cadena tiene contenido real. */
    public static boolean noEsVacio(String texto) {
        return !esVacio(texto);
    }

    /** true si la longitud (sin espacios en los extremos) está en el rango [min, max]. */
    public static boolean longitudEnRango(String texto, int min, int max) {
        if (esVacio(texto)) return false;
        int len = texto.trim().length();
        return len >= min && len <= max;
    }

    /** true si solo contiene letras (con tildes, ñ) y espacios. Ej: "José Peña" */
    public static boolean esSoloLetras(String texto) {
        return noEsVacio(texto) && PATRON_SOLO_LETRAS.matcher(texto.trim()).matches();
    }

    /** true si solo contiene dígitos. Ej: "12345" (útil para cédulas, teléfonos). */
    public static boolean esSoloDigitos(String texto) {
        return noEsVacio(texto) && PATRON_SOLO_DIGITOS.matcher(texto.trim()).matches();
    }

    /** true si solo contiene letras, números y espacios (sin símbolos raros). */
    public static boolean esAlfanumerico(String texto) {
        return noEsVacio(texto) && PATRON_ALFANUMERICO.matcher(texto.trim()).matches();
    }

    // =====================================================
    //  2. VALIDACIONES NUMÉRICAS (devuelven boolean)
    // =====================================================

    /** true si el texto es un entero válido. Detecta "abc", "12.5", "", "12a", etc. */
    public static boolean esEntero(String texto) {
        if (esVacio(texto)) return false;
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** true si el texto es un long válido. Detecta "abc", "12.5", "", "12a", etc. */
    public static boolean esLong(String texto) {
        if (esVacio(texto)) return false;
        try {
            Long.parseLong(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** true si el texto es un double válido. Acepta "12.5" y también "12,5" (coma decimal). */
    public static boolean esDouble(String texto) {
        if (esVacio(texto)) return false;
        try {
            double d = Double.parseDouble(texto.trim().replace(',', '.'));
            // Rechaza "NaN" e "Infinity", que Double.parseDouble sí acepta
            return !Double.isNaN(d) && !Double.isInfinite(d);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** true si el entero es positivo (> 0). */
    public static boolean esEnteroPositivo(String texto) {
        return esEntero(texto) && Integer.parseInt(texto.trim()) > 0;
    }

    /** true si el entero es >= 0. */
    public static boolean esEnteroNoNegativo(String texto) {
        return esEntero(texto) && Integer.parseInt(texto.trim()) >= 0;
    }

    /** true si el entero está en el rango [min, max]. */
    public static boolean esEnteroEnRango(String texto, int min, int max) {
        if (!esEntero(texto)) return false;
        int n = Integer.parseInt(texto.trim());
        return n >= min && n <= max;
    }

    /** true si el double está en el rango [min, max]. */
    public static boolean esDoubleEnRango(String texto, double min, double max) {
        if (!esDouble(texto)) return false;
        double d = Double.parseDouble(texto.trim().replace(',', '.'));
        return d >= min && d <= max;
    }

    // =====================================================
    //  3. VALIDACIONES DE FORMATO ESPECÍFICO
    // =====================================================

    /** Valida un correo electrónico con formato básico. */
    public static boolean esEmail(String texto) {
        return noEsVacio(texto) && PATRON_EMAIL.matcher(texto.trim()).matches();
    }

    /** Valida un año de publicación razonable (por defecto 1450 hasta el año actual + 1). */
    public static boolean esAnioValido(String texto) {
        int anioActual = java.time.Year.now().getValue();
        return esEnteroEnRango(texto, 1450, anioActual + 1);
    }

    /** Valida un ISBN de forma flexible (con o sin guiones, 10 o 13 dígitos). */
    public static boolean esIsbn(String texto) {
        if (esVacio(texto)) return false;
        if (!PATRON_ISBN.matcher(texto.trim()).matches()) return false;
        int digitos = texto.replaceAll("[^\\dXx]", "").length();
        return digitos == 10 || digitos == 13;
    }

    /** Valida un teléfono: solo dígitos, longitud entre 7 y 15. */
    public static boolean esTelefono(String texto) {
        return esSoloDigitos(texto) && longitudEnRango(texto, 7, 15);
    }

    // =====================================================
    //  4. LECTURA CON REINTENTO (piden hasta que sea válido)
    // =====================================================

    /** Pide un entero por consola hasta que el usuario escriba uno válido. */
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SC.nextLine();

            if (esVacio(entrada)) {
                System.out.println("  ❌ El campo no puede estar vacío.");
            } else if (!esEntero(entrada)) {
                System.out.println("  ❌ '" + entrada.trim() + "' no es un número entero válido.");
            } else {
                return Integer.parseInt(entrada.trim());
            }
        }
    }

    /** Pide un entero dentro de un rango [min, max]. */
    public static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int n = leerEntero(mensaje);
            if (n >= min && n <= max) return n;
            System.out.println("  ❌ Debe estar entre " + min + " y " + max + ".");
        }
    }

    /** Pide un entero positivo (> 0). */
    public static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int n = leerEntero(mensaje);
            if (n > 0) return n;
            System.out.println("  ❌ Debe ser mayor que cero.");
        }
    }

    /** Pide un número real (double). Acepta punto o coma como separador decimal. */
    public static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SC.nextLine();

            if (esVacio(entrada)) {
                System.out.println("  ❌ El campo no puede estar vacío.");
            } else if (!esDouble(entrada)) {
                System.out.println("  ❌ '" + entrada.trim() + "' no es un número real válido.");
            } else {
                return Double.parseDouble(entrada.trim().replace(',', '.'));
            }
        }
    }

    /** Pide un double dentro de un rango [min, max]. */
    public static double leerDoubleEnRango(String mensaje, double min, double max) {
        while (true) {
            double d = leerDouble(mensaje);
            if (d >= min && d <= max) return d;
            System.out.println("  ❌ Debe estar entre " + min + " y " + max + ".");
        }
    }

    /** Pide una cadena no vacía. */
    public static String leerCadena(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SC.nextLine();
            if (noEsVacio(entrada)) return entrada.trim();
            System.out.println("  ❌ El campo no puede estar vacío.");
        }
    }

    /** Pide una cadena con longitud entre min y max. */
    public static String leerCadenaConLongitud(String mensaje, int min, int max) {
        while (true) {
            String entrada = leerCadena(mensaje);
            if (longitudEnRango(entrada, min, max)) return entrada;
            System.out.println("  ❌ Debe tener entre " + min + " y " + max + " caracteres.");
        }
    }

    /** Pide una cadena que contenga solo letras (ideal para nombres). */
    public static String leerSoloLetras(String mensaje) {
        while (true) {
            String entrada = leerCadena(mensaje);
            if (esSoloLetras(entrada)) return entrada;
            System.out.println("  ❌ Solo se permiten letras y espacios (sin números ni símbolos).");
        }
    }

    /** Pide un correo electrónico válido. */
    public static String leerEmail(String mensaje) {
        while (true) {
            String entrada = leerCadena(mensaje);
            if (esEmail(entrada)) return entrada;
            System.out.println("  ❌ Formato de correo inválido (ej: usuario@dominio.com).");
        }
    }

    /** Pide un ISBN válido. */
    public static String leerIsbn(String mensaje) {
        while (true) {
            String entrada = leerCadena(mensaje);
            if (esIsbn(entrada)) return entrada;
            System.out.println("  ❌ ISBN inválido (debe tener 10 o 13 dígitos, ej: 978-0132350884).");
        }
    }

    /** Pide una respuesta Sí/No. Devuelve true para S/SI/Y/YES. */
    public static boolean leerSiNo(String mensaje) {
        while (true) {
            String r = leerCadena(mensaje + " (s/n): ").toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) return true;
            if (r.equals("n") || r.equals("no")) return false;
            System.out.println("  ❌ Responde con 's' o 'n'.");
        }
    }
public static void main(String[] args) {

// ---- Pruebas de los métodos booleanos ----
System.out.println("=== PRUEBAS esXxx ===");
System.out.println("esVacio(\"   \")          -> " + Validaciones.esVacio("       "));
System.out.println("esVacio(null)           -> " + Validaciones.esVacio(null));
System.out.println("esEntero(\"123\")         -> " + Validaciones.esEntero("123"));
System.out.println("esEntero(\"12a\")         -> " + Validaciones.esEntero("12a"));
System.out.println("esEntero(\"12.5\")        -> " + Validaciones.esEntero("12.5"));
System.out.println("esDouble(\"12,5\")        -> " + Validaciones.esDouble("12,5"));
System.out.println("esDouble(\"abc\")         -> " + Validaciones.esDouble("abc"));
System.out.println("esDouble(\"NaN\")         -> " + Validaciones.esDouble("NaN"));
System.out.println("esSoloLetras(\"José\")    -> " + Validaciones.esSoloLetras("José"));
System.out.println("esSoloLetras(\"Jo5é\")    -> " + Validaciones.esSoloLetras("Jo5é"));
System.out.println("esEmail(\"a@b.com\")      -> " + Validaciones.esEmail("a@b.com"));
System.out.println("esEmail(\"a@b\")          -> " + Validaciones.esEmail("a@b"));
System.out.println("esIsbn(\"978-0132350884\")-> " + Validaciones.esIsbn("978-0132350884"));
System.out.println("esAnioValido(\"2023\")    -> " + Validaciones.esAnioValido("2023"));

// ---- Pruebas interactivas (con tu clase Libro) ----
System.out.println("\n=== REGISTRO DE LIBRO ===");
String isbn      = Validaciones.leerIsbn("ISBN: ");
String titulo    = Validaciones.leerCadenaConLongitud("Título (3-100 caracteres): ", 3, 100);
String autor     = Validaciones.leerSoloLetras("Autor: ");
int anio         = Validaciones.leerEnteroEnRango("Año de publicación: ", 1450, 2026);
int paginas      = Validaciones.leerEnteroPositivo("Número de páginas: ");
int ejemplares   = Validaciones.leerEnteroEnRango("Cantidad de ejemplares: ", 0, 1000);

System.out.println("\n✅ Libro registrado: " + titulo + " (" + autor + ", " + anio + ")");
}

}
