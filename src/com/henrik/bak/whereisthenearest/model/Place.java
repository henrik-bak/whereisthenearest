package com.henrik.bak.whereisthenearest.model;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class Place implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Key
	private String id;

	@Key
	private String name;

	@Key
	private String reference;

	@Key
	private String types[];

	@Key
	private String international_phone_number;

	@Key
	private String vicinity;

	@Key
	private String formatted_address;

	@Key
	private String url;

	@Key
	private Double rating;

	@Key
	private String website;

	@Key
	private List<Place> address_components;

	@Key
	private String long_name;

	@Key
	private String short_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getInternational_phone_number() {
		return international_phone_number;
	}

	public void setInternational_phone_number(String international_phone_number) {
		this.international_phone_number = international_phone_number;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<Place> getAddress_components() {
		return address_components;
	}

	public void setAddress_components(List<Place> address_components) {
		this.address_components = address_components;
	}

	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public PlaceGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(PlaceGeometry geometry) {
		this.geometry = geometry;
	}

	@Key
	public PlaceGeometry geometry;

	public static class PlaceGeometry {
		@Key
		private Location location;

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
		
	}

	public static class Location {
		
		@Key
		private double lat;

		@Key
		private double lng;
		
		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}
	}

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
}