package no.usn.plastplukk.plastplukk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import no.usn.plastplukk.plastplukk.login.LoginActivity;
import no.usn.plastplukk.plastplukk.registration.CategoryActivity;

import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.CURRENT_PHOTO_PATH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_HEIGHT;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGEVIEW_WIDTH;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.IMAGE_FILE_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MAIN_CATEGORY;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.MY_PREFS_NAME;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.TYPE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.SIZE;
import static no.usn.plastplukk.plastplukk.functions.SharedPreferencesValues.USERNAME;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Resetter lagret verdier
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       if (prefs.getString(USERNAME, null) == null){
            Intent loggInnIntent = new Intent(this, LoginActivity.class);
            startActivity(loggInnIntent);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView userId = headerView.findViewById(R.id.user_id_tv);
        userId.setText(prefs.getString(USERNAME, null));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void registrerPlast(View view){
        clearPreferences();
        Intent messageIntent = new Intent(this, CategoryActivity.class);
        startActivity(messageIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_my_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MyProfileFragment())
                        .commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Logout(View view) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(USERNAME);
        editor.apply();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }
    // Resetter verdiene som lagres under registrering
    private void clearPreferences(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(MAIN_CATEGORY);
        editor.remove(TYPE);
        editor.remove(SIZE);
        editor.remove(IMAGEVIEW_HEIGHT);
        editor.remove(IMAGEVIEW_WIDTH);
        editor.remove(IMAGE_FILE_NAME);
        editor.remove(CURRENT_PHOTO_PATH);

        int size = prefs.getInt("Checksvar" + "_size", 0);
        for (int i = 0; i < size; i++)
            editor.remove("Checksvar" + "_" + i);
        editor.remove("Checksvar_size");
        editor.apply();
    }

    public void goToWeb(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://itfag.usn.no/grupper/v19gr2/plast/web/index.php"));
        startActivity(browserIntent);
    }
}
