package com.example.mapsmaroon5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mymap;
    LatLng currentLocation;
    Stack<LatLng> locations = new Stack<>();

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
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (currentLocation == null) {
            Toast.makeText(this, "Esperando obtener la ubicación actual...", Toast.LENGTH_SHORT).show();
            return true;
        }
        LatLng destination = marker.getPosition();
        drawRoute(currentLocation,destination);
        marker.showInfoWindow();
        return true;
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
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mymap.addMarker(new MarkerOptions().position(currentLocation).title("Ubicacion Actual"));
                            mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));

                            locations.add(currentLocation);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mymap = googleMap;
        mymap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        BitmapDescriptor customIcon = resizeIcon(R.drawable.binoculars, 50, 50);

        //Santa Cruz
        LatLng scMariposario = new LatLng(-17.769306, -63.248581);
        mymap.addMarker(new MarkerOptions().position(scMariposario).title("Güembé Mariposario").icon(customIcon));

        LatLng scCatedral = new LatLng(-17.783879510292756, -63.18184542068362);
        mymap.addMarker(new MarkerOptions().position(scCatedral).title("Catedral Metropolitana Basilica Menor de San Lorenzo").icon(customIcon));

        LatLng scLomas = new LatLng(-17.94050947812592, -63.16008504635624);
        mymap.addMarker(new MarkerOptions().position(scLomas).title("Parque Regional Lomas de Arena").icon(customIcon));

        LatLng scFuerte = new LatLng(-18.17852637202513, -63.82047459453941);
        mymap.addMarker(new MarkerOptions().position(scFuerte).title("El Fuerte de Samaipata").icon(customIcon));

        LatLng scZoologico = new LatLng(-17.759832100421306, -63.18344340895514);
        mymap.addMarker(new MarkerOptions().position(scZoologico).title("Zoologico Municipal").icon(customIcon));

        drawPolygon(scMariposario,scFuerte,scLomas,scCatedral,scZoologico,ContextCompat.getColor(this,R.color.green));

        //Beni
        LatLng bnLagunaS = new LatLng(-14.886882572250338, -64.87613733422462);
        mymap.addMarker(new MarkerOptions().position(bnLagunaS).title("Laguna Suárez").icon(customIcon));

        LatLng bnRiberalta = new LatLng(-11.007341404033314, -66.05825113859729);
        mymap.addMarker(new MarkerOptions().position(bnRiberalta).title("Riberalta").icon(customIcon));

        LatLng bnRurrenabaque = new LatLng(-14.429635673122313, -67.53518293008288);
        mymap.addMarker(new MarkerOptions().position(bnRurrenabaque).title("Rurrenabaque").icon(customIcon));

        LatLng bnSIMoxos = new LatLng(-14.983228736123682, -65.63325078376016);
        mymap.addMarker(new MarkerOptions().position(bnSIMoxos).title("San Ignacio de Moxos").icon(customIcon));

        LatLng bnGuayamerin = new LatLng(-10.823960610156984, -65.36915532401049);
        mymap.addMarker(new MarkerOptions().position(bnGuayamerin).title("Guayamerin").icon(customIcon));

        drawPolygon(bnLagunaS,bnSIMoxos,bnRurrenabaque,bnRiberalta,bnGuayamerin,ContextCompat.getColor(this,R.color.purple));

        //Pando
        LatLng pdPuertoRico = new LatLng(-11.101640701897242, -67.55330088195544);
        mymap.addMarker(new MarkerOptions().position(pdPuertoRico).title("Puerto Rico").icon(customIcon));

        LatLng pdSena = new LatLng(-11.471447489381372, -67.2380426747378);
        mymap.addMarker(new MarkerOptions().position(pdSena).title("Sena").icon(customIcon));

        LatLng pdLagoTumichucua = new LatLng(-11.133552918019317, -66.17540323483405);
        mymap.addMarker(new MarkerOptions().position(pdLagoTumichucua).title("Lago Tumichucua").icon(customIcon));

        LatLng pdLaCostanera = new LatLng(-10.994091031569164, -66.07633767615931);
        mymap.addMarker(new MarkerOptions().position(pdLaCostanera).title("Parque Mirador La Costanera").icon(customIcon));

        LatLng pdEsmeraldaAquiles = new LatLng(-10.987397453776953, -65.9274784136918);
        mymap.addMarker(new MarkerOptions().position(pdEsmeraldaAquiles).title("Parque Esmeralda Aquiles").icon(customIcon));

        drawPolygon(pdEsmeraldaAquiles,pdLaCostanera,pdLagoTumichucua,pdSena,pdPuertoRico,ContextCompat.getColor(this,R.color.white));

        // Cochabamba
        LatLng cbCristo = new LatLng(-17.38434163463851, -66.13497397004808);
        mymap.addMarker(new MarkerOptions().position(cbCristo).title("Cristo de la Concordia").icon(customIcon));

        LatLng cbTorotoro = new LatLng(-18.136660548397337, -65.75886792391022);
        mymap.addMarker(new MarkerOptions().position(cbTorotoro).title("Parque Nacional Torotoro").icon(customIcon));

        LatLng cbIncachaca = new LatLng(-17.400704064378417, -66.05128253398148);
        mymap.addMarker(new MarkerOptions().position(cbIncachaca).title("Incachaca").icon(customIcon));

        LatLng cbLagoCorani = new LatLng(-17.266666436504735, -65.89991422531621);
        mymap.addMarker(new MarkerOptions().position(cbLagoCorani).title("Lago Corani").icon(customIcon));

        LatLng cbTunari = new LatLng(-17.335758367952696, -66.13991250814246);
        mymap.addMarker(new MarkerOptions().position(cbTunari).title("Parque Nacional Tunari").icon(customIcon));

        drawPolygon(cbTorotoro, cbLagoCorani, cbTunari, cbIncachaca, cbCristo, ContextCompat.getColor(this,R.color.blue));

        // Chuquisaca
        LatLng chCastillo = new LatLng(-19.084645686343908, -65.26861029010789);
        mymap.addMarker(new MarkerOptions().position(chCastillo).title("Castillo de la Glorieta").icon(customIcon));

        LatLng chParqueCretacico = new LatLng(-19.00645164004157, -65.23621497555953);
        mymap.addMarker(new MarkerOptions().position(chParqueCretacico).title("Parque Cretácico").icon(customIcon));

        LatLng chPlaza25 = new LatLng(-19.048237268790484, -65.26007731788275);
        mymap.addMarker(new MarkerOptions().position(chPlaza25).title("Plaza 25 de Mayo").icon(customIcon));

        LatLng chCatedral = new LatLng(-19.04872646599837, -65.25986424814575);
        mymap.addMarker(new MarkerOptions().position(chCatedral).title("Catedral Metropolitana de Nuestrra Señora de Guadalupe").icon(customIcon));

        LatLng chTarabuco = new LatLng(-19.12999194142099, -64.95371629548258);
        mymap.addMarker(new MarkerOptions().position(chTarabuco).title("Tarabuco").icon(customIcon));

        drawPolygon(chTarabuco, chParqueCretacico, chPlaza25, chCatedral, chCastillo, ContextCompat.getColor(this,R.color.red));

        // Tarija
        LatLng tjParqueBolivar = new LatLng(-21.53441009217693, -64.72573662637733);
        mymap.addMarker(new MarkerOptions().position(tjParqueBolivar).title("Parque Bolivar").icon(customIcon));

        LatLng tjSanJacinto = new LatLng(-21.633292438935097, -64.75008588520583);
        mymap.addMarker(new MarkerOptions().position(tjSanJacinto).title("Represa de San Jacinto").icon(customIcon));

        LatLng tjTariquia = new LatLng(-22.104370461302093, -64.4319007924908);
        mymap.addMarker(new MarkerOptions().position(tjTariquia).title("Reserva Nacional de flora y fauna de Tariquía").icon(customIcon));

        LatLng tjPadcaya = new LatLng(-21.888821700563586, -64.7117933045008);
        mymap.addMarker(new MarkerOptions().position(tjPadcaya).title("Padcaya").icon(customIcon));

        LatLng tjReservaSama = new LatLng(-21.618515395238276, -64.89060195178142);
        mymap.addMarker(new MarkerOptions().position(tjReservaSama).title("Reserva Sama").icon(customIcon));

        drawPolygon(tjPadcaya, tjReservaSama, tjSanJacinto, tjParqueBolivar, tjTariquia, ContextCompat.getColor(this,R.color.orange));

        // La Paz
        LatLng lpValleLuna = new LatLng(-16.567361353917608, -68.09390336999195);
        mymap.addMarker(new MarkerOptions().position(lpValleLuna).title("Valle de la Luna").icon(customIcon));

        LatLng lpTiwanaku = new LatLng(-16.560811500650555, -68.19961699827783);
        mymap.addMarker(new MarkerOptions().position(lpTiwanaku).title("Tiwanaku").icon(customIcon));

        LatLng lpCopacabana = new LatLng(-16.166302802178354, -69.08616791391631);
        mymap.addMarker(new MarkerOptions().position(lpCopacabana).title("Copacabana").icon(customIcon));

        LatLng lpNevadoIllimani = new LatLng(-16.63331642670789, -67.7908578888411);
        mymap.addMarker(new MarkerOptions().position(lpNevadoIllimani).title("Nevado Illimani").icon(customIcon));

        LatLng lpYungas = new LatLng(-16.497226182090014, -68.1294422146762);
        mymap.addMarker(new MarkerOptions().position(lpYungas).title("Los Yungas").icon(customIcon));

        drawPolygon(lpYungas, lpNevadoIllimani, lpValleLuna, lpTiwanaku, lpCopacabana, ContextCompat.getColor(this,R.color.yellow));

        // Oruro
        LatLng orSantuario = new LatLng(-17.967433403804385, -67.11887500112452);
        mymap.addMarker(new MarkerOptions().position(orSantuario).title("Santuario de la Virgen del Socavón").icon(customIcon));

        LatLng orSalarCoipasa = new LatLng(-19.382933378426085, -68.16639023998218);
        mymap.addMarker(new MarkerOptions().position(orSalarCoipasa).title("Salar de Coipasa").icon(customIcon));

        LatLng orCarnaval = new LatLng(-17.960706895801973, -67.10566615911512);
        mymap.addMarker(new MarkerOptions().position(orCarnaval).title("Carnaval de Oruro").icon(customIcon));

        LatLng orLagoUruUru = new LatLng(-18.09639869764223, -67.09831626151333);
        mymap.addMarker(new MarkerOptions().position(orLagoUruUru).title("Lago Uru Uru").icon(customIcon));

        LatLng orParia = new LatLng(-17.82007606090526, -67.0158034425392);
        mymap.addMarker(new MarkerOptions().position(orParia).title("Paria").icon(customIcon));

        drawPolygon(orSalarCoipasa, orParia, orSantuario, orCarnaval, orLagoUruUru, ContextCompat.getColor(this,R.color.gray));

        // Potosi
        LatLng ptCerroRico = new LatLng(-19.61884460804353, -65.7499123092201);
        mymap.addMarker(new MarkerOptions().position(ptCerroRico).title("Cerro Rico").icon(customIcon));

        LatLng ptCasaMoneda = new LatLng(-19.58888622723661, -65.7541830429072);
        mymap.addMarker(new MarkerOptions().position(ptCasaMoneda).title("Casa de la Moneda").icon(customIcon));

        LatLng ptLagunaColorada = new LatLng(-22.199459045913816, -67.77360982512064);
        mymap.addMarker(new MarkerOptions().position(ptLagunaColorada).title("Laguna Colorada").icon(customIcon));

        LatLng ptSalarUyuni = new LatLng(-20.133384233344046, -67.48911807546018);
        mymap.addMarker(new MarkerOptions().position(ptSalarUyuni).title("Salar de Uyuni").icon(customIcon));

        LatLng ptEduardoAvaroa = new LatLng(-22.581987479399473, -67.47547367468822);
        mymap.addMarker(new MarkerOptions().position(ptEduardoAvaroa).title("Reserva Eduardo Avaroa").icon(customIcon));

        drawPolygon(ptSalarUyuni, ptCasaMoneda, ptCerroRico, ptEduardoAvaroa, ptLagunaColorada, ContextCompat.getColor(this,R.color.green));
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

    private void drawRoute(LatLng origin, LatLng destination) {
        if (currentLocation != null) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(origin)
                    .add(destination)
                    .width(10)
                    .color(ContextCompat.getColor(this,R.color.orange));
            mymap.addPolyline(polylineOptions);
        } else {
            Toast.makeText(this, "Ubicación actual no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void drawPolygon(LatLng point1, LatLng point2, LatLng point3, LatLng point4, LatLng point5, int color)
    {
        PolygonOptions polygonOptions = new PolygonOptions()
                .add(point1,point2,point3,point4,point5)
                .strokeWidth(5)
                .strokeColor(ContextCompat.getColor(this, R.color.white))
                .fillColor(color);
        mymap.addPolygon(polygonOptions);
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