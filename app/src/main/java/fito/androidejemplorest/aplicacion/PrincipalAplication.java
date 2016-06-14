package fito.androidejemplorest.aplicacion;

import android.app.Application;

import java.util.List;

import fito.androidejemplorest.db.dao.DAOProducto;
import fito.androidejemplorest.db.dao.DAOUsuario;
import fito.androidejemplorest.db.modelos.Producto;
import fito.androidejemplorest.db.modelos.Usuario;
import fito.androidejemplorest.utils.VolleyUtils;

/**
 * Created by fito on 11/06/16.
 */
public class PrincipalAplication extends Application {
    private Usuario usuario;
    DAOProducto daoProducto = new DAOProducto(this);

    public void onCreate(){
        super.onCreate();

        new VolleyUtils(this);
    }

    // DAO USUARIOS

    public List<Usuario> listaUsuarios() {
        DAOUsuario daoUsuario = new DAOUsuario(this);
        List<Usuario> listaUsuario = (List<Usuario>) daoUsuario.findAll();
        return listaUsuario;
    }

    public boolean creaUsuario(Usuario usuario) {
        DAOUsuario daoUsuario = new DAOUsuario(this);
        if (daoUsuario.create(usuario) == -1) {
            return false;
        } else {
            return true;
        }
    }

    // DAO USUARIOS

    public List<Producto> listaProductos() {
        List<Producto> listaProducto = (List<Producto>) daoProducto.findAll();
        return listaProducto;
    }

    public boolean creaProducto(Producto producto) {
        if (daoProducto.create(producto) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}