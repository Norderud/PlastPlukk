package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Egenskaper extends AppCompatActivity {

    String[] typer, str;
    String valg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egenskaper);
        Intent intent = getIntent();
        valg = intent.getStringExtra("valg");

        TextView tekst = findViewById(R.id.tekst);
        tekst.setText("Du har valgt: " + valg);

        lagDropDown();
    }

    private void lagDropDown(){

        final LinearLayout layout = findViewById(R.id.str); //

        if (valg.equals("Pose"))
            typer = new String[]{"Velg Type..", "Stor plastpose", "Middels plastpose",
                    "Liten plastpose"};
        else if (valg.equals("Emballasje"))
            typer = new String[]{"Velg Type..", "Godteriemballasje", "Diverse matemballasje",
                    "Nettstrømpe (til f.eks. grønnsaker)", "Plastfilm"};
        else if (valg.equals("Flaske"))
            typer = new String[]{"Velg type..", "Plastflaske", "Kanne", "Tønne", "Bøtte/Boks", "Snusboks", "Kork/lokk", "Annet"};
        else if (valg.equals("Servise"))
            typer = new String[]{"Velg type..", "Kopper/Plastglass", "Tallerken", "Bestikk", "Annet"};
        else if (valg.equals("Redskap"))
            typer = new String[]{"Velg type..", "Fiskegarn", "Fiskesnøre", "Lighter", "Tau/Tråd", "Annet"};
        else if (valg.equals("Diverse"))
            typer = new String[]{"Velg type..", "Bekledning", "Riflehylser", "Ballonger", "Bildeler", "Diverse kroppspleie",
                    "Blander, dumpet avfall", "Annet"};


        str = new String[4]; str[0] = "Velg størrelse..";

        // Dropdown meny, spinnerType
        final Spinner dropdownTyper = findViewById(R.id.spinnerType);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typer);
        dropdownTyper.setAdapter(adapterType);

        //Dropdown meny for størrelsevalg
        final Spinner dropdownStr = findViewById(R.id.spinnerStr);
        ArrayAdapter<String> adapterStr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str);
        dropdownStr.setAdapter(adapterStr);

        // Legger til størrelsemeny dersom plastfilm/-flaske er valgt
        dropdownTyper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valget = (String) dropdownTyper.getSelectedItem().toString();
                if (valget.equals("Plastfilm")) {
                    str[1] = "Mindre enn knyttneve";
                    str[2] = "Mindre enn avis";
                    str[3] = "Større enn avis";
                    layout.setVisibility(View.VISIBLE);
                }
                else if (valget.equals("Plastflaske")) {
                    str[1] = "0,5 Liter";
                    str[2] = "Mellom 0,5 og 1,5 Liter";
                    str[3] = "1,5 Liter eller større";
                    layout.setVisibility(View.VISIBLE);
                }
                else layout.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
