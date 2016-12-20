package com.bangjoni.agentpulsa;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker driverMarker;
    private Marker customerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        LatLng driver = new LatLng(-6.2551596, 106.6198897);
        LatLng customer = new LatLng(-6.2501281, 106.5996596);

        mMap.setOnMarkerClickListener(this);

        driverMarker = mMap.addMarker(new MarkerOptions().position(driver).title("Driver"));
        customerMarker = mMap.addMarker(new MarkerOptions().position(customer).title("Customer"));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(driverMarker.getPosition());
        builder.include(customerMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(customerMarker)) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+driverMarker.getPosition().latitude+","+driverMarker.getPosition().longitude
                            +"&daddr="+customerMarker.getPosition().latitude+","+customerMarker.getPosition().longitude));
            startActivity(intent);
        }
        return false;
    }
}
