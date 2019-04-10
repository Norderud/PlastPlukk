package no.usn.plastplukk.plastplukk.login;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.WEBURL;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = String.format("%s%s", WEBURL, "register");
    private Map<String, String> params;

    public RegisterRequest(String user, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });
        params = new HashMap<>();
        params.put("user", user);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
