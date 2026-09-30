package ni.edu.uam.facturacionapp.Config;

import java.sql.*;
/** Configuración externa: no guardar contraseñas en el repositorio. */
public final class DatabaseConnection {
 private DatabaseConnection() {}
 public static Connection getConnection() throws SQLException {
  String url = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/tienda_javafx");
  java.util.Properties properties = new java.util.Properties();
  properties.setProperty("user", System.getenv().getOrDefault("DB_USER", "postgres"));
  properties.setProperty("password", System.getenv().getOrDefault("DB_PASSWORD", "1234"));
  properties.setProperty("connectTimeout", "5");
  properties.setProperty("socketTimeout", "15");
  return DriverManager.getConnection(url, properties);
 }
 public static boolean probarConexion() throws SQLException {
  try (Connection c = getConnection()) { return c.isValid(5); }
 }
}
