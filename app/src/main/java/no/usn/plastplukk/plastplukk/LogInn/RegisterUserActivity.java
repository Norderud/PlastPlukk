package no.usn.plastplukk.plastplukk.LogInn;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import no.usn.plastplukk.plastplukk.R;

public class RegisterUserActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword1;
    EditText etPassword2;

    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etEmail = findViewById(R.id.etEmail);
        etPassword1 = findViewById(R.id.etPassword1);

        bRegister = findViewById(R.id.bRegister);
    }

    public void registerUser(View view) {
        final String email = etEmail.getText().toString();
        final String password1 = etPassword1.getText().toString();
        final String password2 = etPassword2.getText().toString();


        Log.e("Email", email);
        Log.e("Password", password1);

        if(email.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            alertDialog("Vennligst fyll ut alle feltene");
            return;
        }
        if(isValidPassword(password1))
        if(password1 != password2){
            alertDialog("Passordene matcher ikke");
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else{
                        alertDialog("Registrering feilet");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest(email, password1, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterUserActivity.this);
        queue.add(registerRequest);
    }

    private boolean isValidPassword(String password1) {
        if(true){

        }
    }

    private void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
        builder.setMessage(message)
                .setNegativeButton("Prøv igjen", null)
                .create()
                .show();

    }

}
