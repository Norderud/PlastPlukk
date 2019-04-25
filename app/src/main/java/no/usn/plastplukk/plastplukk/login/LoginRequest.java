package no.usn.plastplukk.plastplukk.login;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.WEBURL;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = String.format("%s%s", WEBURL, "login");
    private Map<String, String> params;

    public LoginRequest(String user, String password, Response.Listener<String> listener,
                        Response.ErrorListener errorListener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("user", user);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
