package fito.androidejemplorest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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

    // Seña númerica que se utiliza cuando se verifica la disponibilidad de los
    // google play services
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //public static final String EXTRA_MESSAGE = "message";
    // Clave que permite recuperar de las preferencias compartidas de la
    // aplicación el dentificador de registro en GCM
    private static final String PROPERTY_REG_ID = "registration_id";
    // Clave que permite recuperar de las preferencias compartidas de la
    // aplicación el dentificador de la versión de la aplicación
    private static final String PROPERTY_APP_VERSION = "appVersion";
    //private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "usuarioGCM";

    //public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    // Identificador de la instancia del servicio de GCM al cual accedemos
    String SENDER_ID = "617801276216";

    // Una simple Tag utilizada en los logs
    static final String TAG = "GCMDemo";

    // Contexto de la aplicación
    private Context context;
    // Identificador de registro
    private String regid;
    // Clase que da acceso a la api de GCM
    private GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Toolbar toolbarTlb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTlb);


        context = getApplicationContext();

        //Chequemos si está instalado Google Play Services
        // Se comprueba que Play Services APK estan disponibles, Si lo esta se
        // proocede con el registro en GCM
        if(checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);

            //Obtenemos el Registration ID guardado
            // Se recupera el "registration Id" almacenado en caso que la
            // aplicación ya se hubiera registrado previamente
            regid = getRegistrationId(context);

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                //TareaRegistroGCM tarea = new TareaRegistroGCM();
                //tarea.execute(txtUsuario.getText().toString());
                registroEnSegundoPlano();
            }
        }

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

    /**
     * Este metodo comprueba si Google Play Services esta disponible, ya que
     * este requiere que el terminal este asociado a una cuenta de google.Esta
     * verificación es necesaria porque no todos los dispositivos Android estan
     * asociados a una cuenta de Google ni usan sus servicios, por ejemplo, el
     * Kindle fire de Amazon, que es una tablet Android pero no requiere de una
     * cuenta de Google.
     *
     * @return Indica si Google Play Services esta disponible.
     */
    private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        }
	        else {
	            Log.i(TAG, "Dispositivo no soportado.");
	            finish();
	        }
	        return false;
	    }
	    return true;
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
                        editor.putString("id", String.valueOf(usuario.getId()));
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

    private String getRegistrationId(Context context) {
        SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0) {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        //String registeredUser = prefs.getString(PROPERTY_USER, "user");

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        //long expirationTime = prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        //String expirationDate = sdf.format(new Date(expirationTime));

        /*Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");*/

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        }
        /*else if (System.currentTimeMillis() > expirationTime) {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }
        else if (!txtUsuario.getText().toString().equals(registeredUser))
        {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }*/

        return registrationId;
    }

    /**
     * Recupera la versión aplicación que identifica a cada una de las
     * actualizaciones de la misma.
     *
     * @return La versión del codigo de la aplicación
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private void registroEnSegundoPlano() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    // En este metodo se invoca al servicio de registro de los
                    // servicios GCM
                    regid = gcm.register(SENDER_ID);
                    msg = "Dispositivo3 registrado, registration ID=" + regid;
                    Log.i(TAG, msg);
                    // Una vez se tiene el identificador de registro se manda a
                    // la aplicacion jee
                    // ya que para que esta envie el mensaje de la notificación
                    // a los servidores
                    // de GCM es necesario dicho identificador
                    registroServidor(params[0], regid);
                    // Se persiste el identificador de registro para que no sea
                    // necesario repetir el proceso de
                    // registro la proxima vez
                    setRegistrationId(params[0], regid);
                } catch (Exception e) {
                    msg = "Error :" + e.getMessage();
                    e.printStackTrace();
                }
                return msg;
            }

        }.execute(PROPERTY_USER);
    }

    private void registroServidor(String usuario, String regId) {
        NetConnection.registroClienteServidor(usuario, regId, new SmartStringVolleyListenerMessage(this) {
            @Override
            public void onSuccess(String responseString) {
                if(responseString.equals("1")) {
                    Log.d(TAG, "Registrado en mi servidor.");
                }

            }
        });
    }

    private void setRegistrationId(String usuario, String regId) {
        SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, usuario);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        //editor.putLong(PROPERTY_EXPIRATION_TIME, System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
    }
}