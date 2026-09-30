package ni.edu.uam.facturacionapp.DAO;

import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import ni.edu.uam.facturacionapp.Crud.Crud;

import java.sql.*;
import java.util.*;

public class ProductoDao implements Crud<Producto> {

    private static final String SELECT =
            "SELECT p.*, c.nombre AS categoria_nombre, c.activa AS categoria_activa " +
            "FROM producto p JOIN categoria c ON c.id=p.categoria_id";

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("categoria_nombre"),
                        rs.getBoolean("categoria_activa")
                ),
                rs.getBigDecimal("precio_venta"),
                rs.getInt("existencia"),
                rs.getBoolean("activo")
        );
    }

    private void parametros(PreparedStatement ps, Producto e) throws SQLException {
        ps.setString(1, e.getCodigo());
        ps.setString(2, e.getNombre());
        ps.setInt(3, e.getCategoria().getId());
        ps.setBigDecimal(4, e.getPrecioVenta());
        ps.setInt(5, e.getExistencia());
        ps.setBoolean(6, e.isActivo());
    }

    @Override
    public void guardar(Producto e) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO producto " +
                     "(codigo, nombre, categoria_id, precio_venta, existencia, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            parametros(ps, e);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> resultado = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY p.id DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) resultado.add(mapear(rs));
        }

        return resultado;
    }

    @Override
    public Optional<Producto> buscar(Integer id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE p.id=?")) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public void actualizar(Producto e) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE producto SET codigo=?, nombre=?, categoria_id=?, " +
                     "precio_venta=?, existencia=?, activo=? WHERE id=?")) {

            parametros(ps, e);
            ps.setInt(7, e.getId());

            if (ps.executeUpdate() != 1) {
                throw new SQLException("El registro ya no existe; actualiza la tabla.");
            }
        }
    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM producto WHERE id=?")) {

            ps.setInt(1, id);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("El registro ya no existe; actualiza la tabla.");
            }
        }
    }
}
