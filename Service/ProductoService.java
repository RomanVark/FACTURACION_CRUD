package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Producto;
import ni.edu.uam.facturacionapp.DAO.ProductoDao;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    private final ProductoDao dao = new ProductoDao();

    public List<Producto> listar() throws SQLException {
        return dao.listar();
    }

    public void guardar(Producto e) throws SQLException {
        e.setCodigo(Validacion.texto(e.getCodigo(), "Código", 50, true));
        e.setNombre(Validacion.texto(e.getNombre(), "Nombre", 150, true));

        if (e.getCategoria() == null || e.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Selecciona una categoría.");
        }

        if (e.getPrecioVenta() == null) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }

        e.setPrecioVenta(Validacion.precio(e.getPrecioVenta().toPlainString()));
        e.setExistencia(Validacion.existencia(String.valueOf(e.getExistencia())));

        if (e.getId() == null) {
            dao.guardar(e);
        } else {
            dao.actualizar(e);
        }
    }

    public void eliminar(Integer id) throws SQLException {
        dao.eliminar(id);
    }
}
