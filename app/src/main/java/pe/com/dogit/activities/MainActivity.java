package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.fragments.EventFragment;
import pe.com.dogit.fragments.MyPublicationFragment;
import pe.com.dogit.fragments.PetFragment;
import pe.com.dogit.fragments.UserFragment;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;

    User user;

    private List<Pet> pets;
    private static String TAG = "DOgIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        photoANImageView = headerView.findViewById(R.id.photoANImageView);
        displayNameTextView = headerView.findViewById(R.id.displayNameTextView);
        emailTextView = headerView.findViewById(R.id.emailTextView);

        user = DOgITApp.getInstance().getCurrentUser();

        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        displayNameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());

        navigateAccordingTo(R.id.nav_user);

        getPets();

    }

    private Fragment getFragmentFor(int id) {
        switch (id) {
            case R.id.nav_user: return new UserFragment();
            case R.id.nav_pet: return new PetFragment();
            case R.id.nav_my_publication: return new MyPublicationFragment();
            case R.id.nav_event: return new EventFragment();
        }
        return null;
    }

    private boolean navigateAccordingTo(int id) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, getFragmentFor(id))
                    .commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_pet) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_my_publication) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_event) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_sesion) {
            SplashActivity.changeData(MainActivity.this,"user" ,"");
            SplashActivity.changeData(MainActivity.this,"password" ,"");
            SplashActivity.changeData(MainActivity.this,"token" ,"");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPets() {
        AndroidNetworking
                .get(DOgITService.PET_USER_URL)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            pets = Pet.build(response.getJSONArray("pets"));
                            Log.d(TAG, "Found Pets: " + String.valueOf(pets.size()));
                            DOgITApp.getInstance().setCurrentPets(pets);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DOgITApp.getInstance().setCurrentPet(null);
        DOgITApp.getInstance().setCurrentEvent(null);
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        displayNameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        getPets();
    }
}
