package fito.androidejemplorest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fito.androidejemplorest.aplicacion.PrincipalAplication;
import fito.androidejemplorest.db.modelos.Producto;
import fr.ganfra.materialspinner.MaterialSpinner;

public class ProductosFragment extends Fragment {
    private View view;

    private List<Producto>  listaProductos;

    @Bind(R.id.input_codigo)
    MaterialSpinner listaProductosSpn;

    @Bind(R.id.usuarioFrmProductoID)
    EditText idUsuarioEdt;
    @Bind(R.id.nombreFrmProductoID)
    EditText nombreEdt;
    @Bind(R.id.cantidadFrmProductoID)
    EditText cantidadEdt;
    @Bind(R.id.descripcionFrmProductoID)
    EditText descripcionEdt;

    public ProductosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_productos, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences prefs = getActivity().getSharedPreferences("PreferenciasEjemploAndroid", Context.MODE_PRIVATE);
        idUsuarioEdt.setText(String.valueOf(prefs.getInt("id",-1)));

        return view;
    }

    @OnItemSelected(R.id.input_codigo)
    public void pintaInterfazProductos(int position) {
        Log.d("Tamaño lista productos = ", ""+listaProductos.size());
        Log.d("Posicion = ", ""+position);
        if (position != -1) {
            Producto producto = listaProductos.get(position);
            idUsuarioEdt.setText(String.valueOf(producto.getUsuario().getId()));
            nombreEdt.setText(producto.getNombre());
            cantidadEdt.setText(String.valueOf(producto.getCantidad()));
            descripcionEdt.setText(producto.getDescripcion());
        }
    }

    @OnClick(R.id.consultarFrmProductoID)
    public void consultarProducto(View view) {
        listaProductos = ((PrincipalAplication) getActivity().getApplication()).listaProductos();
        if (listaProductos.size() < 1) {
            Snackbar.make(view, "Noy productos a mostrar.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
        } else {
            List<Integer> productosID = new ArrayList<>();
            for (Producto producto : listaProductos) {
                productosID.add(producto.getCodigo());
            }

            ArrayAdapter<Integer> adaptador = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item, productosID);
            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listaProductosSpn.setAdapter(adaptador);
        }
    }

    @OnClick(R.id.guardarFrmProductoID)
    public void guardaProducto(View view) {
        if (!TextUtils.isEmpty(nombreEdt.getText()) & !TextUtils.isEmpty(cantidadEdt.getText()) & !TextUtils.isEmpty(descripcionEdt.getText())) {
            Producto producto = new Producto(((PrincipalAplication) getContext().getApplicationContext()).getUsuario(), nombreEdt.getText().toString(), Integer.parseInt(cantidadEdt.getText().toString()), descripcionEdt.getText().toString());
            if (((PrincipalAplication) getActivity().getApplication()).creaProducto(producto)) {
                limpiaTextos();
                consultarProducto(getView());
                Snackbar.make(getView(), "Datos almacenados correctamente.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
            } else {
                Snackbar.make(getView(), "Error, los datos no se almacenaron.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
            }
        } else {
            Snackbar.make(view, "No deben haber camos vacios.", Snackbar.LENGTH_SHORT).setAction("Alerta", null).show();
        }

    }

    @OnClick(R.id.limpiarFrmProductoID)
    public void limpiarDatosProducto(View view) {
        limpiaTextos();
    }

    private void limpiaTextos() {
        listaProductosSpn.setSelection(-1);
        nombreEdt.setText("");
        cantidadEdt.setText("");
        descripcionEdt.setText("");
    }
}