package com.henrik.bak.whereisthenearest.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.henrik.bak.whereisthenearest.R;
import com.henrik.bak.whereisthenearest.util.AlertDialogManager;
import com.henrik.bak.whereisthenearest.util.ConnectionDetector;
import com.henrik.bak.whereisthenearest.util.GPSTracker;

public class MainMenuActivity extends Activity {

	ImageButton btn_food;
	ImageButton btn_bar;
	ImageButton btn_hospital;
	ImageButton btn_atm;
	
	// flag for Internet connection status
    Boolean isInternetPresent = false;
 
    // Connection detector class
    ConnectionDetector cd;
     
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
 
    // GPS Location
    GPSTracker gps;
    
    // Progress dialog
    ProgressDialog pDialog;
     
    // ListItems data
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
     
 // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		btn_food = (ImageButton) findViewById(R.id.btn_food);
		btn_bar = (ImageButton) findViewById(R.id.btn_bar);
		btn_hospital = (ImageButton) findViewById(R.id.btn_hospital);
		btn_atm = (ImageButton) findViewById(R.id.btn_atm);
		
		cd = new ConnectionDetector(getApplicationContext());
		 
        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MainMenuActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
 
        // creating GPS Class object
        gps = new GPSTracker(this);
 
        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(MainMenuActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            gps.showSettingsAlert();
            // stop executing code by return
            return;
        }
		
		initOnClickListeners();
	}
	
	private void initOnClickListeners() {
		
		btn_food.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainMenuActivity.this, MapActivity.class);
				i.putExtra("type", "food");
				startActivity(i);
			}
		});
		
		btn_bar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainMenuActivity.this, MapActivity.class);
				i.putExtra("type", "bar|cafe");
				startActivity(i);
				
			}
		});
		
		btn_hospital.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainMenuActivity.this, MapActivity.class);
				i.putExtra("type", "hospital");
				startActivity(i);
				
			}
		});
		
		btn_atm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainMenuActivity.this, MapActivity.class);
				i.putExtra("type", "atm|bank");
				startActivity(i);
				
			}
		});
		
	}
	
}
