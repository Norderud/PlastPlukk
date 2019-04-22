package no.usn.plastplukk.plastplukk;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.WEBURL;

public class ChangePasswordRequest extends StringRequest {
    private static final String URL = String.format("%s%s", WEBURL, "changePassword");
    private Map<String, String> params;

    public ChangePasswordRequest(String oldPass, String newPass1, String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });
        params = new HashMap<>();
        params.put("oldPass", oldPass);
        params.put("newPass", newPass1);
        params.put("userID", userID);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

