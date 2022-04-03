package com.example.bestqr.models;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getGeoHash() {
        return geoHash;
    }

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
