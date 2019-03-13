package no.usn.plastplukk.plastplukk;

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
        etPassword2 = findViewById(R.id.etPassword2);

        bRegister = findViewById(R.id.bRegister);
    }

    public void registerUser(View view) {
        final String email = etEmail.getText().toString();
        final String passord = etPassword1.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    Log.e("s",response);
                    if(success){
                        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                        builder.setMessage("Registrering feilet")
                                .setNegativeButton("Prøv igjen", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest(email, passord, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterUserActivity.this);
        queue.add(registerRequest);
    }
}
