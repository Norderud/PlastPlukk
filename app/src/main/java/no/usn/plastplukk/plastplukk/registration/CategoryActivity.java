package no.usn.plastplukk.plastplukk.registration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MAIN_CATEGORY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MOBILDATAVARSEL;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.SIZE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.TYPE;

public class CategoryActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("");
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE
                    && !prefs.getBoolean(MOBILDATAVARSEL, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.mobilDataVarsel)
                        .setPositiveButton(R.string.ok, null)
                        .setNegativeButton("Ikke vis dette igjen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putBoolean(MOBILDATAVARSEL, true);
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
        Intent nyIntent = new Intent(this.getBaseContext(), AttributesActivity.class);

        if (!prefs.getString(MAIN_CATEGORY, "Ingen").equals(kategori)){
            editor.putString(MAIN_CATEGORY, kategori);
            editor.remove(TYPE);
            editor.remove(SIZE);
            editor.apply();
        }

        this.startActivity(nyIntent);
    }
}
