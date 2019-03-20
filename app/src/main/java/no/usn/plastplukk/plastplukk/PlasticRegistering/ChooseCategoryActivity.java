package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import no.usn.plastplukk.plastplukk.R;

public class ChooseCategoryActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_reg);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE
                    && !prefs.getBoolean("mobilDataVarslet", false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.mobilDataVarsel)
                        .setPositiveButton(R.string.ok, null)
                        .setNegativeButton("Ikke vis dette igjen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putBoolean("mobilDataVarslet", true);
                                editor.apply();
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

    public void velgEgenskaper(View view) {
        String kategori = view.getTag().toString();
        Intent nyIntent = new Intent(this.getBaseContext(), SetAttributesActivity.class);

        // Shared preferences
        editor.putString("Kategori", kategori);
        editor.apply();

        this.startActivity(nyIntent);
    }
}
