package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Area extends AppCompatActivity {

    String kategori, underKategori, størrelse;
    boolean[] checkSvar;
    TextView feilMelding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        Intent intent = getIntent();
        kategori = intent.getStringExtra("Kategori");
        underKategori = intent.getStringExtra("Underkategori");
        størrelse = intent.getStringExtra("Størrelse");


        feilMelding = findViewById(R.id.Feilmelding);
        checkSvar = new boolean[]{false, false, false, false, false, false};
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

        if (checkSvar.length != 0)
            messageIntent.putExtra("checkSvar", checkSvar);
        messageIntent.putExtra("Kategori", kategori);
        messageIntent.putExtra("Underkategori", underKategori);
        if (størrelse!=null)
            messageIntent.putExtra("Størrelse", størrelse);

        Log.e("Kategori", kategori);
        Log.e("Underkategori", underKategori);
        if (størrelse != null)
            Log.e("Størrelse", størrelse);

        for (int i=0; i<checkSvar.length; i++)
            Log.e("Område", ""+checkSvar[i]);


        startActivity(messageIntent);
    }




    public void checkBoxes(View view){

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {

            case R.id.fjellCheck:
                if (checked)
                    checkSvar[0] = true;
                else
                    checkSvar[0] = false;
                break;
            case R.id.skogCheck:
                if (checked)
                    checkSvar[1] = true;
                else
                    checkSvar[1] = false;
                break;
            case R.id.elvCheck:
                if (checked)
                    checkSvar[2] = true;
                else
                    checkSvar[2] = false;
                break;
            case R.id.kystCheck:
                if (checked)
                    checkSvar[3] = true;
                else
                    checkSvar[3] = false;
                break;
            case R.id.innsjøCheck:
                if (checked)
                    checkSvar[4] = true;
                else
                    checkSvar[4] = false;
                break;
            case R.id.byCheck:
                if (checked)
                    checkSvar[5] = true;
                else
                    checkSvar[5] = false;
                break;
        }
    }
}
