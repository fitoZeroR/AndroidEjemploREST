package fito.androidejemplorest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fito.androidejemplorest.db.modelos.Usuario;
import fito.androidejemplorest.network.NetConnection;
import fito.androidejemplorest.utils.SmartStringVolleyListenerMessage;

import static android.location.LocationManager.GPS_PROVIDER;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbarTlb;

    @Bind(R.id.usuarioID)
    EditText usuarioEdt;
    @Bind(R.id.passwordID)
    EditText passwordEdt;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Toolbar toolbarTlb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTlb);

        //Recuperamos las preferencias almacenadas
        SharedPreferences prefs = getSharedPreferences("PreferenciasEjemploAndroid", Context.MODE_PRIVATE);
        String usuario = prefs.getString("usuario", "");
        String contraseña = prefs.getString("contraseña", "");

        //Comprobamos nombre y clave de ususario
        if(usuario.equals("") & contraseña.equals("")){
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(GPS_PROVIDER)) {
                alertaGPS();
            }
        } else {
            //Si el usuario almacenado es correcto, entramos en la app
            startActivity(new Intent(LoginActivity.this, MenuPrincipalActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.salirLogin_menuD) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.autentificarID)
    public void validar(final View view) {
        if (TextUtils.isEmpty(usuarioEdt.getText()) | TextUtils.isEmpty(passwordEdt.getText())){
            Snackbar.make(view, "No deben existir campos vacios.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
        }
        else {
            NetConnection.login(usuarioEdt.getText().toString(), passwordEdt.getText().toString(), new SmartStringVolleyListenerMessage(this, "Buscando...") {
                @Override
                public void onSuccess(String responseString) {
                    hideMessage();

                    Gson gson = new Gson();
                    usuario = gson.fromJson(responseString, Usuario.class);

                    Log.d("JSON VALIDA USUARIO = ", responseString);

                    if (usuario != null) {
                        //Si el usuario escrito es correcto, almacenamos la preferencia y entramos en la app
                        SharedPreferences settings = getSharedPreferences("PreferenciasEjemploAndroid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("id", usuario.getId());
                        editor.putString("nombre", usuario.getNombre());
                        editor.putString("apellidoPaterno", usuario.getApellidoPaterno());
                        editor.putString("apellidoMaterno", usuario.getApellidoMaterno());
                        editor.putString("usuario", usuario.getUsuario());
                        editor.putString("contraseña", usuario.getContraseña());
                        editor.putString("domicilio", usuario.getDomicilio());
                        editor.putString("telefono", usuario.getTelefono());
                        editor.putString("tipoUsuario", usuario.getTipoUsuario());

                        //Confirmamos el almacenamiento.
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, MenuPrincipalActivity.class));
                        finish();
                    } else {
                        Snackbar.make(view, "Usuario no existe.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
                    }
                }
            });
        }
    }

    private void alertaGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}