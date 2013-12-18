package com.example.gcwg2014;

//Student Fraser Wright Matric:S1223483 Email:fraser_wright@hotmail.com, fwrigh200@caledonian.ac.uk

import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener, OnMarkerClickListener {
	
	//Declare Variables
	private GoogleMap googlemap;
	static final LatLng GLASGOW = new LatLng(55.874, -4.287);
	private Boolean viewtype = true;
	private LatLng userPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	super.onCreate(savedInstanceState);
	
	//checks if Google Play is working and if is to set the content view
	if(isGooglePlay()) {
		setContentView(R.layout.activity_main);
	}

	setUpMap();
	
	addMarkers();
	
	//attaches an onMarkerClickListener to the googlemap
	googlemap.setOnMarkerClickListener(this);
	
	displayLocationDialog();
	
	
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 //Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    menu.findItem(R.id.action_worldview).setVisible(viewtype);
	    menu.findItem(R.id.action_cityview).setVisible(!viewtype);
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		if (item.getItemId()== R.id.action_worldview) {
			
			viewtype = false;
			
			googlemap.clear();
			addCityMarkers();
			//Move the camera instantly to the World View centred on Glasgow
			googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(GLASGOW, 0));

		}
		else if (item.getItemId()== R.id.action_cityview) {
			
			viewtype = true;
			
			googlemap.clear();
			
			addMarkers();
			
			//Move the camera instantly to Glasgow with a zoom of 15.
			googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(GLASGOW, 15));

			// Zoom in, animating the camera.
			googlemap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
		}
		else {
			displayLocationDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	

	//This method discovers if the Google Play Store is available
	private boolean isGooglePlay() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(status == ConnectionResult.SUCCESS) {
			return(true);
		}
		else{
			//creates an error dialog requesting to visit the Google Play Store
			GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
			
		}
		return(false);
	}
	
	
	//This method declares a value for googlemap and then applies a suitable camera view
	private void setUpMap() {
		if(googlemap == null) {
			googlemap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			
			if(googlemap != null){
				
				googlemap.setMyLocationEnabled(true);
				
				
				//Move the camera instantly to Glasgow with a zoom of 15.
				googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(GLASGOW, 15));

				// Zoom in, animating the camera.
				googlemap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
			}
		}
	}
	
	private void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.e("error", "Location Provider not available");
		Toast.makeText(this, "Location Provider not available",     Toast.LENGTH_LONG)
        .show();
	}

	private void addMarkers() {
		//Strathclyde Country Park Marker
				googlemap.addMarker(new MarkerOptions()
				.title("Strathclyde_Country_Park")
				.snippet("366 Hamilton Rd, Motherwell ML1 3ED Triathalon")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.triathalon))
			    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
			    .position(new LatLng(55.7975, -4.023056)));
				
				//Cathkin Braes Country Park Marker
				googlemap.addMarker(new MarkerOptions()
				.title("Cathkin Braes Country Park Marker")
				.snippet("Cathkin Road, Glasgow G45 Cycling")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling))
				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
				.position(new LatLng(55.798605, -4.232141)));
					
				//Hampden Park Marker
				googlemap.addMarker(new MarkerOptions()
				.title("Hampden Park")
				.snippet("Letherby Dr, Glasgow G42 9BA Athletics")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.athletics))
				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
				.position(new LatLng(55.825864, -4.252003)));
					
				//Glasgow Green (Hockey Centre) Marker
				googlemap.addMarker(new MarkerOptions()
				.title("Glasgow Green Hockey Centre")
				.snippet("Glasgow, Glasgow City G40 1HB Hockey")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.hockey))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.844301, -4.235015)));
				
				//National Indoor Sports Arena/Sir Chris Hoy Velodrome Marker
				googlemap.addMarker(new MarkerOptions()
					.title("National Indoor Sports Arena")
					.snippet("1000 London Rd, Glasgow G40 3HY Cycling")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.847222, -4.208042)));
					
				//Barry Buddon Shooting Centre Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Barry Buddon Shooting Centre")
					.snippet("Carnoustie, Angus DD7 Shooting")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.shooting))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.847879, -4.205264)));
					
				//Tollcross Aquatics Centre Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Tollcross Aquatics Centre")
					.snippet("367 Wellshot Rd, Glasgow G32 7QP Aquatics")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.swimming))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.848307, -4.17724)));
					
					//Celtic Park Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Celtic Park")
					.snippet("Celtic Park, Glasgow G40 3RE Opening Ceremony")
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.849722, -4.205556)));
					
				//	//Ibrox Stadium Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Ibrox Stadium")
					.snippet("150 Edmiston Dr, Glasgow, Lanarkshire G51 2XD Rugby")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.rugby))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.853206, -4.309256)));
					
					
					//Scottish Exhibition and Conference Centre Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Scottish Exhibition and Conference Centre")
					.snippet("Exhibition Way, Glasgow G3 8YW Boxing")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.boxing))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.860849, -4.28812)));
					
					//Kelvingrove Lawn Bowls Centre Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Kelvingrove Lawn Bowls Centre")
					.snippet("Kelvin Way, Kelvingrove Park, Glasgow G3 7TA Bowls")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.bowls))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.867666, -4.289163)));
					
					//Scotstoun Leisure Centre Precinct Marker
				googlemap.addMarker(new MarkerOptions()
					.title("Scotstoun Leisure Centre")
					.snippet("72 Danes Dr, Glasgow G14 9HD Squash")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.squash))
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(55.881137, -4.34181)));
				
				//User Position
				if(userPosition != null){
				googlemap.addMarker(new MarkerOptions()
				.title("Your Here")
				.snippet("This is where you are")
				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
				.position(userPosition));}
	}
	
	private void addCityMarkers() {
		
		//Christchurch, New Zealand
		googlemap.addMarker(new MarkerOptions()
		.title("Christchurch, New Zealand")
		.snippet("1974, Scotland Won 19 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_zealand))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(-43.5300, 172.6203)));
		
		//Edmonton Canada
		googlemap.addMarker(new MarkerOptions()
		.title("Edmonton Canada")
		.snippet("1978, Scotland Won 14 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.canada))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(53.5333, -113.5000)));
		
		//Brisbane, Australia
		googlemap.addMarker(new MarkerOptions()
		.title("Brisbane, Australia")
		.snippet("1982, Scotland Won 26 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.australia))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(-27.4679, 153.0278)));
		
		//Edinburgh, Scotland
		googlemap.addMarker(new MarkerOptions()
		.title("Edinburgh, Scotland")
		.snippet("1986, Scotland Won 33 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.scotland))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(55.9531, -3.1889)));
		
		//Auckland, New Zealand
		googlemap.addMarker(new MarkerOptions()
		.title("Auckland, New Zealand")
		.snippet("1990, Scotland Won 22 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_zealand))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(-36.8404, 174.7399)));
		
		//Victoria, Canada
		googlemap.addMarker(new MarkerOptions()
		.title("Victoria, Canada")
		.snippet("1994, Scotland Won 20 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.canada))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(48.4222, -123.3657)));
		
		//Kuala Lumpur, Malaysia
		googlemap.addMarker(new MarkerOptions()
		.title("Kuala Lumpur, Malaysia")
		.snippet("1998, Scotland Won 12 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.malaysia))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(3.1357, 101.6880)));
		
		//Manchester, England
		googlemap.addMarker(new MarkerOptions()
		.title("Manchester, England")
		.snippet("2002, Scotland Won 29 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.england))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(53.4667, -2.2333)));
		
		//Melbourne, Australia
		googlemap.addMarker(new MarkerOptions()
		.title("Melbourne, Australia")
		.snippet("2006, Scotland Won 29 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.australia))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(-37.8136, 144.9631)));
		
		//Delhi, India
		googlemap.addMarker(new MarkerOptions()
		.title("Delhi, India")
		.snippet("2010, Scotland Won 26 Medals")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.india))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng (28.6100, 77.2300)));
		
		//User Position
		if(userPosition != null){
		googlemap.addMarker(new MarkerOptions()
		.title("Your Here")
		.snippet("This is where you are")
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(userPosition));}

		
	}

	//This method is used to change the users location on the googlemap
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		//adds a marker to googlemaps that represents the users location
		googlemap.addMarker(new MarkerOptions()
		.title("Your Location")
		.snippet("This is where you are")
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng (arg0.getLatitude(),arg0.getLongitude())));
		//uses the location arg0 to find the latitude and longitude
		LatLng position = new LatLng(arg0.getLatitude(),arg0.getLongitude());
		userPosition = position;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
    	builder.setMessage(arg0.getSnippet())
    	.setTitle(arg0.getTitle())
    	.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    	AlertDialog dialog = builder.create();
		return false;
	}
	
	//creates a dialog box that user can enter there location or automattically find their location
	public void displayLocationDialog(){
		
		//instantiates new AlertDialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		//sets the message of the dialog
		builder.setMessage("Please Enter Your Location")
		
		//sets the title of the dialog
		.setTitle("Enter Location");
		
		//adds an edittext field to the dialog
		final EditText input = new EditText(MainActivity.this);  
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	                        LinearLayout.LayoutParams.MATCH_PARENT,
	                        LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			builder.setView(input);
			//creates a positive button in the dialog assigning an onclick listener
			builder.setPositiveButton("Auto", new DialogInterface.OnClickListener() {
	        
				public void onClick(DialogInterface dialog, int id) {
					
					//instantiates a location manager and accesses the location services
					LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
					
					//declares string provider to best provider
					String provider = lm.getBestProvider(new Criteria(), true);
					
					//if provider is null then runs the onProviderDisabled()
					if (provider ==null) {
						onProviderDisabled(provider);
					}
					//finds the last known location from the provider
					Location loc = lm.getLastKnownLocation(provider);
					
					//sends location to onLocationChanged()
					if (loc != null) {
						onLocationChanged(loc);
					}
					//closes dialog
	            dialog.dismiss();
	            
	            }
			}
			);
			//adds a neutral button to the dialog and add onclick listener
			builder.setNeutralButton("Enter", new DialogInterface.OnClickListener() {
		        
				public void onClick(DialogInterface dialog, int id) {
					
					//declares new location variable l
					Location l = new Location("position");
					//declares address as text from edittext box
					String address = input.getText().toString();
					
					Log.e("error","enter button clicked");
					//commented code is failed attempt at using JSON to find location
					//UseJSON uj = new UseJSON();
					//Log.e("error",address);
					//LatLng position = uj.getPosition(address);
					//Log.e("error", "retrieved position");
					//l.setLatitude(position.latitude);
					//l.setLongitude(position.longitude);
					//onLocationChanged(l);
					
					//trys to use geocoder to find location from address
					try
	                {
						//instantiates mew geocoder
						Geocoder geocoder = new Geocoder(MainActivity.this);
						//declares Address List
						List<Address> addresses = null;
						
						int i=0;
						//while loop attempts to use geocoder to find location
						//sends location to onLocationChanged()
						//and closes after 11 attempts
						while(addresses==null||i<11){
							i++;
							
	                    addresses = geocoder.getFromLocationName(address, 1);
	                    if (addresses!=null&&addresses.size()>0){
	                    	Address add = addresses.get(0);
	                    	l.setLatitude(add.getLatitude());
	                    	l.setLongitude(add.getLongitude());
	                    	onLocationChanged(l);
	                    	dialog.dismiss();
	                    }
	                    else {
	                    	Log.e("error", "geocoder connection attempt fail "+i);
	                    }
						}
	                }

	                catch (IOException e)
	                {
	                    e.printStackTrace();
	                }
					
	            
	            }

			}
	    );
			
			
			
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
}

