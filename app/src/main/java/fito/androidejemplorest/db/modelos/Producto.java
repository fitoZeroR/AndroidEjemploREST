package fito.androidejemplorest.db.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fito on 8/12/15.
 */
@DatabaseTable
public class Producto implements Comparable {
    @DatabaseField(generatedId = true, columnName = "codigo")
    private int codigo;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Usuario usuario;
    @DatabaseField
    private String nombre;
    @DatabaseField
    private int cantidad;
    @DatabaseField
    private String descripcion;

    public Producto() {}

    public Producto(Usuario usuario, String nombre, int cantidad, String descripcion) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int compareTo(Object o) {
        String codigoProductoThis = String.valueOf(this.codigo);
        String codigoProductoAComparar = String.valueOf(((Producto)o).getCodigo());
        return codigoProductoThis.compareTo(codigoProductoAComparar);
    }
}