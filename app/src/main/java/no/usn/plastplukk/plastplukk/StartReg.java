package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StartReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_reg);
    }

    public void velgEgenskaper(View view) {
        String kategori = view.getTag().toString();
        Intent nyIntent = new Intent(this.getBaseContext(), Egenskaper.class);
        nyIntent.putExtra("kategori", kategori);
        this.startActivity(nyIntent);
    }
}
