package no.usn.plastplukk.plastplukk.LogInn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword1;
    private final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword1 = findViewById(R.id.etPasswordLogin);
        final Button bLogin = findViewById(R.id.bLogin);
        final TextView registerLink = findViewById(R.id.registerLink);
    }

   @Override
   public void onBackPressed(){
       moveTaskToBack(true);
   }

    public void sendToRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterUserActivity.class);
        startActivity(registerIntent);
    }

    public void logIn(View view){
        final String email = etEmail.getText().toString();
        String password = etPassword1.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int result = jsonResponse.getInt("result");
                    if (result == 1) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("Email", email);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);

                    } else if (result == 2){
                        alertDialog(getString(R.string.feil_passord));
                    } else {
                        alertDialog(getString(R.string.feil_ved_innlogging));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
    private void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(R.string.prov_igjen, null)
                .create()
                .show();

    }
}
