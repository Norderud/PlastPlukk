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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import no.usn.plastplukk.plastplukk.MainActivity;
import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERID;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERNAME;

public class RegisterUserActivity extends AppCompatActivity {
    EditText etUser;
    EditText etPassword1;
    EditText etPassword2;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etUser = findViewById(R.id.etUser);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);

        bRegister = findViewById(R.id.bRegister);
        etPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    registerUser(getCurrentFocus());
                    return true;
                }
                return false;
            }
        });
    }

    public void registerUser(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetwork() == null){
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        final String user = etUser.getText().toString();
        final String password1 = etPassword1.getText().toString();
        final String password2 = etPassword2.getText().toString();

        if(user.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            alertDialog(getString(R.string.fyll_ut_alle_felt));
            return;
        }
        if(!isValidPassword(password1)){
            alertDialog(getString(R.string.passord_sjekk));
            return;
        }
        if(!password1.equals(password2)){
            alertDialog(getString(R.string.passord_match));
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String error = jsonResponse.getString("error");
                    if(success){
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(USERNAME, user);
                        editor.putInt(USERID, jsonResponse.getInt("user_id"));
                        editor.apply();
                        Toast.makeText(RegisterUserActivity.this, getString(R.string.registrering_vellykket), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                        RegisterUserActivity.this.startActivity(intent);
                    } else{
                        alertDialog(getString(R.string.registrering_feilet) + "\n"+ error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest(user, password1, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterUserActivity.this);
        queue.add(registerRequest);
    }

    private boolean isValidPassword(String password1) {
        String regex = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$";
        if(Pattern.matches(regex, password1)){
            return true;
        }
        return false;
    }

    private void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(R.string.prov_igjen, null)
                .create()
                .show();
    }

}
