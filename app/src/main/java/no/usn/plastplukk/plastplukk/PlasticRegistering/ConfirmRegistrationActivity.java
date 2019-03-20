package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
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

import no.usn.plastplukk.plastplukk.HelpFunctions.PhotoHelpFunctions;
import no.usn.plastplukk.plastplukk.R;

public class ConfirmRegistrationActivity extends AppCompatActivity {

    private JSONObject jsonObject;
    private RequestQueue rQueue;
    ImageView imageView;
    private String imageFileName;
    String photoPath;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_registration);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //Set the values that the user has selected.
        SharedPreferences sharedPreferences = getSharedPreferences(
                ChooseAreaActivity.MY_PREFS_NAME, MODE_PRIVATE);
        TextView confirmCat = findViewById(R.id.confirmCat);
        confirmCat.append(sharedPreferences.getString("Kategori", null));
        TextView confirmSecondCat = findViewById(R.id.confirmSecondCat);
        confirmSecondCat.append(sharedPreferences.getString("Underkategori", null));
        TextView confirmSize = findViewById(R.id.confirmSize);
        confirmSize.append(sharedPreferences.getString("Størrelse", "Kan ikke settes for dette objektet"));
        TextView confirmLocation = findViewById(R.id.confirmLocation);
        confirmLocation.append(getLocations());

        //Find picture and place in imageview

        imageFileName = bundle.getString(PhotoUploadActivity.IMAGEFILENAME);
        imageView = findViewById(R.id.photoConfirmDisplay);
        photoPath = bundle.getString(PhotoUploadActivity.PHOTOPATH);
        bitmap = PhotoHelpFunctions.loadImageFromFile(imageView, photoPath,
                bundle.getInt(PhotoUploadActivity.IMAGE_WIDTH),
                bundle.getInt(PhotoUploadActivity.IMAGE_HEIGHT));
        imageView.setImageBitmap(bitmap);
        //Upload the data to server with onlick on button.
        Button uploadButton = (Button) findViewById(R.id.confirmButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadRegistration(bitmap);
                    Intent registrationConfirmed = new Intent(
                            ConfirmRegistrationActivity.this,
                            RegistrationFinnished.class);
                    startActivity(registrationConfirmed);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmRegistrationActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getLocations() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                ChooseAreaActivity.MY_PREFS_NAME, MODE_PRIVATE);
        StringBuilder result = new StringBuilder();
        boolean[] areaCheckList = ChooseAreaActivity.loadArray("Checksvar", sharedPreferences);
        for (int i = 0; i < areaCheckList.length; i++) {
            if (areaCheckList[i]) {
                switch (i) {
                    case 0:
                        result.append("Fjell, ");
                        break;
                    case 1:
                        result.append("Skog, ");
                        break;
                    case 2:
                        result.append("Elv, ");
                        break;
                    case 3:
                        result.append("Kyst, ");
                        break;
                    case 4:
                        result.append("Innsjø, ");
                        break;
                    case 5:
                        result.append("By, ");
                        break;
                }
            }
        }
        return result.toString();
    }

    //Do the upload of the complete registration. Put in jsonRequest and send to PHP API.
    private void uploadRegistration(Bitmap bitmap) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                ChooseAreaActivity.MY_PREFS_NAME, MODE_PRIVATE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            //Send inn bildet
            jsonObject.put("name", imageFileName);
            jsonObject.put("image", encodedImage);
            //Send inn kategori, underkategori, størrelse osv.
            jsonObject.put("maincategory", sharedPreferences.getString("Kategori", null));
            jsonObject.put("secondcategory", sharedPreferences.getString("Underkategori", null));
            jsonObject.put("size", sharedPreferences.getString("Størrelse", null));
            jsonObject.put("user", sharedPreferences.getString("User", null));
            jsonObject.put("latitude", sharedPreferences.getString("Latitude", null));
            jsonObject.put("longitude", sharedPreferences.getString("Longitude", null));

            boolean[] areaCheckList = ChooseAreaActivity.loadArray("Checksvar", sharedPreferences);
            jsonObject.put("Mountain", (!areaCheckList[0]) ? 0 : 1);
            jsonObject.put("Forest", (!areaCheckList[1]) ? 0 : 1);
            jsonObject.put("River", (!areaCheckList[2]) ? 0 : 1);
            jsonObject.put("Coast", (!areaCheckList[3]) ? 0 : 1);
            jsonObject.put("Lake", (!areaCheckList[4]) ? 0 : 1);
            jsonObject.put("City", (!areaCheckList[5]) ? 0 : 1);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        String upload_URL = "https://itfag.usn.no/grupper/v19gr2/plast/itfag/uploadVolley.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        rQueue.getCache().clear();
                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
}
