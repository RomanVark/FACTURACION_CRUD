package ni.edu.uam.facturacionapp.Controller;

import ni.edu.uam.facturacionapp.Model.Categoria;
import ni.edu.uam.facturacionapp.Service.CategoriaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
public class CategoriaController extends CatalogoController<Categoria> {
 @FXML private TextField nombre;
 @FXML private CheckBox activa;
 private final CategoriaService service=new CategoriaService();
 protected void configurar() { columna(0,e->e.getId().toString());columna(1,Categoria::getNombre);columna(2,e->e.isActiva()?"Activa":"Inactiva"); }
 protected List<Categoria> cargar() throws Exception { return service.listar(); }
 protected Categoria formulario() {return new Categoria(seleccionado==null?null:seleccionado.getId(),nombre.getText(),activa.isSelected());}
 protected void mostrar(Categoria e) {nombre.setText(e.getNombre());activa.setSelected(e.isActiva());}
 protected void limpiar() {nombre.clear();activa.setSelected(true);}
 protected void persistir(Categoria e) throws Exception {service.guardar(e);}
 protected void borrar(Categoria e) throws Exception {service.eliminar(e.getId());}
 protected String textoBusqueda(Categoria e) {return e.getId()+" "+e.getNombre()+" "+(e.isActiva()?"activa":"inactiva");}
}
