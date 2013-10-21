package com.henrik.bak.whereisthenearest.places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.henrik.bak.whereisthenearest.model.Place;
import com.henrik.bak.whereisthenearest.model.PlaceDetails;
import com.henrik.bak.whereisthenearest.model.PlacesList;

public class PlaceRequest {

	static List<Double> latList = new ArrayList<Double>();
	static List<Double> lngList = new ArrayList<Double>();

	// Create our transport.
	private static final HttpTransport transport = new ApacheHttpTransport();
	// The different Places API endpoints.
	// order of data in requested URL doesn'n matter
	private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	private static final String PLACE_ADD_URL = "https://maps.googleapis.com/maps/api/place/add/json?key=YOUR KEY HERE&sensor=false"; 
	private static final String PLACES_DELETE_URL = "https://maps.googleapis.com/maps/api/place/delete/json?key=YOUR KEY HERE&sensor=false";

	// Fill in the API key you want to use.
	private static final String API_KEY = "AIzaSyBOgUCoQIV8PAvWWZEHiHuAmM9y4IvY1U4";
	static final String LOG_KEY = "GooglePlace";

	static ArrayList<String> placeReference =  new ArrayList<String>();

	// converting back to -33.8670522, 151.1957362 format
	double latitude = 46.2508925602;
	double longitude = 20.1597201082;

	// Sydney, Australia
	//double latitude = -33.8670522;
	//double longitude = 151.1957362;

	// telenet
	//double latitude = 51.034823;
	//double longitude = 4.483774;


	public PlacesList performSearch(String param) throws Exception {
		try {
			Log.v(LOG_KEY, "Start Search...");
			GenericUrl reqUrl = new GenericUrl(PLACES_SEARCH_URL);
			reqUrl.put("key", API_KEY);
			reqUrl.put("location", latitude + "," + longitude);
			reqUrl.put("radius", 5000); // radius of 5Km 
			reqUrl.put("types", param);
			reqUrl.put("sensor", "false");
			reqUrl.put("rankby","distance");
			Log.v(LOG_KEY, "Requested URL= " + reqUrl);
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(reqUrl);

			Log.v(LOG_KEY, request.execute().parseAsString());							
			PlacesList places = request.execute().parseAs(PlacesList.class);
			Log.v(LOG_KEY, "STATUS = " + places.getStatus());
			// empty array lists
			latList.clear();
			lngList.clear();
			for (Place place : places.getResults()) {
				Log.v(LOG_KEY, place.getName());	
				latList.add(place.geometry.getLocation().getLat());
				lngList.add(place.geometry.getLocation().getLng());	
				// assign last added place reference 
				placeReference.add(place.getReference());

			}			       
			return places;

		} catch (HttpResponseException e) {
			Log.v(LOG_KEY, e.getResponse().parseAsString());
			throw e;
		}

		catch (IOException e) {
			// TODO: handle exception
			throw e;
		}
	}

	public PlaceDetails performDetails(String reference) throws Exception { 
		try {
			Log.v(LOG_KEY, "Perform Place Detail....");
			GenericUrl reqUrl = new GenericUrl(PLACES_DETAILS_URL);
			reqUrl.put("key", API_KEY);
			reqUrl.put("reference", reference);
			reqUrl.put("sensor", "false");
			Log.v(LOG_KEY, "Requested URL= " + reqUrl);
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(reqUrl);

			Log.v(LOG_KEY, request.execute().parseAsString());	
			PlaceDetails placeDetail = request.execute().parseAs(PlaceDetails.class);

			return placeDetail;

		} catch (HttpResponseException e) {
			Log.v(LOG_KEY, e.getResponse().parseAsString());
			throw e;
		}
		catch (IOException e) {	
			// TODO: handle exception
			throw e;
		}
	}

	public JSONObject addPlace(double lat, double lng, String type, String name) throws Exception {
		try {
			Log.v(LOG_KEY, "Adding Place...");
			String vic = "5/48 Pirrama Road, Pyrmont";
			String formtd_address = "5/48 Pirrama Road, Pyrmont NSW, Australia";
			String formtd_phone_number = "(02) 9374 4000";
			String myUrl = "http://maps.google.com/maps/place?cid=10281119596374313554";
			String myWebsite = "http://www.google.com.au/";

			HttpPost post = new HttpPost(PLACE_ADD_URL);
			String postBody = 
					"{"+
							"\"location\": {" +
							"\"lat\": " + lat + "," +
							"\"lng\": " + lng +
							"}," + 
							"\"accuracy\":50.0," +
							"\"name\": \"" + name + "\"," +
							"\"types\": [\"" + type + "\"]," +
							"\"vicinity\":\""+ vic +"\","+
							"\"formatted_address\":\""+ formtd_address +"\","+
							"\"formatted_phone_number\":\""+ formtd_phone_number +"\","+
							"\"url\":\""+ myUrl +"\","+
							"\"website\":\""+ myWebsite +"\","+  
							"\"language\": \"en\" " +

					      "}"; 

			StringEntity se = new StringEntity(postBody,HTTP.UTF_8);
			post.setEntity(se);
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody = new DefaultHttpClient().execute(post, responseHandler);
			JSONObject response = new JSONObject(responseBody);
			Log.v(LOG_KEY, "Requested URL= " + PLACE_ADD_URL); 

			return response;

		} catch (HttpResponseException e) {
			Log.v(LOG_KEY, e.getResponse().parseAsString());
			throw e;
		}

		catch (IOException e) {
			// TODO: handle exception
			throw e;
		}
	}  

	public JSONObject deletePlace(String reference) throws Exception { 
		try {
			Log.v(LOG_KEY, "Deleting Place...");
			HttpPost post = new HttpPost(PLACES_DELETE_URL);		 
			String postBody = "{\"reference\":\""+ reference +"\"}";
			StringEntity se = new StringEntity(postBody,HTTP.UTF_8);
			post.setEntity(se);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = new DefaultHttpClient().execute(post, responseHandler);
			JSONObject response = new JSONObject(responseBody);
			Log.v(LOG_KEY, "Requested URL= " + PLACES_DELETE_URL); 

			return response;

		} catch (HttpResponseException e) {
			Log.v(LOG_KEY, e.getResponse().parseAsString());
			throw e;
		}
		catch (IOException e) {	
			// TODO: handle exception
			throw e;
		}

	}


	public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {

		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Google-Places-DemoApp");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory()) ;
				//JsonHttpParser.builder(new JacksonFactory());
				//parser.jsonFactory = new JacksonFactory();
				request.addParser(parser);
			}
		});
	}

}
