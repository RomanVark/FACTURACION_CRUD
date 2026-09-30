package ni.edu.uam.facturacionapp.Controller;

import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Service.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductoController extends CatalogoController<Producto> {

    @FXML private TextField codigo, nombre, precioVenta, existencia;
    @FXML private ComboBox<Categoria> categoria;
    @FXML private CheckBox activo;

    @FXML private ComboBox<String> filtroEstado;
    @FXML private ComboBox<Categoria> filtroCategoria;

    private final ProductoService service = new ProductoService();
    private List<Categoria> categorias = List.of();

    @Override
    protected void configurar() {
        // La tabla sigue exactamente los atributos solicitados en la práctica.
        columna(0, Producto::getCodigo);
        columna(1, Producto::getNombre);
        columna(2, e -> e.getCategoria().getNombre());
        columna(3, e -> e.getPrecioVenta().toPlainString());
        columna(4, e -> String.valueOf(e.getExistencia()));
        columna(5, e -> e.isActivo() ? "Activo" : "Inactivo");
    }

    @Override
    protected void configurarFiltros() {
        filtroEstado.getItems().setAll("Todos", "Activos", "Inactivos");
        filtroEstado.setValue("Todos");

        filtroCategoria.setPromptText("Todas las categorías");

        filtroEstado.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltro());
        filtroCategoria.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltro());
    }

    @Override
    protected List<Producto> cargar() throws Exception {
        categorias = new CategoriaService().listar();
        return service.listar();
    }

    @Override
    protected void despuesDeCargar() {
        Categoria seleccionada = filtroCategoria.getValue();
        filtroCategoria.getItems().setAll(categorias);

        if (seleccionada != null &&
                categorias.stream().anyMatch(c -> c.getId().equals(seleccionada.getId()))) {
            filtroCategoria.setValue(
                    categorias.stream()
                            .filter(c -> c.getId().equals(seleccionada.getId()))
                            .findFirst()
                            .orElse(null)
            );
        } else {
            filtroCategoria.setValue(null);
        }
    }

    @Override
    protected Producto formulario() {
        return new Producto(
                seleccionado == null ? null : seleccionado.getId(),
                codigo.getText(),
                nombre.getText(),
                categoria.getValue(),
                Validacion.precio(precioVenta.getText()),
                Validacion.existencia(existencia.getText()),
                activo.isSelected()
        );
    }

    @Override
    protected void mostrar(Producto e) {
        codigo.setText(e.getCodigo());
        nombre.setText(e.getNombre());
        precioVenta.setText(e.getPrecioVenta().toPlainString());
        existencia.setText(String.valueOf(e.getExistencia()));
        activo.setSelected(e.isActivo());

        categoria.setValue(
                categorias.stream()
                        .filter(c -> c.getId().equals(e.getCategoria().getId()))
                        .findFirst()
                        .orElse(e.getCategoria())
        );
    }

    @Override
    protected void limpiar() {
        codigo.clear();
        nombre.clear();
        precioVenta.setText("0.00");
        existencia.setText("0");
        categoria.setValue(null);
        activo.setSelected(true);
    }

    @Override
    protected void persistir(Producto e) throws Exception {
        service.guardar(e);
    }

    @Override
    protected void borrar(Producto e) throws Exception {
        service.eliminar(e.getId());
    }

    @Override
    protected String textoBusqueda(Producto e) {
        return e.getCodigo() + " " + e.getNombre() + " " + e.getCategoria().getNombre();
    }

    @Override
    protected boolean coincideFiltro(Producto e, String texto) {
        boolean coincideTexto = super.coincideFiltro(e, texto);

        String estado = filtroEstado.getValue();
        boolean coincideEstado =
                estado == null ||
                estado.equals("Todos") ||
                (estado.equals("Activos") && e.isActivo()) ||
                (estado.equals("Inactivos") && !e.isActivo());

        Categoria cat = filtroCategoria.getValue();
        boolean coincideCategoria =
                cat == null || cat.getId().equals(e.getCategoria().getId());

        return coincideTexto && coincideEstado && coincideCategoria;
    }

    @FXML
    private void limpiarBusqueda() {
        filtro.clear();
        filtroEstado.setValue("Todos");
        filtroCategoria.setValue(null);
    }
}
