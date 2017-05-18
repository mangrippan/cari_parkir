package com.tomcat.parkir;

import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tomcat.parkir.DB.DB;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class DetailParkirActivity extends AppCompatActivity  implements OnMapReadyCallback {


    private GoogleMap mMap;
    public Parkir parkir;
    public LatLng currentCoordinate;
    public LatLng targetCoordinate;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailparkir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_detail_parkir);

        String parkirId=null;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            parkirId = ""+extras.getInt("parkir_id");
            Log.d("parkirid",""+extras.getInt("parkir_id"));
        }
        getDetailParkir(parkirId);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        gps = new GPSTracker(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDetailParkir(String parkirId){
        DB db = new DB(this);
        parkir = db.getDetailParkir(parkirId);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        setUpParkir();
        drawDirection(currentCoordinate,targetCoordinate);
    }


    public void setUpMap(){
        double currentLatitude=0;
        double currentLongitude=0;
        mMap.setMyLocationEnabled(true);
        //set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }
        Log.d("currentlatitude",""+currentLatitude);
        // Create a LatLng object for the current location
        currentCoordinate = new LatLng(currentLatitude, currentLongitude);
        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoordinate));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }
    public void setUpParkir(){
        targetCoordinate = new LatLng(parkir.getLatitude(), parkir.getLongitude());
        mMap.addMarker(new MarkerOptions().position(targetCoordinate).title(parkir.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(targetCoordinate));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );
    }

    public void drawDirection(LatLng currentCoordinate, LatLng targetCoordinate){
        GMapV2Direction md = new GMapV2Direction();
        //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        Document doc = md.getDocument(currentCoordinate, targetCoordinate,
                GMapV2Direction.MODE_DRIVING);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        Polyline polylin = mMap.addPolyline(rectLine);
    }

    public void setView(){
        TextView textParkirName = (TextView) findViewById(R.id.textParkirName);
        TextView textParkirPrice = (TextView) findViewById(R.id.textParkirPrice);
        TextView textParkirAddress = (TextView) findViewById(R.id.textParkirAddress);
        TextView textParkirAvailable = (TextView) findViewById(R.id.textParkirAvailable);

        String price = parkir.getPrice();
        if(price.equals("0"))
            price = "Free";
        else if(price==null)
            price = "Unknown";

        textParkirName.setText(parkir.getName());
        textParkirPrice.setText(price);
        textParkirAddress.setText(parkir.getAddress());
        textParkirAvailable.setText(parkir.getAvailable()+" / "+parkir.getCapacity()+" Available");
    }
}
