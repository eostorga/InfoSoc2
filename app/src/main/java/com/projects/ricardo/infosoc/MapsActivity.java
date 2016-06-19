package com.projects.ricardo.infosoc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


/*
 * Hace el trabajo pesado. El servicio MapsActivityService hace UNA sola invocación a esta clase.
 * A partir de ahí, esta clase sigue funcionando con los parámetros INTERVAL y FASTEST_INTERVAL.
 */

public class MapsActivity extends Activity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long  SEGUNDOS = 10;
    private static final long INTERVAL = 1000 * SEGUNDOS;
    private static final long FASTEST_INTERVAL = 1000 * SEGUNDOS;
    Button btnEndR;
    //TextView horaValue, latitudValue, longitudValue, presicionValue, rutaMSG;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    CoordinatesPath coopat;


    /*
     * Inicializa el objeto LocationRequest.
     */
    protected void createLocationRequest() {
        coopat = new CoordinatesPath();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /*
     * Cuando el servicio MapsActivityService llama a esta clase, se ejecuta este de acá.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ...............................");


        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_ruta_datos); /*
        horaValue = (TextView) findViewById(R.id.horaValue);
        latitudValue = (TextView) findViewById(R.id.latitudValue);
        longitudValue = (TextView) findViewById(R.id.longitudValue);
        presicionValue = (TextView) findViewById(R.id.presicionValue);
        rutaMSG = (TextView) findViewById(R.id.rutamsg);*/

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        //rutaMSG.startAnimation(anim);

        /*btnFusedLocation = (Button) findViewById(R.id.btn);
        btnFusedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                updateUI();
            }
        });*/

        btnEndR = (Button) findViewById(R.id.btnEndRoute);
        btnEndR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();

                Log.d(TAG, "onBackPressed fired ..............");
                mGoogleApiClient.disconnect();
                Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.d(TAG, "onStop fired ..............");
        //mGoogleApiClient.disconnect();
        //Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    /*
     * Este es el más importante de todos. Cada vez que pasan FASTEST_INTERVAL segundos, se llama este
     * método.
     */
    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            String pre = String.valueOf((int)mCurrentLocation.getAccuracy()) + " m";

            /*horaValue.setText(""+mLastUpdateTime);
            latitudValue.setText(lat);
            longitudValue.setText(lng);
            presicionValue.setText(pre);*/

            //Las mediciones que he hecho son bastante bastante constantes.
            //Al aire libre, la presición que más predomina es de 5m.
            //Hay algunas mediciones de 10m, pero, para dejar un margen más permisivo, lo
            //dejaré en 20m. Just because...
            if(mCurrentLocation.getAccuracy()>20) {
                //presicionValue.setTextColor(Color.rgb(255, 0, 0));
            }
            else {
                //presicionValue.setTextColor(Color.rgb(51, 204, 51));
                //latitud, longitud, hora, precisión
                coopat.crear_camino(lat + "," + lng + ","+mLastUpdateTime+", "+ pre);
            }


        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(getBaseContext(), MapsActivityService.class));
        super.onBackPressed();
    }
}