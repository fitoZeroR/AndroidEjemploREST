package fito.androidejemplorest.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class StringVolleyListenerMessage implements Response.Listener<String>, Response.ErrorListener {
	private ProgressDialog dialog;

	protected void showMessage(Context c, String message) {
		dialog = new ProgressDialog(c);
		dialog.setIndeterminate(true);
        dialog.setCancelable(false); 
		dialog.setMessage(message);
		dialog.show();
	}
	
	protected void hideMessage() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}

    @Override
    public void onResponse(String response) {
       onSuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onFailure(error);
    }

    public abstract void onStart();

	public abstract void onSuccess(String responseString);

	public abstract void onFailure(VolleyError error);

}
