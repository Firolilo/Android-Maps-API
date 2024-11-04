package com.example.mapsmaroon5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mymap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mymap=googleMap;

        BitmapDescriptor customIcon = resizeIcon(R.drawable.xd, 100, 70);

        LatLng santaCruz = new LatLng(-17.78629, -63.18117);
        mymap.addMarker(new MarkerOptions().position(santaCruz).title("Santa Cruz").icon(customIcon));

        /*LatLng laPaz = new LatLng(-16.50000, -68.11929);
        mymap.addMarker(new MarkerOptions().position(laPaz).title("La Paz"));

        LatLng cochabamba = new LatLng(-17.39050, -66.15740);
        mymap.addMarker(new MarkerOptions().position(cochabamba).title("Cochabamba"));

        LatLng sucre = new LatLng(-19.03333, -65.26278);
        mymap.addMarker(new MarkerOptions().position(sucre).title("Sucre"));

        LatLng potosi = new LatLng(-19.58333, -65.75920);
        mymap.addMarker(new MarkerOptions().position(potosi).title("Potosí"));

        LatLng tarija = new LatLng(-21.53500, -64.73000);
        mymap.addMarker(new MarkerOptions().position(tarija).title("Tarija"));

        LatLng beni = new LatLng(-13.28929, -65.39725);
        mymap.addMarker(new MarkerOptions().position(beni).title("Beni"));

        LatLng pando = new LatLng(-11.04703, -68.16818);
        mymap.addMarker(new MarkerOptions().position(pando).title("Pando"));

        LatLng oruro = new LatLng(-17.96500, -66.34900);
        mymap.addMarker(new MarkerOptions().position(oruro).title("Oruro"));*/

        mymap.moveCamera(CameraUpdateFactory.newLatLng(santaCruz));
    }

    private BitmapDescriptor resizeIcon(int xd, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(this, xd);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), xd);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}