package com.example.bestqr.ui.map;

import androidx.annotation.NonNull;

import com.example.bestqr.Database;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NearbyCodes {

    // Find cities within 50km of London
    final LatLng userLatLng;
    final GeoLocation center = new GeoLocation(51.5074, 0.1278);
    final double radiusInM = 10 * 1000;
    List<DocumentSnapshot> matchingDocs;

    public NearbyCodes(LatLng userLatLng) {
        this.userLatLng = userLatLng;
    }

    // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
    // a separate query for each pair. There can be up to 9 pairs of bounds
    // depending on overlap, but in most cases there are 4.
    List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
    final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
    for (GeoQueryBounds b : bounds) {
        Query q = Database.collection("cities")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash);

        tasks.add(q.get());
    }

    // Collect all the query results together into a single list
    Tasks.whenAllComplete(tasks)
            .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                @Override
                public void onComplete(@NonNull Task<List<Task<?>>> t) {
                    matchingDocs = new ArrayList<>();

                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            double lat = doc.getDouble("lat");
                            double lng = doc.getDouble("lng");

                            // We have to filter out a few false positives due to GeoHash
                            // accuracy, but most will match
                            GeoLocation docLocation = new GeoLocation(lat, lng);
                            double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                            if (distanceInM <= radiusInM) {
                                matchingDocs.add(doc);
                            }
                        }
                    }

            // matchingDocs contains the results
            // ...
        }
    });

    public List<DocumentSnapshot> getNearbyCodeList() {
        return matchingDocs;
    }
}
