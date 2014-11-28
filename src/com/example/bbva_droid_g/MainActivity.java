package com.example.bbva_droid_g;

import java.io.IOException;
import java.net.URL;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.ProgressBar;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;


public class MainActivity extends FragmentActivity implements LocationListener {
		
	public static String[] queries={"select type","auto","car","gas","barsandrestaurants","bars","restaurant","beauty","beautyclinics","beautysalon","book","constructionmaterials","materials","paint","education","education_sub","fashion","fashionwomen","food","foodstore","bakery","health","pharmacy","hospital","homeaccessories","goods","jewelry_sub","leisure","leisuretime","videorental","musicalinstrument","office","officeaccessories","others","others_sub","pet"};
	public static String default_lati="19.345";
	public static String default_longi="-99.175";
	public static String winner_lati1 = default_lati; // top_1 result lati found by function find_top_1()
	public static String winner_longi1 = default_longi; // top_1 result longi found by function find_top_1()
	public static String winner_lati2 = default_lati; // top_2 result lati found by function find_top_1()
	public static String winner_longi2 = default_longi; // top_2 result longi found by function find_top_1()
	public static String winner_lati3 = default_lati; // top_3 result lati found by function find_top_1()
	public static String winner_longi3 = default_longi; // top_3 result longi found by function find_top_1()
	public static String[] lati1= new String[150];        
	public static String[] longi1= new String[150];   	
	public static String[] lati2= new String[150];        
	public static String[] longi2= new String[150];
	public static String[] lati3= new String[150];        
	public static String[] longi3= new String[150];
	static int back_pressed=0; // no need to reload if back button pressed. optimization.
	
	private ProgressBar spinner;
    boolean mIsSubmitClicked = false;
    private GoogleMap mMap;
    private LocationManager locationManager;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		initQueries();
		initUi();
        Spinner spinner1 = (Spinner) findViewById(R.id.lati);
        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, queries);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner1.setAdapter(spinnerArrayAdapter);
	}

	
	private void initQueries() {
    	//GetTypes asyncGetTypes = (GetTypes) new GetTypes();
    	//asyncGetTypes.execute();
	}

	private void initUi() {
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
	    spinner.setVisibility(View.GONE);
	    Button proc = (Button) findViewById(R.id.process_button);
        proc.setVisibility(View.GONE);
        setUpMapIfNeeded();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.MIN_TIME, Constants.MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER               
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    // This is the method that is called when the submit button is clicked
	public void find_top_1(View view) {
        Spinner latiSpinnerText = (Spinner) findViewById(R.id.lati);		
        Integer row_id = (latiSpinnerText.getSelectedItemPosition())-1;

        // row_id is -1 if "select type" is selected
        if(row_id != -1) {
        	GetTopOne asyncGetTopOne = (GetTopOne) new GetTopOne();
        	asyncGetTopOne.execute();
        }
    } 
    
  class GetTypes extends android.os.AsyncTask<Void, Void, Void> {    	
    	@Override
    	protected void onPreExecute() {
    	}        
        protected Void doInBackground(Void... params) {
            SpreadsheetService service = new SpreadsheetService("com.ss1");
        	//android.os.Debug.waitForDebugger();
            try {
            	String urlString = "https://spreadsheets.google.com/feeds/list/1umzqJ2j08Id6yw_KQSW8yD2_WL0yGyS1V7c5yEqwDvE/default/public/values";
                URL url = new URL(urlString);

                ListFeed feed = service.getFeed(url, ListFeed.class);
                Integer i=0;
                for (ListEntry entry : feed.getEntries()) {
                  CustomElementCollection elements = entry.getCustomElements();          
                  queries[i++] = elements.getValue("type");
                }                
              } catch (IOException e) {
                e.printStackTrace();
              } catch (ServiceException e) {
                e.printStackTrace();
              }
			return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	// TODO Auto-generated method stub
       }
    }

    class GetTopOne extends android.os.AsyncTask<Void, Void, Void> {    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		setProgressBarIndeterminateVisibility(true);
    		Button sub = (Button) findViewById(R.id.submit_button);
    		sub.setEnabled(false);
    		Button proc = (Button) findViewById(R.id.process_button);
    		spinner.setVisibility(View.VISIBLE);
    		mIsSubmitClicked = true;
    	}
        
        protected Void doInBackground(Void... params) {        	
            Spinner latiSpinnerText = (Spinner) findViewById(R.id.lati);
            // subtract 1 because the item begins with "type" 
            Integer row_id = (latiSpinnerText.getSelectedItemPosition())-1;

            if(back_pressed == 0) {
            back_pressed = 1;
            SpreadsheetService service = new SpreadsheetService("ss1");
            try {
            	String urlString = "https://spreadsheets.google.com/feeds/list/1umzqJ2j08Id6yw_KQSW8yD2_WL0yGyS1V7c5yEqwDvE/default/public/values";
                URL url = new URL(urlString);
                ListFeed feed = service.getFeed(url, ListFeed.class);
                Integer i=0;                
                for (ListEntry entry : feed.getEntries()) {
                  CustomElementCollection elements = entry.getCustomElements();                  
                  lati1[i] = elements.getValue("lati1");
                  longi1[i] = elements.getValue("longi1");
                  lati2[i] = elements.getValue("lati2");
                  longi2[i] = elements.getValue("longi2");
                  lati3[i] = elements.getValue("lati3");
                  longi3[i] = elements.getValue("longi3");
                  i++;
                }
              } catch (IOException e) {
                e.printStackTrace();
              } catch (ServiceException e) {
                e.printStackTrace();
              }
            }
        	        	
            // top_1 result to be marked on map
            winner_lati1=lati1[row_id]; // from ss
            winner_longi1=longi1[row_id]; // from ss
            winner_lati2=lati2[row_id]; // from ss
            winner_longi2=longi2[row_id]; // from 
            winner_lati3=lati3[row_id]; // from ss
            winner_longi3=longi3[row_id]; // from ss
			return null;        	
        }
        
        @Override
        protected void onPostExecute(Void result) {
        	super.onPostExecute(result);
        	Button sub = (Button) findViewById(R.id.submit_button);
    		sub.setEnabled(true);
            setProgressBarIndeterminateVisibility(false);
    		Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
    		intent.putExtra("winner_lati1", winner_lati1);
    		intent.putExtra("winner_longi1", winner_longi1);
    		intent.putExtra("winner_lati2", winner_lati2);
    		intent.putExtra("winner_longi2", winner_longi2);
    		intent.putExtra("winner_lati3", winner_lati3);
    		intent.putExtra("winner_longi3", winner_longi3);
    		startActivity(intent);
        }
    } // end async task
    

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
        LatLng markerLoc = new LatLng(19.43, -99.205);

        mMap.addMarker(new MarkerOptions()
        .position(markerLoc)                                                                        // at the location you needed
        .title("my location")                                                                     // with a title you needed
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mypos)));

        CameraUpdate cameraUpdate3 = CameraUpdateFactory.newLatLngZoom(markerLoc, 8);
        mMap.moveCamera(cameraUpdate3);              
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(19.345, -99.175);
				
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }
    
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


}
