package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Cliente;
import ni.edu.uam.facturacionapp.DAO.ClienteDao;
import java.sql.SQLException;
import java.util.List;
public class ClienteService {
 private final ClienteDao dao=new ClienteDao();
 public List<Cliente> listar() throws SQLException { return dao.listar(); }
 public void guardar(Cliente e) throws SQLException {
  e.setNombre(Validacion.texto(e.getNombre(), "Nombre",150,true));
  e.setDocumento(Validacion.texto(e.getDocumento(), "Documento",50,true));
  e.setTelefono(Validacion.texto(e.getTelefono(), "Teléfono",30,false));
  e.setCorreo(Validacion.texto(e.getCorreo(), "Correo",150,false));
  e.setDireccion(Validacion.texto(e.getDireccion(), "Dirección",300,false));
  if(!e.getCorreo().isEmpty() && !e.getCorreo().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("El correo no tiene un formato válido.");
  if(e.getId()==null) dao.guardar(e); else dao.actualizar(e);
 }
 public void eliminar(Integer id) throws SQLException { dao.eliminar(id); }
}
