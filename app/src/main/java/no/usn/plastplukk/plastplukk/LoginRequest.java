package no.usn.plastplukk.plastplukk;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://itfag.usn.no/grupper/v19gr2/plast/itfag/login.php";
    private Map<String, String> params;

    public LoginRequest(String email, String passord, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });

        params = new HashMap<>();
        params.put("email", email);
        params.put("passord", passord);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
