package com.example.bestqr;

import com.example.bestqr.models.QRCODE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class QRCodeList extends ArrayList<QRCODE> {
    public int getTotalScore() {
        return this.stream().collect(Collectors.summingInt(QRCODE::getScore));
    }

    public QRCODE getHighest() {
        return Collections.max(this, Comparator.comparing(s -> s.getScore()));
    }


}
