package fito.androidejemplorest.aplicacion;

import android.app.Application;

import fito.androidejemplorest.utils.VolleyUtils;

/**
 * Created by fito on 11/06/16.
 */
public class PrincipalAplication extends Application {
    public void onCreate(){
        super.onCreate();

        new VolleyUtils(this);
    }
}