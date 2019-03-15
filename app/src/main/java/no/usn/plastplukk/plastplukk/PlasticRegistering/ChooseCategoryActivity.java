package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import no.usn.plastplukk.plastplukk.R;

public class ChooseCategoryActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_reg);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }
        trackLocation();

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    trackLocation();
                return;
        }
    }

    private void trackLocation() {
        locationManager.requestLocationUpdates("gps", 2000, 10, locationListener);
    }

    public void velgEgenskaper(View view) {
        String kategori = view.getTag().toString();
        Intent nyIntent = new Intent(this.getBaseContext(), SetAttributesActivity.class);

        Location lastKnowLocation = locationManager.getLastKnownLocation("gps");
        String latitude = ""+lastKnowLocation.getLatitude();
        String longitude = ""+lastKnowLocation.getLongitude();

        Log.e("Test", latitude);


        // Shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("Kategori", kategori);
        editor.putString("Latitude", latitude);
        editor.putString("Longitude", longitude);
        editor.apply();

        this.startActivity(nyIntent);
    }
}
