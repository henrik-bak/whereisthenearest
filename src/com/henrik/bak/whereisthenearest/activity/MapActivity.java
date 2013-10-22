package com.henrik.bak.whereisthenearest.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.henrik.bak.whereisthenearest.R;
import com.henrik.bak.whereisthenearest.model.Place;
import com.henrik.bak.whereisthenearest.model.PlaceDetails;
import com.henrik.bak.whereisthenearest.model.PlacesList;
import com.henrik.bak.whereisthenearest.places.PlaceRequest;

public class MapActivity extends FragmentActivity {

	private GoogleMap mMap;
	private String queryString;

	private Button btnDetails;
	private Button btnRoute;
	private Map<Marker, String> markerMap;
	private String reference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		if (savedInstanceState == null) {
		    Bundle extras = getIntent().getExtras();
		    if(extras == null) {
		    	queryString= null;
		    } else {
		    	queryString= extras.getString("type");
		    }
		} else {
			queryString= (String) savedInstanceState.getSerializable("type");
		}
		
		initCompo();
		new SearchSrv(MapActivity.this, queryString).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	private void initCompo() {
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setOnMarkerClickListener(getOnMarkerClickListener());
		btnDetails = (Button) findViewById(R.id.btnDetails);
		btnRoute = (Button) findViewById(R.id.btnRoute);
		initOnClickHandlers();
	}
	
	private OnMarkerClickListener getOnMarkerClickListener()
	{
	    return new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				reference = markerMap.get(arg0);
				return false;
			}
		};      
	    	
	}


	private void initOnClickHandlers() {
		btnDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!reference.isEmpty()) {
					new DetailSrv(MapActivity.this, reference).execute();
				}
				
			}
		});
		
		btnRoute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}


	private class SearchSrv extends AsyncTask<Void, Void, PlacesList>{

		private ProgressDialog dialog;
		private Context context;
		private String param;

		public SearchSrv(Context context, String param) {
			this.context = context;
			this.param = param;
		}

		@Override
		protected PlacesList doInBackground(Void... params) {
			PlacesList pl = null;
			try {
				// send place search request from here
				pl = new PlaceRequest().performSearch(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pl;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Loading..");
			dialog.isIndeterminate();
			dialog.show();
		}

		@Override
		protected void onPostExecute(PlacesList result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			LatLng testStart =  new LatLng(46.2508925602, 20.1597201082);
						
			mMap.addMarker(new MarkerOptions()
			.title("Your current location")
			.position(testStart));
			
			markerMap = new HashMap<Marker, String>();
			
			for (Place place : result.getResults()) {
					Marker m = mMap.addMarker(new MarkerOptions()
					.title(place.getName())
					.position(new LatLng(place.geometry.getLocation().getLat(),
							place.geometry.getLocation().getLng()))
							.snippet(place.getVicinity()));
					markerMap.put(m, place.getReference());
			}
			
			//new Routing(context,mMap, Color.parseColor("#ff0000")).execute(testStart, nearestEndPoint);
			

			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(result.getResults().get(0).geometry.getLocation().getLat(),
					result.getResults().get(0).geometry.getLocation().getLng())) // Sets the center of the map to
					// Mountain View
					.zoom(14) // Sets the zoom
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
					mMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
		}
	}	// End of class SearchSrv here
	
	
	private class DetailSrv extends AsyncTask<Void, Void, PlaceDetails>{

		private ProgressDialog dialog;
		private Context context;
		private String param;

		public DetailSrv(Context context, String param) {
			this.context = context;
			this.param = param;
		}

		@Override
		protected PlaceDetails doInBackground(Void... params) {
			PlaceDetails details = null;
			try {
				// send place search request from here
				details = new PlaceRequest().performDetails(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return details;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Loading..");
			dialog.isIndeterminate();
			dialog.show();
		}

		@Override
		protected void onPostExecute(PlaceDetails result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			Log.d("MAUNIKA", result.toString());
		}
	}	// End of class SearchSrv here


}
