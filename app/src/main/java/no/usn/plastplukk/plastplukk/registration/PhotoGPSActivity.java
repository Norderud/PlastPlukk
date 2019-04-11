package no.usn.plastplukk.plastplukk.registration;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import no.usn.plastplukk.plastplukk.R;
import no.usn.plastplukk.plastplukk.functions.HelpFunctions;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.CURRENT_PHOTO_PATH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_HEIGHT;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_WIDTH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGE_FILE_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;

public class PhotoGPSActivity extends AppCompatActivity {

    final static int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    String imageFileName;
    static String currentPhotoPath;
    Button taBildeKnapp, confirmPictureButton;
    public static String PHOTOPATH = "photoPath", IMAGEFILENAME = "imageFileName",
            IMAGE_WIDTH = "imageWidth", IMAGE_HEIGHT = "imageHeigth";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gps);
        getSupportActionBar().setTitle("");
        taBildeKnapp = (Button) findViewById(R.id.kameraKnapp);
        confirmPictureButton = (Button) findViewById(R.id.videreFraKamera);
        imageView = findViewById(R.id.photoDisplay);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        taBildeKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
            }
        });
        confirmPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confirmPictureIntent = new Intent(getApplicationContext(), ConfirmRegistrationActivity.class);
                confirmPictureIntent.putExtra(IMAGEFILENAME, imageFileName);
                confirmPictureIntent.putExtra(IMAGE_WIDTH, imageView.getWidth());
                confirmPictureIntent.putExtra(IMAGE_HEIGHT, imageView.getHeight());
                editor.putInt(IMAGEVIEW_WIDTH, imageView.getWidth());
                editor.putInt(IMAGEVIEW_HEIGHT, imageView.getHeight());
                editor.apply();
                startActivity(confirmPictureIntent);
            }
        });
        if (prefs.getString(CURRENT_PHOTO_PATH, "").length() > 0) {
            showPictureOnReturn();
        }
    }

    private void showPictureOnReturn(){
        imageFileName = prefs.getString(IMAGE_FILE_NAME, imageFileName);
        taBildeKnapp.setText(getString(R.string.ta_nytt_bilde));
        confirmPictureButton.setVisibility(View.VISIBLE);
        Bitmap bitmap = HelpFunctions.loadImageFromFile(imageView, prefs.getString(CURRENT_PHOTO_PATH, ""),
                prefs.getInt(IMAGEVIEW_WIDTH, 0), prefs.getInt(IMAGEVIEW_HEIGHT, 0));
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
        TextView tempText = findViewById(R.id.take_pic_text);
        tempText.setVisibility(View.GONE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();

                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "no.usn.plastplukk.plastplukk.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            taBildeKnapp.setText(getString(R.string.ta_nytt_bilde));
            confirmPictureButton.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = HelpFunctions.loadImageFromFile(imageView, currentPhotoPath, imageView.getWidth(), imageView.getHeight());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(CURRENT_PHOTO_PATH, currentPhotoPath);
            editor.putInt(IMAGEVIEW_HEIGHT, imageView.getHeight());
            editor.putInt(IMAGEVIEW_WIDTH, imageView.getWidth());
            editor.apply();
            TextView tempText = findViewById(R.id.take_pic_text);
            tempText.setVisibility(View.GONE);
            Intent confirmPictureIntent = new Intent(getApplicationContext(), ConfirmRegistrationActivity.class);
            confirmPictureIntent.putExtra(IMAGEFILENAME, imageFileName);
            confirmPictureIntent.putExtra(IMAGE_WIDTH, imageView.getWidth());
            confirmPictureIntent.putExtra(IMAGE_HEIGHT, imageView.getHeight());
            startActivity(confirmPictureIntent);
        } else {
            recreate();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) { // Only create file if external storage exists
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                    + "_" + String.valueOf(Calendar.getInstance().getTimeInMillis());
            imageFileName = String.format("JPEG_%s_.jpg", timeStamp);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = new File(storageDir, imageFileName);
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            editor.putString(IMAGE_FILE_NAME, imageFileName);
            editor.apply();
            return image;
        }
        throw new IOException();
    }

    private void requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.alle_rettigheter_er_godtatt), Toast.LENGTH_SHORT).show();
                            dispatchTakePictureIntent();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            alertDialog("Manglende Rettighet! Aktiver lokasjonstjenester og lagring",
                                    "Instillinger", Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.feilet), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
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
// Benyttet https://demonuts.com/android-upload-image-using-volley/ for å lage opplastingskode

// Refferanse på bilde-koden er https://developer.android.com/training/camera/photobasics#java

// Kode for rotering av bilde funnet her:
// https://stackoverflow.com/questions/21776802/taking-picture-with-camera-intent-rotate-picture-in-portrait-mode-android