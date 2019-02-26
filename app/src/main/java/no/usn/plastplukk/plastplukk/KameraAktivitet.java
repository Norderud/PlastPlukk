package no.usn.plastplukk.plastplukk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class KameraAktivitet extends AppCompatActivity {
    final static int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    Uri bildetsURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera_aktivitet);
        Button kameraKnapp =(Button)findViewById(R.id.kameraKnapp);
        imageView = findViewById(R.id.photoDisplay);
        kameraKnapp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            //TODO Lagre bilde hvis bilde er ok

            // Gets real path of image so it can be uploaded
            //bildetsURI = hentURI(getApplicationContext(), photo);
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //TODO hva skjer om man kansellerer
        }
    }
    /*public Uri hentURI(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/

// Refferanse på bilde-koden er https://developer.android.com/training/camera/photobasics#java
}
