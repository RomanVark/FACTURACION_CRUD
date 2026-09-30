package ni.edu.uam.facturacionapp.DAO;

import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import ni.edu.uam.facturacionapp.Crud.Crud;
import java.sql.*;
import java.util.*;
public class ClienteDao implements Crud<Cliente> {
 private static final String SELECT = "SELECT * FROM cliente";
 private Cliente mapear(ResultSet rs) throws SQLException {
  return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono"), rs.getString("correo"), rs.getString("direccion"), rs.getBoolean("activo"));
 }
 private void parametros(PreparedStatement ps, Cliente e) throws SQLException {
  ps.setString(1, e.getNombre());
  ps.setString(2, e.getDocumento());
  ps.setString(3, e.getTelefono());
  ps.setString(4, e.getCorreo());
  ps.setString(5, e.getDireccion());
  ps.setBoolean(6, e.isActivo());
 }
 @Override public void guardar(Cliente e) throws SQLException {
  try (Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(
   "INSERT INTO cliente (nombre, documento, telefono, correo, direccion, activo) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
   parametros(ps,e); ps.executeUpdate();
   try(ResultSet rs=ps.getGeneratedKeys()) { if(rs.next()) e.setId(rs.getInt(1)); }
  }
 }
 @Override public List<Cliente> listar() throws SQLException {
  List<Cliente> resultado=new ArrayList<>();
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT+" ORDER BY id DESC"); ResultSet rs=ps.executeQuery()) {
   while(rs.next()) resultado.add(mapear(rs));
  } return resultado;
 }
 @Override public Optional<Cliente> buscar(Integer id) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(SELECT+" WHERE id=?")) {
   ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()) { return rs.next()?Optional.of(mapear(rs)):Optional.empty(); }
  }
 }
 @Override public void actualizar(Cliente e) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement("UPDATE cliente SET nombre=?, documento=?, telefono=?, correo=?, direccion=?, activo=? WHERE id=?")) {
   parametros(ps,e); ps.setInt(7,e.getId());
   if(ps.executeUpdate()!=1) throw new SQLException("El registro ya no existe; actualiza la tabla.");
  }
 }
 @Override public void eliminar(Integer id) throws SQLException {
  try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM cliente WHERE id=?")) {
   ps.setInt(1,id); if(ps.executeUpdate()!=1) throw new SQLException("El registro ya no existe; actualiza la tabla.");
  }
 }
}
