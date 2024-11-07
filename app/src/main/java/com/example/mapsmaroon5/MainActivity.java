package com.example.mapsmaroon5;

import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mymap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private int LOCATION_PERMISSION_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        mymap=googleMap;

        BitmapDescriptor customIcon = resizeIcon(R.drawable.xd, 100, 70);

        LatLng sucre = new LatLng(-19.03333, -65.26278);
        mymap.addMarker(new MarkerOptions().position(sucre).title("Sucre"));

        /*
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


//        mymap.moveCamera(CameraUpdateFactory.newLatLng(santaCruz));
    }

    private void getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            Stack<LatLng> aa=new Stack<>();

                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mymap.addMarker(new MarkerOptions().position(currentLocation).title("Ubicacion Actual"));
                            mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));

                            LatLng santaCruz = new LatLng(-17.78629, -63.18117);
                            mymap.addMarker(new MarkerOptions().position(santaCruz).title("Santa Cruz"));

                            LatLng laPaz = new LatLng(-16.50000, -68.11929);
                            mymap.addMarker(new MarkerOptions().position(laPaz).title("La Paz"));

                            LatLng cochabamba = new LatLng(-17.39050, -66.15740);
                            mymap.addMarker(new MarkerOptions().position(cochabamba).title("Cochabamba"));

                            aa.add(currentLocation);
                            aa.add(santaCruz);
                            aa.add(laPaz);
                            aa.add(cochabamba);

                            drawRoute(aa);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
            else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void drawRoute(Stack<LatLng> points) {
        if(!points.isEmpty() && points.size()!=1){
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(points.pop())
                    .add(points.peek())
                    .width(10)
                    .color(ContextCompat.getColor(this,R.color.black));
            mymap.addPolyline(polylineOptions);
            drawRoute(points);
        }
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