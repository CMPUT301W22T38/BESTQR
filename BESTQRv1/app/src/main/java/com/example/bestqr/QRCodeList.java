package com.example.bestqr;

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

    public void ascendingSort(){
        Collections.sort(this, new Comparator<QRCODE>() {
            @Override
            public int compare(QRCODE qrcode, QRCODE t1) {
                return qrcode.getScore() - t1.getScore();
            }
        });
    }

    public void descendingSort(){
        Collections.sort(this, new Comparator<QRCODE>() {
            @Override
            public int compare(QRCODE qrcode, QRCODE t1) {
                return  t1.getScore() - qrcode.getScore();
            }
        });
    }

    public void chronologicalSort() {
        Collections.sort(this, new Comparator<QRCODE>() {
            @Override
            public int compare(QRCODE qrcode, QRCODE t1) {
                return qrcode.getTimestamp().getTimeStamp().compareTo(t1.getTimestamp().getTimeStamp());
            }
        });
    }

}
