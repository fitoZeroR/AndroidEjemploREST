package fito.androidejemplorest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fito.androidejemplorest.aplicacion.PrincipalAplication;
import fito.androidejemplorest.db.modelos.Usuario;

public class MenuPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbarTlb;
    //private Toolbar toolbarTlb;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    //private DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;
    //private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ButterKnife.bind(this);

        //toolbarTlb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTlb);

        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarTlb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new LocalizacionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        getSupportActionBar().setTitle("Localización Geografica");

        SharedPreferences prefs = getSharedPreferences("PreferenciasEjemploAndroid", Context.MODE_PRIVATE);
        View headerLayout = navigationView.getHeaderView(0);
        TextView tituloNavView = ButterKnife.findById(headerLayout, R.id.tituloView);
        tituloNavView.setText(prefs.getString("usuario", ""));
        TextView subTituloNavView = ButterKnife.findById(headerLayout, R.id.subtituloView);
        subTituloNavView.setText(prefs.getString("nombre", "")+" "+prefs.getString("apellidoPaterno", "")+" "+prefs.getString("apellidoMaterno", ""));

        List<Usuario> listaUsuario = ((PrincipalAplication) getApplication()).listaUsuarios();
        if (listaUsuario == null) {
            creaUsuario(prefs);
        } else {
            boolean validaUsuario = true;
            for (Usuario usuario : listaUsuario) {
                if (usuario.getUsuario().equals(prefs.getString("usuario","")) & usuario.getContraseña().equals(prefs.getString("contraseña", ""))) {
                    ((PrincipalAplication) getApplication()).setUsuario(usuario);
                    validaUsuario = false;
                }
            }
            if (validaUsuario) {
                creaUsuario(prefs);
            }
        }
    }

    private void creaUsuario(SharedPreferences prefs) {
        Usuario usuario = new Usuario(prefs.getString("nombre", ""), prefs.getString("apellidoPaterno", ""), prefs.getString("apellidoMaterno", ""), prefs.getString("usuario", ""),
                prefs.getString("contraseña", ""), prefs.getString("domicilio", ""), prefs.getString("telefono", ""), prefs.getString("tipoUsuario", ""));
        if (((PrincipalAplication) getApplication()).creaUsuario(usuario)) {
            ((PrincipalAplication) getApplication()).setUsuario(usuario);
            Snackbar.make(findViewById(android.R.id.content), "Datos almacenados correctamente.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Error, los datos no se almacenaron.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.localizacio_menu_lateral:
                fragment = new LocalizacionFragment();
                fragmentTransaction = true;
                break;
            case R.id.productos_menu_lateral:
                fragment = new ProductosFragment();
                fragmentTransaction = true;
                break;
            case R.id.salir_menu_lateral:
                //Borramos el usuario almacenado en preferencias y volvemos a la pantalla de login
                SharedPreferences settings = getSharedPreferences("PreferenciasEjemploAndroid", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("id", "");
                editor.putString("nombre", "");
                editor.putString("apellidoPaterno", "");
                editor.putString("apellidoMaterno", "");
                editor.putString("usuario", "");
                editor.putString("contraseña", "");
                editor.putString("domicilio", "");
                editor.putString("telefono", "");
                editor.putString("tipoUsuario", "");

                //Confirmamos el almacenamiento.
                editor.commit();

                //Volvemos a la pantalla de Login
                startActivity(new Intent(MenuPrincipalActivity.this, LoginActivity.class));
                finish();
        }

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}