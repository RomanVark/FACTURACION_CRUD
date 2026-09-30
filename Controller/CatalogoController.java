package ni.edu.uam.facturacionapp.Controller;

import javafx.fxml.FXML;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
public abstract class CatalogoController<T> {
 @FXML protected Pane root;
 @FXML protected TableView<T> tabla;
 @FXML protected TextField filtro;
 @FXML protected Label estado;
 @FXML protected Button eliminarBoton;
 protected T seleccionado;
 private final ObservableList<T> registros=FXCollections.observableArrayList();
 private final FilteredList<T> filas=new FilteredList<>(registros);
 protected abstract List<T> cargar() throws Exception;
 protected abstract void configurar();
 protected abstract T formulario();
 protected abstract void mostrar(T e);
 protected abstract void limpiar();
 protected abstract void persistir(T e) throws Exception;
 protected abstract void borrar(T e) throws Exception;
 protected abstract String textoBusqueda(T e);
 protected boolean coincideFiltro(T e, String texto) {
  return textoBusqueda(e).toLowerCase(Locale.ROOT).contains(texto.trim().toLowerCase(Locale.ROOT));
 }
 protected void configurarFiltros() {}
 protected void despuesDeCargar() {}

 @FXML public void initialize() {
  configurar();
  configurarFiltros();
  tabla.setPlaceholder(new Label("No hay registros para mostrar."));

  SortedList<T> ordenadas = new SortedList<>(filas);
  ordenadas.comparatorProperty().bind(tabla.comparatorProperty());
  tabla.setItems(ordenadas);

  tabla.getSelectionModel().selectedItemProperty().addListener((o,a,b)->{
   seleccionado=b;
   eliminarBoton.setDisable(b==null);
   if(b!=null) mostrar(b); else limpiar();
  });

  filtro.textProperty().addListener((o,a,b)->aplicarFiltro());

  nuevo();
  actualizar();
 }

 protected void aplicarFiltro() {
  String texto = filtro.getText() == null ? "" : filtro.getText();
  filas.setPredicate(e -> coincideFiltro(e, texto));
 }
 @SuppressWarnings("unchecked") protected void columna(int indice, Function<T,String> valor) {
  TableColumn<T,String> col=(TableColumn<T,String>)tabla.getColumns().get(indice);
  col.setCellValueFactory(c->new ReadOnlyStringWrapper(valor.apply(c.getValue())));
 }
 @FXML public void nuevo() { tabla.getSelectionModel().clearSelection(); seleccionado=null; limpiar(); eliminarBoton.setDisable(true); }
 @FXML public void actualizar() {
  estado.setText("Cargando registros…");
  Ui.ejecutar(root,this::cargar, datos->{ despuesDeCargar(); registros.setAll(datos); nuevo(); estado.setText(datos.size()+" registros cargados · Selecciona una fila para editar"); },e->estado.setText(Ui.mensaje(e)));
 }
 @FXML public void guardar() {
  try { T e=formulario();
   Ui.ejecutar(root,()->{persistir(e);return true;},ok->actualizar(),Ui::error);
  } catch(IllegalArgumentException e) { Ui.error(e); }
 }
 @FXML public void eliminar() {
  if(seleccionado==null) return;
  Alert confirmacion=new Alert(Alert.AlertType.CONFIRMATION,"¿Eliminar el registro seleccionado? Esta acción es permanente.",ButtonType.CANCEL,ButtonType.OK);
  confirmacion.initOwner(root.getScene().getWindow());confirmacion.setTitle("Eliminar registro");confirmacion.setHeaderText("Confirma la eliminación");
  if(confirmacion.showAndWait().orElse(ButtonType.CANCEL)!=ButtonType.OK)return;
  T e=seleccionado; Ui.ejecutar(root,()->{borrar(e);return true;},ok->actualizar(),Ui::error);
 }
}
