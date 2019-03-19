package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import no.usn.plastplukk.plastplukk.R;

public class ChooseAreaActivity extends AppCompatActivity {

    String kategori, underKategori, størrelse;
    boolean[] checkSvar;
    CheckBox fjellCheck, skogCheck, elvCheck, kystCheck, innsjøCheck, veiCheck,
    industriHandelCheck, skoleFritidCheck, dyrketMarkLandbrukCheck, boligCheck;
    TextView feilMelding;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        kategori =  prefs.getString("Kategori", "Ingen");
        underKategori = prefs.getString("Underkategori", "Ingen");
        størrelse = prefs.getString("Størrelse", størrelse);
        feilMelding = findViewById(R.id.Feilmelding);
        checkSvar = new boolean[10];
    }

    @Override
    protected void onResume() {
        checkOnReturn();
        super.onResume();
    }

    // Åpner neste intent
    public void openKamera(View view){
        boolean check = false;
        for (int i=0; i<checkSvar.length; i++) {
            if (checkSvar[i])
                check = true;
        }

        if (!check) {
            feilMelding.setText(getString(R.string.velg_minst_en_område));
            return;
        }

        Intent messageIntent = new Intent(this, PhotoUploadActivity.class);
        startActivity(messageIntent);
    }

    //Sjekker hvilke som er checked, lagrer disse i en array + sharedprefs
    public void checkBoxes(View view){
        switch(view.getId()) {
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
    public void checkOnReturn(){
        boolean[] array = loadArray("Checksvar", prefs);
        if (array.length == 0)
            return;
        checkSvar = array;

        if (checkSvar[0]) {
            fjellCheck = findViewById(R.id.fjellCheck);
            fjellCheck.setChecked(true);
        } if (checkSvar[1]) {
            skogCheck = findViewById(R.id.skogCheck);
            skogCheck.setChecked(true);
        } if (checkSvar[2]) {
            elvCheck = findViewById(R.id.elvCheck);
            elvCheck.setChecked(true);
        } if (checkSvar[3]) {
            kystCheck = findViewById(R.id.kystCheck);
            kystCheck.setChecked(true);
        } if (checkSvar[4]) {
            innsjøCheck = findViewById(R.id.innsjøCheck);
            innsjøCheck.setChecked(true);
        } if (checkSvar[5]) {
            veiCheck = findViewById(R.id.veiCheck);
            veiCheck.setChecked(true);
        }if (checkSvar[6]) {
            industriHandelCheck = findViewById(R.id.industriHandelCheck);
            industriHandelCheck.setChecked(true);
        }if (checkSvar[7]) {
            skoleFritidCheck = findViewById(R.id.skoleFritidCheck);
            skoleFritidCheck.setChecked(true);
        }if (checkSvar[8]) {
            dyrketMarkLandbrukCheck = findViewById(R.id.dyrketMarkLandbrukCheck);
            dyrketMarkLandbrukCheck.setChecked(true);
        }if (checkSvar[9]) {
            boligCheck = findViewById(R.id.boligCheck);
            boligCheck.setChecked(true);
        }
    }

    // Lagrer checksvar arrayen som unike boolverdier i sharedprefs
    public boolean storeArray(boolean[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);

        for(int i=0;i<array.length;i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    // Returnerer lagret bool verdier som en array
    public static boolean[] loadArray(String arrayName, SharedPreferences prefs) {
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }
}
