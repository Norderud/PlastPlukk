package no.usn.plastplukk.plastplukk.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MAIN_CATEGORY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.TYPE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.SIZE;

public class AreaActivity extends AppCompatActivity {

    String kategori, type, størrelse;
    int antChecked;
    boolean[] checkSvar;
    CheckBox fjellCheck, skogCheck, elvCheck, kystCheck, innsjøCheck, veiCheck,
            industriHandelCheck, skoleFritidCheck, dyrketMarkLandbrukCheck, boligCheck;
    TextView feilMelding;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_area);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        kategori = prefs.getString(MAIN_CATEGORY, "Ingen");
        type = prefs.getString(TYPE, "Ingen");
        størrelse = prefs.getString(SIZE, størrelse);
        feilMelding = findViewById(R.id.Feilmelding);
        checkSvar = new boolean[10];
        antChecked = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkOnReturn();
    }

    // Åpner neste intent
    public void openKamera(View view) {
        boolean anyAreaChosen = false;
        Intent messageIntent = new Intent(this, PhotoActivity.class);
        for (int i = 0; i < checkSvar.length; i++) {
            if (checkSvar[i])
                anyAreaChosen = true;
                startActivity(messageIntent);
        }
        if (!anyAreaChosen)
            toastError(getString(R.string.velg_minst_en_område));
    }

    private void toastError(String message) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show();
    }

    //Sjekker hvilke som er checked, lagrer disse i en array + sharedprefs
    public void checkBoxes(View view) {
        if(((CheckBox) view).isChecked()){
            antChecked++;
        } else {
            antChecked--;
        }
        if(antChecked > 3){
            toastError(getString(R.string.kun_velge_3));
            ((CheckBox) view).setChecked(false);
            antChecked--;
            return;
        }
        switch (view.getId()) {
            case R.id.fjellCheck:
                checkSvar[0] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.skogCheck:
                checkSvar[1] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.elvCheck:
                checkSvar[2] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.kystCheck:
                checkSvar[3] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.innsjøCheck:
                checkSvar[4] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.veiCheck:
                checkSvar[5] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.industriHandelCheck:
                checkSvar[6] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.skoleFritidCheck:
                checkSvar[7] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.dyrketMarkLandbrukCheck:
                checkSvar[8] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
            case R.id.boligCheck:
                checkSvar[9] = ((CheckBox) view).isChecked();
                storeArray(checkSvar, "Checksvar", this);
                break;
        }
    }

    // Checker de boksene som tidligere var checked
    public void checkOnReturn() {
        boolean[] array = loadArray("Checksvar", prefs);
        if (array.length == 0)
            return;
        checkSvar = array;
        fjellCheck = findViewById(R.id.fjellCheck);
        fjellCheck.setChecked(checkSvar[0]);
        skogCheck = findViewById(R.id.skogCheck);
        skogCheck.setChecked(checkSvar[1]);
        elvCheck = findViewById(R.id.elvCheck);
        elvCheck.setChecked(checkSvar[2]);
        kystCheck = findViewById(R.id.kystCheck);
        kystCheck.setChecked(checkSvar[3]);
        innsjøCheck = findViewById(R.id.innsjøCheck);
        innsjøCheck.setChecked(checkSvar[4]);
        veiCheck = findViewById(R.id.veiCheck);
        veiCheck.setChecked(checkSvar[5]);
        industriHandelCheck = findViewById(R.id.industriHandelCheck);
        industriHandelCheck.setChecked(checkSvar[6]);
        skoleFritidCheck = findViewById(R.id.skoleFritidCheck);
        skoleFritidCheck.setChecked(checkSvar[7]);
        dyrketMarkLandbrukCheck = findViewById(R.id.dyrketMarkLandbrukCheck);
        dyrketMarkLandbrukCheck.setChecked(checkSvar[8]);
        boligCheck = findViewById(R.id.boligCheck);
        boligCheck.setChecked(checkSvar[9]);

        for(boolean b: checkSvar){
            if(b){
                antChecked++;
            }
        }
    }

    // Lagrer checksvar arrayen som unike boolverdier i sharedprefs
    public boolean storeArray(boolean[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    // Returnerer lagret bool verdier som en array
    public static boolean[] loadArray(String arrayName, SharedPreferences prefs) {
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }


}
