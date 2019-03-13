package no.usn.plastplukk.plastplukk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Area extends AppCompatActivity {

    String kategori, underKategori, størrelse;
    boolean[] checkSvar;
    CheckBox fjellCheck, skogCheck, elvCheck, kystCheck, innsjøCheck, byCheck;
    TextView feilMelding;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        kategori =  prefs.getString("kategori", "Ingen");
        underKategori = prefs.getString("Underkategori", "Ingen");
        størrelse = prefs.getString("Størrelse", størrelse);

        feilMelding = findViewById(R.id.Feilmelding);
        checkSvar = new boolean[]{false, false, false, false, false, false};
    }

    @Override
    protected void onResume() {
        checkOnReturn();
        super.onResume();
    }

    public void openKamera(View view){
        boolean check = false;
        for (int i=0; i<checkSvar.length; i++) {
            if (checkSvar[i])
                check = true;
        }

        if (!check) {
            feilMelding.setText("Vennligst velg minst en type område.");
            return;
        }

        Intent messageIntent = new Intent(this, KameraAktivitet.class);
        startActivity(messageIntent);
    }


    public void checkBoxes(View view){

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {

            case R.id.fjellCheck:
                if (checked) {
                    checkSvar[0] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[0] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
            case R.id.skogCheck:
                if (checked) {
                    checkSvar[1] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[1] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
            case R.id.elvCheck:
                if (checked) {
                    checkSvar[2] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[2] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
            case R.id.kystCheck:
                if (checked) {
                    checkSvar[3] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[3] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
            case R.id.innsjøCheck:
                if (checked) {
                    checkSvar[4] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[4] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
            case R.id.byCheck:
                if (checked) {
                    checkSvar[5] = true;
                    storeArray(checkSvar, "Checksvar", this);
                }
                else {
                    checkSvar[5] = false;
                    storeArray(checkSvar, "Checksvar", this);
                }
                break;
        }
    }

    public void checkOnReturn(){
        boolean[] array = loadArray("Checksvar", this);

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
            byCheck = findViewById(R.id.byCheck);
            byCheck.setChecked(true);
        }

    }

    public boolean storeArray(boolean[] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);

        for(int i=0;i<array.length;i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public boolean[] loadArray(String arrayName, Context mContext) {
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for(int i=0;i<size;i++)
        array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }
}
