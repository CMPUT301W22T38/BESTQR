package com.example.bestqr;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapQRCodes {
    private ArrayList<QR_CODE> nearbyCodes;

    public void addNearbyCode(QR_CODE qrCode){
        nearbyCodes.add(qrCode);
    }

    public void plotLocations(){
        // this method gives the visual representation of the map
    }

}
