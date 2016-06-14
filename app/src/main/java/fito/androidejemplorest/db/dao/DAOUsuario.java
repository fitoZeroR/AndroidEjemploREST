package fito.androidejemplorest.db.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import fito.androidejemplorest.db.DatabaseHelper;
import fito.androidejemplorest.db.DatabaseManager;
import fito.androidejemplorest.db.modelos.Usuario;
import fito.androidejemplorest.utils.ICrud;

/**
 * Created by fito on 8/12/15.
 */
public class DAOUsuario implements ICrud {
    private DatabaseHelper helper;

    public DAOUsuario(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;

        Usuario usuario = (Usuario) item;
        try {
            index = helper.getUsuarioDao().create(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;

        Usuario usuario= (Usuario) item;

        try {
            index = helper.getUsuarioDao().update(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;

        Usuario usuario = (Usuario) item;

        try {
            index = helper.getUsuarioDao().delete(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public Object findById(int id) {
        Usuario usuario = null;
        try {
            usuario = helper.getUsuarioDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
    public List<?> findAll() {
        List<Usuario> usuarios = null;

        try {
            usuarios =  helper.getUsuarioDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }
}