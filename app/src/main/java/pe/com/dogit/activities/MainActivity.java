package pe.com.dogit.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import pe.com.dogit.fragments.AdoptionFragment;
import pe.com.dogit.fragments.BlogFragment;
import pe.com.dogit.fragments.EventFragment;
import pe.com.dogit.fragments.PostAdoptionFragment;
import pe.com.dogit.fragments.PublicationFragment;
import pe.com.dogit.fragments.PetFragment;
import pe.com.dogit.fragments.RequestFragment;
import pe.com.dogit.fragments.UserFragment;
import pe.com.dogit.fragments.VisitFragment;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;
    Toolbar toolbar;

    User user;

    private List<Pet> pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        photoANImageView = headerView.findViewById(R.id.photoANImageView);
        displayNameTextView = headerView.findViewById(R.id.displayNameTextView);
        emailTextView = headerView.findViewById(R.id.emailTextView);

        user = DOgITApp.getInstance().getMyUser();

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
            case R.id.nav_user:
                toolbar.setTitle(R.string.nav_option_user);
                return new UserFragment();
            case R.id.nav_pet:
                toolbar.setTitle(R.string.nav_option_pet);
                return new PetFragment();
            case R.id.nav_blog:
                toolbar.setTitle(R.string.nav_option_blog);
                return new BlogFragment();
            case R.id.nav_publication:
                toolbar.setTitle(R.string.nav_option_publication);
                return new PublicationFragment();
            case R.id.nav_request:
                toolbar.setTitle(R.string.nav_option_request);
                return new RequestFragment();
            case R.id.nav_adoption:
                toolbar.setTitle(R.string.nav_option_adoption);
                return new AdoptionFragment();
            case R.id.nav_post_adoption:
                toolbar.setTitle(R.string.nav_option_post_adoption);
                return new PostAdoptionFragment();
            case R.id.nav_visit:
                toolbar.setTitle(R.string.nav_option_visit);
                return new VisitFragment();
            case R.id.nav_event:
                toolbar.setTitle(R.string.nav_option_event);
                return new EventFragment();
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
        }  else if (id == R.id.nav_blog) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_publication) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_request) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_adoption) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_post_adoption) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_visit) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_event) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_sesion) {
            SplashActivity.changeData(MainActivity.this,"user" ,"");
            SplashActivity.changeData(MainActivity.this,"password" ,"");
            SplashActivity.changeData(MainActivity.this,"token" ,"");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            pets = Pet.build(response.getJSONArray("pets"));
                            DOgITApp.getInstance().setCurrentPets(pets);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DOgITApp.getInstance().setCurrentPet(null);
        DOgITApp.getInstance().setCurrentEvent(null);
        DOgITApp.getInstance().setCurrentPublication(null);
        DOgITApp.getInstance().setCurrentBlog(null);
        DOgITApp.getInstance().setCurrentRequest(null);
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        displayNameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        getPets();
    }
}
