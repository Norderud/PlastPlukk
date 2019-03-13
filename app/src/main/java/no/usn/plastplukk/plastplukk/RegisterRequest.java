package no.usn.plastplukk.plastplukk;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = "http://192.168.10.180/brukerlogin/register.php";
    private Map<String, String> params;

    public RegisterRequest(String email, String passord, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener, new Response.ErrorListener() {
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
