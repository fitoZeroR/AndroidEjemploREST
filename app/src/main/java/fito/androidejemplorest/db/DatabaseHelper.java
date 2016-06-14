package fito.androidejemplorest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import fito.androidejemplorest.db.modelos.Producto;
import fito.androidejemplorest.db.modelos.Usuario;

/**
 * Created by fito on 8/12/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "profesionalDB.sqlite";

    private static final int DATABASE_VERSION = 1;

    private Dao<Usuario, Integer> usuarioDao = null;
    private Dao<Producto, Integer> productoDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Usuario.class);
            TableUtils.createTable(connectionSource, Producto.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Usuario.class, true);
            TableUtils.dropTable(connectionSource, Producto.class, true);
            onCreate(db, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new " + newVersion, e);
        }
        /*try {
            List<String> allSql = new ArrayList<String>();
            switch(oldVersion)
            {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }*/
    }

    public Dao<Usuario, Integer> getUsuarioDao() {
        if (null == usuarioDao) {
            try {
                usuarioDao = getDao(Usuario.class);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usuarioDao;
    }

    public Dao<Producto, Integer> getProductoDao() {
        if (null == productoDao) {
            try {
                productoDao = getDao(Producto.class);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productoDao;
    }

    @Override
    public void close() {
        super.close();
        usuarioDao = null;
        productoDao = null;
    }
}