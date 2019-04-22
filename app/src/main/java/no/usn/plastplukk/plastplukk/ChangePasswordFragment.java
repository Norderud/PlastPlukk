package no.usn.plastplukk.plastplukk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import no.usn.plastplukk.plastplukk.login.RegisterRequest;
import no.usn.plastplukk.plastplukk.login.RegisterUserActivity;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERID;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERNAME;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener{
    EditText etOldPassword;
    EditText etNew1;
    EditText etNew2;
    Button bChange;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        etOldPassword = v.findViewById(R.id.et_old_password);
        etNew1 = v.findViewById(R.id.etPassword1);
        etNew2 = v.findViewById(R.id.etPassword2);
        bChange = v.findViewById(R.id.bChange);
        bChange.setOnClickListener(this);
        return v;
    }

    public void changePassword() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetwork() == null){
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        final String oldPass = etOldPassword.getText().toString();
        final String newPass1 = etNew1.getText().toString();
        final String newPass2 = etNew2.getText().toString();

        if(oldPass.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()){
            alertDialog(getString(R.string.fyll_ut_alle_felt));
            return;
        }
        if(!newPass1.equals(newPass2)){
            alertDialog(getString(R.string.passord_match));
            return;
        }
        if(!isValidPassword(newPass1) || !isValidPassword(newPass2)){
            alertDialog(getString(R.string.passord_sjekk));
            return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Toast.makeText(getActivity(), getString(R.string.endring_vellykket), Toast.LENGTH_LONG).show();
                    } else{
                        String error = jsonResponse.getString("error");
                        alertDialog(getString(R.string.registrering_feilet) + "\n"+ error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String userId = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).getInt("userID", 0) +"";
        ChangePasswordRequest changePassReq = new ChangePasswordRequest(oldPass, newPass1, userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(changePassReq);
    }

    private boolean isValidPassword(String password1) {
        String regex = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$";
        if(Pattern.matches(regex, password1)){
            return true;
        }
        return false;
    }

    private void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setNegativeButton(R.string.prov_igjen, null)
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == bChange.getId()){
            changePassword();
        }
    }
}
