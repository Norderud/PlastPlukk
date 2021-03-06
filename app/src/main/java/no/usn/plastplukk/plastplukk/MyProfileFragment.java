package no.usn.plastplukk.plastplukk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import no.usn.plastplukk.plastplukk.R;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERID;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERNAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.WEBURL;

public class MyProfileFragment extends Fragment implements View.OnClickListener {
    private RequestQueue rQueue;
    Button changePasswordButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        final TextView totalSum = v.findViewById(R.id.totalRegSum);
        final TextView yearSum = v.findViewById(R.id.thisYearRegSum);
        final TextView monthSum = v.findViewById(R.id.thisMonthRegSum);
        final TextView daySum = v.findViewById(R.id.todayRegSum);
        TextView brukernavnText = v.findViewById(R.id.profile_username);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        brukernavnText.setText(sharedPreferences.getString(USERNAME, ""));
        JSONObject jsonObject = new JSONObject();

        try {
            if (sharedPreferences.getInt(USERID, -1) != -1) {

                //Send inn bildet
                jsonObject.put("userID", sharedPreferences.getInt(USERID, -1));
            }
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        String upload_URL = String.format("%s%s", WEBURL, "sumreg");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int replyTotal = jsonObject.getInt("total");
                            int replyYearTotal = jsonObject.getInt("year_total");
                            int replyMonthTotal = jsonObject.getInt("month_total");
                            int replyDayTotal = jsonObject.getInt("day_total");
                            rQueue.getCache().clear();
                            totalSum.setText(""+replyTotal + " stk.");
                            yearSum.setText(""+replyYearTotal+ " stk.");
                            monthSum.setText(""+replyMonthTotal+ " stk.");
                            daySum.setText(""+replyDayTotal+ " stk.");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa2", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });

        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjectRequest);

        changePasswordButton = v.findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == changePasswordButton.getId()){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ChangePasswordFragment())
                    .commit();
        }
    }
}
