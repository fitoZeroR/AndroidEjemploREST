package fito.androidejemplorest.modelos;

import com.google.gson.annotations.SerializedName;

public class Usuario implements Comparable {
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellidoPaterno")
    private String apellidoPaterno;
    @SerializedName("apellidoMaterno")
    private String apellidoMaterno;
    @SerializedName("usuario")
    private String usuario;
    @SerializedName("contraseña")
    private String contraseña;
    @SerializedName("domicilio")
    private String domicilio;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("tipoUsuario")
    private String tipoUsuario;

    public Usuario() {}

    public Usuario(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String usuario, String contraseña, String domicilio, String telefono, String tipoUsuario) {
        this.id = id;
    	this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public int compareTo(Object o) {
        String idUsuarioThis = String.valueOf(this.id);
        String idUsuarioAComparar = String.valueOf(((Usuario)o).getId());
        return idUsuarioThis.compareTo(idUsuarioAComparar);
    }
}