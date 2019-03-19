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
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import no.usn.plastplukk.plastplukk.R;

public class ChooseCategoryActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_reg);

    }

    public void velgEgenskaper(View view) {
        String kategori = view.getTag().toString();
        Intent nyIntent = new Intent(this.getBaseContext(), SetAttributesActivity.class);

        // Shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("Kategori", kategori);
        editor.apply();

        this.startActivity(nyIntent);
    }
}
