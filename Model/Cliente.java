package ni.edu.uam.facturacionapp.Model;

import java.math.BigDecimal;

public class Cliente {
    private Integer id;
    private String nombre;
    private String documento;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;
    public Cliente() {}
    public Cliente(Integer id, String nombre, String documento, String telefono, String correo, String direccion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    @Override public String toString() { return nombre; }
}
