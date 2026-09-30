package ni.edu.uam.facturacionapp.Controller;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
public final class Ui {
 private Ui() {}
 public static <T> void ejecutar(Pane root, Callable<T> trabajo, Consumer<T> listo, Consumer<Throwable> error) {
  root.setDisable(true);
  Task<T> task=new Task<>() { @Override protected T call() throws Exception { return trabajo.call(); } };
  task.setOnSucceeded(e->{ root.setDisable(false); listo.accept(task.getValue()); });
  task.setOnFailed(e->{ root.setDisable(false); error.accept(task.getException()); });
  Thread hilo=new Thread(task,"consulta-catalogos"); hilo.setDaemon(true); hilo.start();
 }
 public static String mensaje(Throwable e) {
  if(e instanceof SQLException sql) {
   String estado=sql.getSQLState();
   if("23505".equals(estado)) return "El código o documento ya está registrado. Utiliza otro valor.";
   if("23503".equals(estado)) return "La categoría tiene productos asociados o ya no existe. Revisa los productos y actualiza las tablas.";
   if(estado!=null && estado.startsWith("08")) return "No se pudo conectar con PostgreSQL. Revisa el servidor y DB_URL, DB_USER y DB_PASSWORD.";
   if("28P01".equals(estado)) return "Usuario o contraseña de PostgreSQL incorrectos.";
   if("42P01".equals(estado)) return "Faltan tablas. Ejecuta database/02-tablas.sql en tienda_javafx.";
   return "No se completó la operación en PostgreSQL (estado "+estado+"). Revisa la configuración y las tablas.";
  }
  return e.getMessage()==null?"No se pudo completar la operación.":e.getMessage();
 }
 public static void error(Throwable e) {
  Alert a=new Alert(Alert.AlertType.ERROR);a.setTitle("Revisa la operación");a.setHeaderText("No se guardaron cambios");a.setContentText(mensaje(e));a.showAndWait();
 }
}
