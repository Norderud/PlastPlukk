package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Egenskaper extends AppCompatActivity {

    String[] typer, str;    // Valgene til dropdown menyene
    String kategori, underKategori, størrelse;
    TextView feilMelding, overTekst;
    boolean visible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egenskaper);
        Intent intent = getIntent();

        kategori = intent.getStringExtra("kategori");

        feilMelding = findViewById(R.id.Feilmelding);
        overTekst = findViewById(R.id.tekst);
        overTekst.setText(kategori);

        lagDropDown();
    }


    // Åpner neste aktivitet - kamera
    public void openArea(View view){

        if (underKategori.equals("Velg type..") || (størrelse.equals("Velg størrelse..") && visible)){
            feilMelding.setText("Vennligst fyll ut alle feltene.");
            return;
        } else
            størrelse = "";
        Intent messageIntent = new Intent(this, Area.class);
        messageIntent.putExtra("Kategori", kategori);
        messageIntent.putExtra("Underkategori", underKategori);

        if (størrelse.length() != 0)
            messageIntent.putExtra("Størrelse", størrelse);
        startActivity(messageIntent);
    }

    // Oppretter dropdown menyene og deres funksjoner
    private void lagDropDown(){

        final LinearLayout layout = findViewById(R.id.str); //

        // Alle valgene legges til
        if (kategori.equals("Pose"))
            typer = new String[]{"Velg type..", "Stor plastpose", "Middels plastpose",
                    "Liten plastpose"};
        else if (kategori.equals("Emballasje"))
            typer = new String[]{"Velg type..", "Godteriemballasje", "Diverse matemballasje",
                    "Nettstrømpe (til f.eks. grønnsaker)", "Plastfilm"};
        else if (kategori.equals("Flaske"))
            typer = new String[]{"Velg type..", "Plastflaske", "Kanne", "Tønne", "Bøtte/Boks", "Snusboks", "Kork/lokk", "Annet"};
        else if (kategori.equals("Servise"))
            typer = new String[]{"Velg type..", "Kopper/Plastglass", "Tallerken", "Bestikk", "Annet"};
        else if (kategori.equals("Redskap"))
            typer = new String[]{"Velg type..", "Fiskegarn", "Fiskesnøre", "Lighter", "Tau/Tråd", "Annet"};
        else if (kategori.equals("Diverse"))
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
                    visible = true;
                }
                else if (valget.equals("Plastflaske")) {
                    str[1] = "0,5 Liter";
                    str[2] = "Mellom 0,5 og 1,5 Liter";
                    str[3] = "1,5 Liter eller større";
                    layout.setVisibility(View.VISIBLE);
                    visible = true;
                }
                else{
                    visible = false;
                    layout.setVisibility(View.INVISIBLE);
                }
                underKategori = dropdownTyper.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                underKategori = "";
            }
        });
        dropdownStr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                størrelse = dropdownStr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
