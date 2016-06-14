package fito.androidejemplorest;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalizacionFragment extends Fragment {
    //@Bind(R.id.localizacionMapaPrincipalID)
    //GoogleMap googleMapLocalizacion;
    private GoogleMap googleMapLocalizacion;

    public LocalizacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localizacion, container, false);
        //ButterKnife.bind(this, view);

        googleMapLocalizacion = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.localizacionMapaPrincipalID)).getMap();

        googleMapLocalizacion.getUiSettings().setZoomControlsEnabled(true);
        //mapa.getUiSettings().setTiltGesturesEnabled(true);
        googleMapLocalizacion.getUiSettings().setAllGesturesEnabled(true);
        //mapa.setBuildingsEnabled(true);
        //googleMapLocalizacion.setMyLocationEnabled(true);

        //Obtenemos una referencia al LocationManager
        /*LocationManager locManager =  (LocationManager)view.getContext().getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/

        double lat = -106.42223741859198 ;
        double lon = 23.2010437036098;
        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(lon, lat), 17F);
        googleMapLocalizacion.animateCamera(cam);

        return view;
    }
}