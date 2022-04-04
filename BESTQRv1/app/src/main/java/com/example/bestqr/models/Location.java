package com.example.bestqr.models;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Location {
    private double latitude;
    private double longitude;
    private String geoHash;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));
    }

    /**
     * the latitude of the location
     * @return the latitude of the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * the longitude of the location
     * @return the longitude of the location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * the geohash of the location
     * @return - The Geohash of the location
     */
    public String getGeoHash() {
        return geoHash;
    }


    /**
     * the hashmap of the location information
     * @return - The hashmap object that contains the info of the location object
     */
    public HashMap toMap() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> location = new HashMap<>();

        result.put("geohash", this.getGeoHash());
        result.put("coordinates", location);
        location.put("longitude", this.longitude);
        location.put("latitude", this.latitude);

        return result;
    }


}
