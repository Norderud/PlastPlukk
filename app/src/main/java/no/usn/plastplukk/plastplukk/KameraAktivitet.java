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

public class KameraAktivitet extends AppCompatActivity {
    Uri bildetsURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera_aktivitet);
        Button kameraKnapp =(Button)findViewById(R.id.kameraKnapp);
        kameraKnapp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //TODO Lagre bilde hvis bilde er ok, vise det frem
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Gets real path of image so it can be uploaded
                bildetsURI = hentURI(getApplicationContext(), photo);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO hva skjer om man kansellerer
            }
        }
    }
    public Uri hentURI(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
