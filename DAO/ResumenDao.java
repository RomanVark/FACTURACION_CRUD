package ni.edu.uam.facturacionapp.DAO;

import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import java.sql.*;
public class ResumenDao {
 public record Resumen(long categorias,long productos,long clientes,long unidades,long activos) {}
 public Resumen obtener() throws SQLException {
  String sql="SELECT (SELECT count(*) FROM categoria), (SELECT count(*) FROM producto), (SELECT count(*) FROM cliente), (SELECT coalesce(sum(existencia),0) FROM producto), (SELECT count(*) FROM producto WHERE activo)";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()) {
   rs.next();return new Resumen(rs.getLong(1),rs.getLong(2),rs.getLong(3),rs.getLong(4),rs.getLong(5));
  }
 }
}
