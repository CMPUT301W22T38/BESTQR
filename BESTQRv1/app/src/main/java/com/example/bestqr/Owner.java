package com.example.bestqr;

import java.util.ArrayList;

public class Owner {
    public ArrayList<Profile> allPlayers;
    public ArrayList<QR_CODE> allStoredQrCodes;

    public void removeQrCode(QR_CODE qrCode){
        allStoredQrCodes.remove(qrCode);
    }

    public void removePlayer(Profile player){
        allPlayers.remove(player);
    }


}
