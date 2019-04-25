package no.usn.plastplukk.plastplukk.registration;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import no.usn.plastplukk.plastplukk.functions.HelpFunctions;
import no.usn.plastplukk.plastplukk.R;
import no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues;

import static android.view.View.GONE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.AREA_ARRAY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MAIN_CATEGORY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.TYPE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.LATITUDE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.LONGITUDE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.SIZE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERID;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.WEBURL;


public class ConfirmRegistrationActivity extends AppCompatActivity {

    private JSONObject jsonObject;
    private RequestQueue rQueue;
    ImageView imageView;
    private String imageFileName;
    String photoPath;
    Bitmap bitmap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private boolean newLocationRecieved, submitButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_registration);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        SharedPreferences sharedPreferences = getSharedPreferences(
                MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                editor.putString(LATITUDE, ""+location.getLatitude());
                editor.putString(LONGITUDE, ""+location.getLongitude());
                newLocationRecieved = true;
                if (submitButtonPressed) findViewById(R.id.confirmButton).performClick();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };

        //Set the values that the user has selected.
        TextView confirmCat = findViewById(R.id.confirmCatEdit);
        confirmCat.append(sharedPreferences.getString(MAIN_CATEGORY, null));
        TextView confirmSecondCat = findViewById(R.id.confirmSecondCatEdit);
        confirmSecondCat.append(sharedPreferences.getString(TYPE, null));
        TextView confirmSizeEdit = findViewById(R.id.confirmSizeEdit);
        TextView confirmSize = findViewById(R.id.confirmSize);
        if (sharedPreferences.getString(SIZE, null) == null){
            confirmSize.setVisibility(View.INVISIBLE);
            confirmSizeEdit.setVisibility(View.INVISIBLE);
        }else{
            confirmSizeEdit.append(sharedPreferences.getString(SIZE, "Ikke satt"));
        }
        TextView confirmLocation = findViewById(R.id.confirmLocationEdit);
        confirmLocation.append(getLocations());



        //Find picture and place in imageview
        imageFileName = bundle.getString(PhotoActivity.IMAGEFILENAME);
        imageView = findViewById(R.id.photoConfirmDisplay);
        photoPath = sharedPreferences.getString(SharedPreferencesValues.CURRENT_PHOTO_PATH,"");
        bitmap = HelpFunctions.loadImageFromFile(imageView, photoPath,
                bundle.getInt(PhotoActivity.IMAGE_WIDTH),
                bundle.getInt(PhotoActivity.IMAGE_HEIGHT));
        imageView.setImageBitmap(bitmap);
        //Upload the data to server with onlick on button.
        Button uploadButton = (Button) findViewById(R.id.confirmButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getActiveNetwork() == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        alertDialog("Aktiver GPS for å fortsette.", "Endre innstillinger", Settings.ACTION_LOCATION_SOURCE_SETTINGS, null);
                        return;
                    }
                    if (!newLocationRecieved) {
                        findViewById(R.id.confirmButton).setVisibility(View.GONE);
                        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                        submitButtonPressed = true;
                        Toast.makeText(getApplicationContext(), getString(R.string.venter_gps), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editor.apply();
                    uploadRegistration(bitmap);
                    Intent registrationConfirmed = new Intent(
                            ConfirmRegistrationActivity.this,
                            RegistrationCompleteActivity.class);
                    startActivity(registrationConfirmed);

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(ConfirmRegistrationActivity.this, R.string.feilet, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStop(){
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
        }
        super.onResume();
    }

    private String getLocations() {

        SharedPreferences sharedPreferences = getSharedPreferences(
                MY_PREFS_NAME, MODE_PRIVATE);
        StringBuilder result = new StringBuilder();
        boolean[] areaCheckList = AreaActivity.loadArray(AREA_ARRAY, sharedPreferences);
        for (int i = 0; i < areaCheckList.length; i++) {
            if (areaCheckList[i]) {
                if (result.toString().length() > 0){
                    result.append(", ");
                }
                switch (i) {
                    case 0:
                        result.append(getString(R.string.fjell));
                        break;
                    case 1:
                        result.append(getString(R.string.skog));
                        break;
                    case 2:
                        result.append(getString(R.string.elv));
                        break;
                    case 3:
                        result.append(getString(R.string.kyst));
                        break;
                    case 4:
                        result.append(getString(R.string.innsjo));
                        break;
                    case 5:
                        result.append(getString(R.string.vei));
                        break;
                    case 6:
                        result.append(getString(R.string.industri_handel));
                        break;
                    case 7:
                        result.append(getString(R.string.skole_fritid));
                        break;
                    case 8:
                        result.append(getString(R.string.dyrket_mark_landbruk));
                        break;
                    case 9:
                        result.append(getString(R.string.boligområde));
                        break;
                }
            }
        }
        return result.toString();
    }

    //Do the upload of the complete registration. Put in jsonRequest and send to PHP API.
    private void uploadRegistration(Bitmap bitmap) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                MY_PREFS_NAME, MODE_PRIVATE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            //Send inn bildet
            jsonObject.put("name", imageFileName);
            jsonObject.put("image", encodedImage);
            //Send inn type, størrelse osv.
            jsonObject.put("type", sharedPreferences.getString(TYPE, null));
            jsonObject.put("size", sharedPreferences.getString(SIZE, ""));
            jsonObject.put("userID", sharedPreferences.getInt(USERID, 0));
            jsonObject.put("latitude", sharedPreferences.getString(LATITUDE, null));
            jsonObject.put("longitude", sharedPreferences.getString(LONGITUDE, null));

            boolean[] areaCheckList = AreaActivity.loadArray("Checksvar", sharedPreferences);
            jsonObject.put("Mountain", (!areaCheckList[0]) ? 0 : 1);
            jsonObject.put("Forest", (!areaCheckList[1]) ? 0 : 1);
            jsonObject.put("River", (!areaCheckList[2]) ? 0 : 1);
            jsonObject.put("Coast", (!areaCheckList[3]) ? 0 : 1);
            jsonObject.put("Lake", (!areaCheckList[4]) ? 0 : 1);
            jsonObject.put("Road", (!areaCheckList[5]) ? 0 : 1);
            jsonObject.put("Industry_Towns", (!areaCheckList[6]) ? 0 : 1);
            jsonObject.put("School_Recreational_area", (!areaCheckList[7]) ? 0 : 1);
            jsonObject.put("Acre_Agriculture", (!areaCheckList[8]) ? 0 : 1);
            jsonObject.put("Residential_area", (!areaCheckList[9]) ? 0 : 1);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        String upload_URL = String.format("%s%s", WEBURL, "uploadcomplete");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                        Log.e("aaaaaaa", jsonObject.toString());
                            String response = jsonObject.getString("message");
                            rQueue.getCache().clear();
                            Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });

        rQueue = Volley.newRequestQueue(this);
        rQueue.add(jsonObjectRequest);

    }

    private void alertDialog(String message, String buttonName, final String settings, final Uri uri){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(buttonName, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (settings == null)
                            return;
                        Intent intent = new Intent(settings);
                        if (uri != null)
                            intent.setData(uri);
                        startActivityForResult(intent, 233);
                    }
                })
                .create()
                .show();
    }
}
