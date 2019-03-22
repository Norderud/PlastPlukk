package no.usn.plastplukk.plastplukk.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.CURRENT_PHOTO_PATH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_HEIGHT;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_WIDTH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGE_FILE_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MAIN_CATEGORY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.TYPE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.SIZE;

public class RegistrationCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_complete);

        Button homeButton = findViewById(R.id.doneHomeButton);
        Button newRegisterButton = findViewById(R.id.doneNewRegister);
        clearPreferences();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(RegistrationCompleteActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
        newRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegistrationCompleteActivity.this, CategoryActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }

    // Resetter verdiene som lagres under registrering
    private void clearPreferences(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(MAIN_CATEGORY);
        editor.remove(TYPE);
        editor.remove(SIZE);
        editor.remove(IMAGEVIEW_HEIGHT);
        editor.remove(IMAGEVIEW_WIDTH);
        editor.remove(CURRENT_PHOTO_PATH);
        editor.remove(IMAGE_FILE_NAME);

        int size = prefs.getInt("Checksvar" + "_size", 0);
        for (int i = 0; i < size; i++)
            editor.remove("Checksvar" + "_" + i);
        editor.remove("Checksvar_size");
        editor.apply();
    }
}
