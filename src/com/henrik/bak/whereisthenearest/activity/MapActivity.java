package com.henrik.bak.whereisthenearest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.henrik.bak.whereisthenearest.R;
import com.henrik.bak.whereisthenearest.model.Place;
import com.henrik.bak.whereisthenearest.model.PlacesList;
import com.henrik.bak.whereisthenearest.places.PlaceRequest;

public class MapActivity extends Activity {

	private final String TAG = getClass().getSimpleName();
	private GoogleMap mMap;
	private String queryString;

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
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
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
						
			mMap.addMarker(new MarkerOptions()
			.title("asd")
			.position(new LatLng(46.2508925602, 20.1597201082)));
			
			for (Place place : result.getResults()) {
					mMap.addMarker(new MarkerOptions()
					.title(place.getName())
					.position(new LatLng(place.geometry.getLocation().getLat(),
							place.geometry.getLocation().getLng()))
							.snippet(place.getVicinity()));

			}

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


}
