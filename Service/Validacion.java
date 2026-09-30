package ni.edu.uam.facturacionapp.Service;

import java.math.BigDecimal;
public final class Validacion {
 private Validacion() {}
 public static String texto(String valor, String campo, int max, boolean requerido) {
  String limpio=valor==null?"":valor.trim();
  if(requerido && limpio.isEmpty()) throw new IllegalArgumentException(campo+" es obligatorio.");
  if(limpio.length()>max) throw new IllegalArgumentException(campo+": máximo "+max+" caracteres.");
  return limpio;
 }
 public static BigDecimal precio(String texto) {
  try {
   BigDecimal n=new BigDecimal(texto.trim().replace(',', '.'));
   if(n.signum()<=0 || n.compareTo(new BigDecimal("9999999999.99"))>0 || n.stripTrailingZeros().scale()>2) throw new NumberFormatException();
   return n.setScale(2);
  } catch(RuntimeException ex) { throw new IllegalArgumentException("Precio: escribe un número mayor que cero con hasta 2 decimales."); }
 }
 public static int existencia(String texto) {
  try { int n=Integer.parseInt(texto.trim()); if(n<0) throw new NumberFormatException(); return n; }
  catch(RuntimeException ex) { throw new IllegalArgumentException("Existencia: escribe un entero no negativo."); }
 }
}
