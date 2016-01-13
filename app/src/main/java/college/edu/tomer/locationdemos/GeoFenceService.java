package college.edu.tomer.locationdemos;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeoFenceService extends IntentService {

    public GeoFenceService() {
        super("GeoFenceService");
    }

    protected void onHandleIntent(Intent intent) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                Log.d("T", "Error");
                return;
            }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("In College").setContentText("Yay!").setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(0, notification);



        // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

                //Use this two variables:
                /*geofenceTransition;
                        triggeringGeofences;

                  */
            }
        }
}