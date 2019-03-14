package no.usn.plastplukk.plastplukk.LogInn;

import android.support.annotation.Nullable;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = "https://itfag.usn.no/grupper/v19gr2/plast/itfag/register.php";
    private Map<String, String> params;

    public RegisterRequest(String email, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });

        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        Log.e("email", email);
        Log.e("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
