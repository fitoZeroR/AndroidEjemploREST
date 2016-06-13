package fito.androidejemplorest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import fito.androidejemplorest.modelos.Usuario;
import fito.androidejemplorest.network.NetConnection;
import fito.androidejemplorest.utils.SmartStringVolleyListenerMessage;

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
            NetConnection.login(usuarioEdt.getText().toString(), passwordEdt.getText().toString(), new SmartStringVolleyListenerMessage(this) {
                @Override
                public void onSuccess(String responseString) {
                    hideMessage();

                    Gson gson = new Gson();
                    usuario = gson.fromJson(responseString, Usuario.class);

                    Log.d("JSON VALIDA USUARIO = ", responseString);

                    if (usuario != null) {
                        Log.e("Entro", "Entrooooooooooooooooooooooooooooooooo");
                    } else {
                        Snackbar.make(view, "Usuario no existe.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
                    }
                }
            });
        }
    }
}