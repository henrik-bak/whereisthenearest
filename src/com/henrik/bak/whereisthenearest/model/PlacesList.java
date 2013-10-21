package com.henrik.bak.whereisthenearest.model;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class PlacesList implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Key
	private String status;

	@Key
	private List<Place> results;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Place> getResults() {
		return results;
	}

	public void setResults(List<Place> results) {
		this.results = results;
	}

}