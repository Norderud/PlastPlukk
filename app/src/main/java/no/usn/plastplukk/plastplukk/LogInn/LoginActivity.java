package no.usn.plastplukk.plastplukk.LogInn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPassword1;
    private final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUserLogin);
        etPassword1 = findViewById(R.id.etPasswordLogin);
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetwork() == null){
            Toast.makeText(getApplicationContext(), "Du er ikke koblet til internett.", Toast.LENGTH_SHORT).show();
            return;
        }

            final String username = etUser.getText().toString();
        String password = etPassword1.getText().toString();

        Log.e("username: ", username);
        Log.e("password: ", password);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String error = jsonResponse.getString("error");
                    if (success) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("User", username);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);

                    } else {
                        alertDialog(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
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
