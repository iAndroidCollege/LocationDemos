package college.edu.tomer.locationdemos;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    @Bind(R.id.galleryPager)
    ViewPager galleryPager;
    private GoogleMap mMap;
    private static final int REQUEST_MAP = 0;
    private GoogleApiClient mApiClient;
    private LocationRequest mRequest;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        initGallery();
        addGeoFences();
        SupportMapFragment mapFragment = new SupportMapFragment();
        mRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000).
                        setFastestInterval(1000)
        ;

        addGeoFences();

        getSupportFragmentManager().
                beginTransaction().
                add(R.id.mapLayout, mapFragment).
                commit();
        mapFragment.getMapAsync(this);
    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeoFenceService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private void initGallery() {
        ArrayList<String> mImages = new ArrayList<>();
        mImages.add("http://121clicks.com/wp-content/uploads/2012/02/pawel_klarecki_07.jpg");
        mImages.add("http://s1.favim.com/orig/18/deviantart-landscape-lonely-pink-promise-quote-Favim.com-196903.jpg");
        mImages.add("http://gdj.gdj.netdna-cdn.com/wp-content/uploads/2012/08/landscape-replection-photography-5.jpg");
        galleryPager.setAdapter(
                new GalleryPagerAdapter(
                        getSupportFragmentManager(),
                        mImages)
        );
    }


    void addGeoFences(){
         mGeofenceList =  new ArrayList<>();

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("ir atika")

                .setCircularRegion(
                        31.246216,34.8045161,

                        100
                )
                .setExpirationDuration(
                        1000*60*60*24)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mApiClient != null)
            mApiClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_MAP);
        } else {
            initApiClient();
        }
    }

    private void initApiClient() {
        mApiClient =
                new GoogleApiClient.Builder(this, this, this).
                        addApi(LocationServices.API).
                        build();
        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapsActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mRequest, this);


        LocationServices.GeofencingApi.addGeofences(
                mApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.
                    getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
            if (addressList != null) {
                for (Address address : addressList) {
                    Toast.makeText(MapsActivity.this, address.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.animateCamera(
                CameraUpdateFactory.
                        newLatLngZoom(
                                new LatLng(
                                        location.getLatitude(),
                                        location.getLongitude()),
                                17));

        getAddressFromLocation(location);
    }

    @Override
    public void onResult(Status status) {

    }
}
