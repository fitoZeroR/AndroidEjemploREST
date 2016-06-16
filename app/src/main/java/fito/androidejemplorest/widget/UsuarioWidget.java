package fito.androidejemplorest.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import fito.androidejemplorest.LoginActivity;
import fito.androidejemplorest.R;
import fito.androidejemplorest.aplicacion.PrincipalAplication;
import fito.androidejemplorest.db.modelos.Usuario;
import fito.androidejemplorest.network.NetConnection;
import fito.androidejemplorest.utils.SmartStringVolleyListenerMessage;

import static fito.androidejemplorest.widget.WidgetConfigActivity.banderaAccesoDatos;

/**
 * Created by fito on 14/06/16.
 */
public class UsuarioWidget extends AppWidgetProvider {
    private static List<Usuario> listaUsuario;
    private static int contdorUsuarios;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (banderaAccesoDatos) {
            //Iteramos la lista de widgets en ejecución
            for (int i = 0; i < appWidgetIds.length; i++) {
                //ID del widget actual
                int widgetId = appWidgetIds[i];

                if (i == 0) {
                    /* LOCAL */
                    //listaUsuario = ((PrincipalAplication) context.getApplicationContext()).listaUsuarios();

                    /* REMOTO */
                    obtieneConfiguracionWidget(context, appWidgetManager, widgetId);
                } else {
                    //Actualizamos el widget actual
                    actualizarWidget(context, appWidgetManager, widgetId);
                }
            }
        } else {
            //Iteramos la lista de widgets en ejecución
            for (int i = 0; i < appWidgetIds.length; i++)
            {
                //ID del widget actual
                int widgetId = appWidgetIds[i];

                //Actualizamos el widget actual
                actualizarWidget(context, appWidgetManager, widgetId);
            }
            banderaAccesoDatos = true;
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case "BACK":
                if (contdorUsuarios != 0) {
                    contdorUsuarios--;
                }
                obtieneIdWidget(context, intent);
                break;
            case "NEXT":
                if (contdorUsuarios != listaUsuario.size()-1) {
                    contdorUsuarios++;
                }
                obtieneIdWidget(context, intent);
                break;
        }
        super.onReceive(context, intent);
    }

    private void obtieneIdWidget(Context context, Intent intent) {
        //Obtenemos el ID del widget a actualizar
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        //Obtenemos el widget manager de nuestro contexto
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            actualizarWidget(context, widgetManager, widgetId);
        }
    }

    public static void obtieneConfiguracionWidget(final Context context, final AppWidgetManager appWidgetManager, final int widgetId) {
         /* LOCAL */
        //listaUsuario = ((PrincipalAplication) context.getApplicationContext()).listaUsuarios();

        /* REMOTO */
        NetConnection.obtieneUsuarios(new SmartStringVolleyListenerMessage((Activity) context) {
            @Override
            public void onSuccess(String responseString) {
                //hideMessage();

                Type collectionType = new TypeToken<List<Usuario>>(){}.getType();
                listaUsuario = (List<Usuario>) new Gson().fromJson(responseString, collectionType);

                actualizarWidget(context, appWidgetManager, widgetId);
            }
        });
        //actualizarWidget(context, appWidgetManager, widgetId);
    }

    private static void actualizarWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        //Obtenemos la lista de controles del widget actual
        RemoteViews controles = new RemoteViews(context.getPackageName(), R.layout.widget_usuario);

        if (listaUsuario != null) {
            Log.d("Entra onRecive actualiza", "*******************************************************************************************");
            Log.d("Entra onUpdate", "Usuario = " + listaUsuario.size());
            Log.d("Entra onUpdate", "contador = " + contdorUsuarios);
            Usuario usuario = listaUsuario.get(contdorUsuarios);
            Log.d("Entra actualiza", "Usuario nombre = " + usuario.getNombre());

            controles.setTextViewText(R.id.idLblWidgetID, "ID = "+usuario.getId());
            controles.setTextViewText(R.id.nombreLblWidgetID, "Nombre = "+usuario.getNombre());
            controles.setTextViewText(R.id.apellidoPaternoLblWidgetID, "Apellido Paterno = "+usuario.getApellidoPaterno());
            controles.setTextViewText(R.id.apellidoMaternoLblWidgetID, "Apellido Materno = "+usuario.getApellidoMaterno());
            controles.setTextViewText(R.id.usuarioLblWidgetID, "Usuario = "+usuario.getUsuario());
            controles.setTextViewText(R.id.contraseñaLblWidgetID, "Contraseña = "+usuario.getContraseña());
            controles.setTextViewText(R.id.domicilioLblWidgetID, "Domicilio = "+usuario.getDomicilio());
            controles.setTextViewText(R.id.telefonoLblWidgetID, "Telefono = "+usuario.getTelefono());
            controles.setTextViewText(R.id.tipoUsuarioLblWidgetID, "Tipo Usuario = "+usuario.getTipoUsuario());

        }

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.layoutPrincipalWidgetID, pendingIntent);

        //Asociamos los 'eventos' al widget
        final Intent intentBack = new Intent(context, UsuarioWidget.class);
        intentBack.setAction("BACK");
        intentBack.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        final PendingIntent pendingIntentBack = PendingIntent
                .getBroadcast(context, 0, intentBack,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.backWidgetID, pendingIntentBack);

        final Intent intentNext = new Intent(context, UsuarioWidget.class);
        intentNext.setAction("NEXT");
        intentNext.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        final PendingIntent pendingIntentNext = PendingIntent
                .getBroadcast(context, 0, intentNext,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.nextWidgetID, pendingIntentNext);

        //Notificamos al manager de la actualización del widget actual
        appWidgetManager.updateAppWidget(widgetId, controles);
    }
}