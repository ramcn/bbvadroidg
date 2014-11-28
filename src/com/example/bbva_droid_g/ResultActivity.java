package com.example.bbva_droid_g;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ResultActivity extends FragmentActivity implements LocationListener  {
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;
    private LocationManager locationManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//android.os.Debug.waitForDebugger();
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_result);
        setUpMapIfNeeded();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.MIN_TIME, Constants.MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER               
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);        	 
  	  	startActivity(intent);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
        	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();            
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
    	Double default_lati=19.345;
    	Double default_longi=-99.175;
        LatLng markerLoc = new LatLng(default_lati, default_longi);

        mMap.addMarker(new MarkerOptions()
        .position(markerLoc)                                                                        // at the location you needed
        .title("my location")                                                                     // with a title you needed
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mypos)));        
        
        Intent intent = getIntent();        
		String winner_lati1 = intent.getStringExtra("winner_lati1");		
		String winner_longi1 = intent.getStringExtra("winner_longi1");
		String winner_lati2 = intent.getStringExtra("winner_lati2");		
		String winner_longi2 = intent.getStringExtra("winner_longi2");
		String winner_lati3 = intent.getStringExtra("winner_lati3");		
		String winner_longi3 = intent.getStringExtra("winner_longi3");
		
		if(winner_lati1 != null && winner_longi1 != null) {
			Double l1=Double.parseDouble(winner_lati1);
			Double l2=Double.parseDouble(winner_longi1);
	        LatLng markerLoc1 = new LatLng(l1, l2);
	        mMap.addMarker(new MarkerOptions()
	        .position(markerLoc1)                                                                        // at the location you needed
	        .title("top1")                                                                     // with a title you needed
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.top1)));

			Double l3=Double.parseDouble(winner_lati2);
			Double l4=Double.parseDouble(winner_longi2);
			LatLng markerLoc2 = new LatLng(l3, l4);
	        mMap.addMarker(new MarkerOptions()
	        .position(markerLoc2)                                                                        // at the location you needed
	        .title("top2")                                                                     // with a title you needed
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.top2)));
			

			Double l5=Double.parseDouble(winner_lati3);
			Double l6=Double.parseDouble(winner_longi3);
			LatLng markerLoc3 = new LatLng(l5, l6);
	        mMap.addMarker(new MarkerOptions()
	        .position(markerLoc3)                                                                        // at the location you needed
	        .title("top3")                                                                     // with a title you needed
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.top3)));

			
			LatLng latLng = new LatLng(l5, l6);
	        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLng(latLng);
	        mMap.moveCamera(cameraUpdate1);
	        
	        CameraUpdate cameraUpdate2 = CameraUpdateFactory.newLatLngZoom(latLng, 12);
	        mMap.animateCamera(cameraUpdate2);		
	        //locationManager.removeUpdates(this);

		}

    }
    
    @Override
    public void onLocationChanged(Location location) {
    	Double default_lati=19.345;
    	Double default_longi=-99.175;
        LatLng latLng = new LatLng(default_lati, default_longi);
				
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
       
}
