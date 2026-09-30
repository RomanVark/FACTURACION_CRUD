package ni.edu.uam.facturacionapp.Controller;

import ni.edu.uam.facturacionapp.Main;
import ni.edu.uam.facturacionapp.DAO.ResumenDao;
import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class MainController {
 @FXML private Pane root;
 @FXML private Label totalCategorias,totalProductos,totalClientes,unidades,activos,estado;
 private final Map<String,Stage> ventanas=new HashMap<>();
 @FXML public void initialize() {
  actualizar();
  root.sceneProperty().addListener((o,a,scene)->{if(scene!=null)scene.windowProperty().addListener((ob,old,w)->{if(w!=null)w.focusedProperty().addListener((obs,antes,ahora)->{if(ahora & !root.isDisabled())actualizar();});});});
 }
 @FXML public void actualizar() {
  estado.setText("Consultando resumen…");
  Ui.ejecutar(root,()->new ResumenDao().obtener(),r->{
   totalCategorias.setText(""+r.categorias());totalProductos.setText(""+r.productos());totalClientes.setText(""+r.clientes());unidades.setText(r.unidades()+" unidades en inventario");activos.setText(r.activos()+" productos activos");estado.setText("Datos actualizados desde PostgreSQL");
  },e->{totalCategorias.setText("—");totalProductos.setText("—");totalClientes.setText("—");unidades.setText("Resumen no disponible");activos.setText("");estado.setText(Ui.mensaje(e));});
 }
 @FXML private void categorias() {abrir("Categoria","Categorías");}
 @FXML private void productos() {abrir("Producto","Productos");}
 @FXML private void clientes() {abrir("Cliente","Clientes");}
 private void abrir(String archivo,String titulo) {
  Stage existente=ventanas.get(archivo);if(existente!=null){existente.show();existente.toFront();return;}
  try {
   Stage stage=new Stage(); stage.initOwner(root.getScene().getWindow());stage.setTitle("FacturaciónAPP | "+titulo);
   stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource(archivo+".fxml"))));stage.setMinWidth(1000);stage.setMinHeight(760);
   ventanas.put(archivo,stage);stage.setOnHidden(e->{ventanas.remove(archivo);if(!root.isDisabled())actualizar();});stage.show();
  } catch(IOException e) {Ui.error(e);}
 }
 @FXML private void probarConexion() {Ui.ejecutar(root,DatabaseConnection::probarConexion,ok->estado.setText(ok?"Conexión JDBC verificada correctamente":"El servidor no respondió"),e->estado.setText(Ui.mensaje(e)));}
}
