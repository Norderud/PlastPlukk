package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import no.usn.plastplukk.plastplukk.HelpFunctions.PhotoHelpFunctions;
import no.usn.plastplukk.plastplukk.R;

public class PhotoUploadActivity extends AppCompatActivity {

    final static int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    String imageFileName;
    static String currentPhotoPath;
    Button taBildeKnapp, confirmPictureButton;
    public static String PHOTOPATH = "photoPath", IMAGEFILENAME = "imageFileName",
            IMAGE_WIDTH="imageWidth", IMAGE_HEIGHT = "imageHeigth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera_aktivitet);
        taBildeKnapp = (Button) findViewById(R.id.kameraKnapp);
        confirmPictureButton = (Button) findViewById(R.id.videreFraKamera);
        imageView = findViewById(R.id.photoDisplay);
        requestMultiplePermissions();
        taBildeKnapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        confirmPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confirmPictureIntent = new Intent(getApplicationContext(), ConfirmRegistrationActivity.class);
                confirmPictureIntent.putExtra(PHOTOPATH, currentPhotoPath);
                confirmPictureIntent.putExtra(IMAGEFILENAME, imageFileName);
                confirmPictureIntent.putExtra(IMAGE_WIDTH, imageView.getWidth());
                confirmPictureIntent.putExtra(IMAGE_HEIGHT, imageView.getHeight());
                startActivity(confirmPictureIntent);
            }
        });
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
            taBildeKnapp.setText("Ta nytt bilde?");
            confirmPictureButton.setVisibility(View.VISIBLE);
            Bitmap bitmap = PhotoHelpFunctions.loadImageFromFile(imageView, currentPhotoPath, imageView.getWidth(), imageView.getHeight());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            SharedPreferences sharedPreferences = getSharedPreferences(ChooseAreaActivity.MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("currentPhotoPath", currentPhotoPath);
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
            return image;
        }
        throw new IOException();
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

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
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}
// Benyttet https://demonuts.com/android-upload-image-using-volley/ for å lage opplastingskode

// Refferanse på bilde-koden er https://developer.android.com/training/camera/photobasics#java

// Kode for rotering av bilde funnet her:
// https://stackoverflow.com/questions/21776802/taking-picture-with-camera-intent-rotate-picture-in-portrait-mode-android