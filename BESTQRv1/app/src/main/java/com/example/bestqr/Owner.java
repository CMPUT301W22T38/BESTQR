package com.example.bestqr;

import com.example.bestqr.Database.Database;
import com.example.bestqr.Database.EntryExist;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;

import java.util.ArrayList;

public class Owner extends Profile {
    public ArrayList<Profile> allPlayers;
    public ArrayList<QRCODE> allStoredQrCodes;

    public Owner(){
        super(Database.getOwnerId());
    }

    /**
     * This method removes a QRCODE of the owners choice
     * @param qrCode - the QRCODE to be removed from the game
     */
    public void removeQrCode(QRCODE qrCode){
        Database.removeAllQRCode(qrCode);
    }

    /**
     * This method removes a player of the owners choice
     * @param player - the player to be removed from the game
     */
    public void removePlayer(Profile player){
       if (EntryExist.isRegistered(player.getAndroidId())){
            Database.deletePlayer(player);

       }
    }


}
