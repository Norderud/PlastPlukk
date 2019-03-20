package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

public class RegistrationFinnished extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_finnished);

        Button homeButton = findViewById(R.id.doneHomeButton);
        Button newRegisterButton = findViewById(R.id.doneNewRegister);
        clearPreferences();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(RegistrationFinnished.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
        newRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegistrationFinnished.this, ChooseCategoryActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    // Resetter verdiene som lagres under registrering
    private void clearPreferences(){
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Kategori");
        editor.remove("Underkategori");
        editor.remove("Størrelse");

        int size = prefs.getInt("Checksvar" + "_size", 0);
        for (int i = 0; i < size; i++)
            editor.remove("Checksvar" + "_" + i);
        editor.remove("Checksvar_size");
        editor.apply();
    }
}
