package com.example.bestqr;

import java.util.ArrayList;

public class MapQRCodes {
    private ArrayList<QRCODE> nearbyCodes;

    public void addNearbyCode(QRCODE qrCode){
        nearbyCodes.add(qrCode);
    }

    public void plotLocations(){
        // this method gives the visual representation of the map
    }

}
