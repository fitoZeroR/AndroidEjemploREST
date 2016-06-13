package fito.androidejemplorest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public abstract class SmartStringVolleyListenerMessage extends StringVolleyListenerMessage {

    protected Activity activity;
    private String startMessage;
    private boolean showErrorMessage = true;

    public SmartStringVolleyListenerMessage(Activity activity) {
        this(activity, null, true);
    }

    public SmartStringVolleyListenerMessage(Activity activity, String startMessage) {
        this(activity, startMessage, true);
    }

    public SmartStringVolleyListenerMessage(Activity activity, boolean showErrorMessage) {
        this(activity, null, showErrorMessage);
    }

    public SmartStringVolleyListenerMessage(Activity activity, String startMessage, boolean showErrorMessage) {
        this.activity = activity;
        this.startMessage = startMessage;
        this.showErrorMessage = showErrorMessage;
    }

	@Override
	public void onStart() {
        if (startMessage != null) {
            showMessage(activity, startMessage);
        }
	}

	@Override
	public void onFailure(VolleyError error) {
        hideMessage();

        if (!showErrorMessage) {
            Log.e("Service Response", "Error server", error);
            return;
        }

        if (error instanceof TimeoutError) {
            AlertDialog.Builder builderRed = new AlertDialog.Builder(activity);
            builderRed.setTitle(null)
                    .setMessage("El servidor ha tardado mas de lo esperado, intente mas tarde.")
                    .setCancelable(true)
                    .setPositiveButton("OK", null);
            AlertDialog alertRed = builderRed.create();
            alertRed.show();
        }
        else {
            AlertDialog.Builder builderRed = new AlertDialog.Builder(activity);
            builderRed.setTitle(null)
                    .setMessage("Ocurrio un error de servidor")
                    .setCancelable(true)
                    .setPositiveButton("OK", null);
            AlertDialog alertRed = builderRed.create();
            alertRed.show();
            Log.e("Service Response", "Error server", error);
        }

    }
}
