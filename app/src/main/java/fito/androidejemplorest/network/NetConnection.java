package fito.androidejemplorest.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import fito.androidejemplorest.R;
import fito.androidejemplorest.utils.StringVolleyListenerMessage;
import fito.androidejemplorest.utils.VolleyUtils;


public class NetConnection {
    private static final String TAG = "NetConnection";
    public static final String BASE_WS_URL = "http://192.168.100.2:8080/WSEjemploAndroid/rest/android/";

    private static int DEFAULT_TIMEOUT = 10 * 1000;
    private static int DEFAULT_RETRIES = 1;
    private static float DEFAULT_BACKOFF_MULTIPLIER = 1;

    private static final boolean DEMO = false;

    public static boolean isOnline(Activity a , boolean showMessage) {
        ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        if (showMessage){
            AlertDialog.Builder builder = new AlertDialog.Builder(a);
            builder.setMessage(a.getString(R.string.noInternet))
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

            AlertDialog.Builder builderRed = new AlertDialog.Builder(a);
            builderRed.setTitle("Verifica tu conexión a internet.")
                    .setMessage("Error de red")
                    .setCancelable(true)
                    .setPositiveButton("OK", null);
            AlertDialog alertRed = builderRed.create();
            alertRed.show();
        }
        return false;
    }

    public static void login(String usuario, String password, StringVolleyListenerMessage responseHandler) {
        Map<String, String> map = new HashMap<>();
        map.put("usuario", usuario);
        map.put("password", password);

        String queryString = VolleyUtils.mapToQueryString(map);

        String endpoint = "valida_usuario?"+queryString;
        String WS_URL = BASE_WS_URL + endpoint;
        final String tag = "alfaro_"+endpoint;

        Log.d("fito", WS_URL);

        final StringRequest myReq = new StringRequest(Request.Method.GET, WS_URL, responseHandler, responseHandler);
        myReq.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, DEFAULT_RETRIES, DEFAULT_BACKOFF_MULTIPLIER));

        responseHandler.onStart();
        VolleyUtils.getInstance().addToRequestQueue(myReq, tag);
    }
}