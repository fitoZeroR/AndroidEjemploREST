package fito.androidejemplorest.db.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import fito.androidejemplorest.db.DatabaseHelper;
import fito.androidejemplorest.db.DatabaseManager;
import fito.androidejemplorest.db.modelos.Producto;
import fito.androidejemplorest.utils.ICrud;

/**
 * Created by fito on 8/12/15.
 */
public class DAOProducto implements ICrud {
    private DatabaseHelper helper;

    public DAOProducto(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;

        Producto producto = (Producto) item;
        try {
            index = helper.getProductoDao().create(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int update(Object item) {
        return 0;
    }

    @Override
    public int delete(Object item) {
        return 0;
    }

    @Override
    public Object findById(int id) {
        return null;
    }

    @Override
    public List<?> findAll() {
        List<Producto> productos = null;

        try {
            productos =  helper.getProductoDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
}