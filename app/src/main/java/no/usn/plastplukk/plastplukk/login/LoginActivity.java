package no.usn.plastplukk.plastplukk.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERID;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERNAME;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPassword1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = findViewById(R.id.etUserLogin);
        etPassword1 = findViewById(R.id.etPasswordLogin);
        etPassword1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    logIn(getCurrentFocus());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void sendToRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterUserActivity.class);
        startActivity(registerIntent);
    }

    public void logIn(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetwork() == null) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        final String username = etUser.getText().toString();
        String password = etPassword1.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        int userID = jsonResponse.getInt("userID");
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(USERNAME, username);
                        editor.putInt(USERID, userID);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);

                    } else {
                        String error = jsonResponse.getString("error");
                        alertDialog(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
            }
        };
        LoginRequest loginRequest = new LoginRequest(username, password, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    private void alertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(R.string.prov_igjen, null)
                .create()
                .show();
    }
}
