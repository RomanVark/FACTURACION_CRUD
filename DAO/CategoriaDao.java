package ni.edu.uam.facturacionapp.DAO;

import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import ni.edu.uam.facturacionapp.Crud.Crud;
import java.sql.*;
import java.util.*;
public class CategoriaDao implements Crud<Categoria> {
 private static final String SELECT = "SELECT * FROM categoria";
 private Categoria mapear(ResultSet rs) throws SQLException {
  return new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getBoolean("activa"));
 }
 private void parametros(PreparedStatement ps, Categoria e) throws SQLException {
  ps.setString(1, e.getNombre());
  ps.setBoolean(2, e.isActiva());
 }
 @Override public void guardar(Categoria e) throws SQLException {
  try (Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(
   "INSERT INTO categoria (nombre, activa) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
   parametros(ps,e); ps.executeUpdate();
   try(ResultSet rs=ps.getGeneratedKeys()) { if(rs.next()) e.setId(rs.getInt(1)); }
  }
 }
 @Override public List<Categoria> listar() throws SQLException {
  List<Categoria> resultado=new ArrayList<>();
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT+" ORDER BY id DESC"); ResultSet rs=ps.executeQuery()) {
   while(rs.next()) resultado.add(mapear(rs));
  } return resultado;
 }
 @Override public Optional<Categoria> buscar(Integer id) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT+" WHERE id=?")) {
   ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()) { return rs.next()?Optional.of(mapear(rs)):Optional.empty(); }
  }
 }
 @Override public void actualizar(Categoria e) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement("UPDATE categoria SET nombre=?, activa=? WHERE id=?")) {
   parametros(ps,e); ps.setInt(3,e.getId());
   if(ps.executeUpdate()!=1) throw new SQLException("El registro ya no existe; actualiza la tabla.");
  }
 }
 @Override public void eliminar(Integer id) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM categoria WHERE id=?")) {
   ps.setInt(1,id); if(ps.executeUpdate()!=1) throw new SQLException("El registro ya no existe; actualiza la tabla.");
  }
 }
}
