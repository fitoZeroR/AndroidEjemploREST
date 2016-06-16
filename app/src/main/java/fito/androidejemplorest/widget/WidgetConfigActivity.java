package fito.androidejemplorest.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fito.androidejemplorest.R;

//public class WidgetConfigActivity extends AppCompatActivity {
public class WidgetConfigActivity extends AppCompatActivity {
    private int widgetId = 0;
    public static boolean banderaAccesoDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        //Obtenemos el Intent que ha lanzado esta ventana
        //y recuperamos sus parámetros
        Intent intentOrigen = getIntent();
        Bundle params = intentOrigen.getExtras();

        //Obtenemos el ID del widget que se está configurando
        widgetId = params.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        //Establecemos el resultado por defecto (si se pulsa el botón 'Atrás'
        //del teléfono será éste el resultado devuelto).
        setResult(RESULT_CANCELED);

        actualizaWidget();
    }

    private void actualizaWidget() {
        Log.d("Entra Config","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        banderaAccesoDatos = false;
        //Actualizamos el widget tras la configuración
        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(WidgetConfigActivity.this);
        UsuarioWidget.obtieneConfiguracionWidget(WidgetConfigActivity.this, appWidgetManager, widgetId);

        //Devolvemos como resultado: ACEPTAR (RESULT_OK)
        Intent resultado = new Intent();
        resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultado);
        finish();
    }
}
