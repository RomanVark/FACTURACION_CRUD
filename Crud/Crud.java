package ni.edu.uam.facturacionapp.Crud;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public interface Crud<T> {
 void guardar(T entidad) throws SQLException;
 List<T> listar() throws SQLException;
 Optional<T> buscar(Integer id) throws SQLException;
 void actualizar(T entidad) throws SQLException;
 void eliminar(Integer id) throws SQLException;
}
