package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.content.Intent;
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
}
