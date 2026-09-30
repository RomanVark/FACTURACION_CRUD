package ni.edu.uam.facturacionapp.Controller;

import ni.edu.uam.facturacionapp.Model.Cliente;
import ni.edu.uam.facturacionapp.Service.ClienteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
public class ClienteController extends CatalogoController<Cliente> {
 @FXML private TextField nombre,documento,telefono,correo,direccion;
 @FXML private CheckBox activo;
 private final ClienteService service=new ClienteService();
 protected void configurar() {columna(0,e->e.getId().toString());columna(1,Cliente::getNombre);columna(2,Cliente::getDocumento);columna(3,Cliente::getTelefono);columna(4,Cliente::getCorreo);columna(5,Cliente::getDireccion);columna(6,e->e.isActivo()?"Activo":"Inactivo");}
 protected List<Cliente> cargar() throws Exception {return service.listar();}
 protected Cliente formulario() {return new Cliente(seleccionado==null?null:seleccionado.getId(),nombre.getText(),documento.getText(),telefono.getText(),correo.getText(),direccion.getText(),activo.isSelected());}
 protected void mostrar(Cliente e) {nombre.setText(e.getNombre());documento.setText(e.getDocumento());telefono.setText(e.getTelefono());correo.setText(e.getCorreo());direccion.setText(e.getDireccion());activo.setSelected(e.isActivo());}
 protected void limpiar() {nombre.clear();documento.clear();telefono.clear();correo.clear();direccion.clear();activo.setSelected(true);}
 protected void persistir(Cliente e) throws Exception {service.guardar(e);}
 protected void borrar(Cliente e) throws Exception {service.eliminar(e.getId());}
 protected String textoBusqueda(Cliente e) {return e.getId()+" "+e.getNombre()+" "+e.getDocumento()+" "+e.getTelefono()+" "+e.getCorreo();}
}
