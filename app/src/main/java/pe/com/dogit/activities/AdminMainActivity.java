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

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.fragments.AdoptionFragment;
import pe.com.dogit.fragments.BlogFragment;
import pe.com.dogit.fragments.BlogsFragment;
import pe.com.dogit.fragments.EventFragment;
import pe.com.dogit.fragments.EventsFragment;
import pe.com.dogit.fragments.PetFragment;
import pe.com.dogit.fragments.PetsFragment;
import pe.com.dogit.fragments.PublicationFragment;
import pe.com.dogit.fragments.PublicationsFragment;
import pe.com.dogit.fragments.ReportFragment;
import pe.com.dogit.fragments.UserFragment;
import pe.com.dogit.fragments.UsersFragment;
import pe.com.dogit.fragments.VisitFragment;
import pe.com.dogit.models.User;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;
    Toolbar toolbar;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
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
    }

    private Fragment getFragmentFor(int id) {
        switch (id) {
            case R.id.nav_user:
                toolbar.setTitle(R.string.nav_option_user);
                return new UserFragment();
            case R.id.nav_users:
                toolbar.setTitle(R.string.nav_option_users);
                return new UsersFragment();
            case R.id.nav_blogs:
                toolbar.setTitle(R.string.nav_option_blogs);
                return new BlogsFragment();
            case R.id.nav_publications:
                toolbar.setTitle(R.string.nav_option_publications);
                return new PublicationsFragment();
            case R.id.nav_reports:
                toolbar.setTitle(R.string.nav_option_reports);
                return new ReportFragment();
            case R.id.nav_events:
                toolbar.setTitle(R.string.nav_option_events);
                return new EventsFragment();
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
        } else if (id == R.id.nav_users) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_blogs) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_publications) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_events) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_reports) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_sesion) {
            SplashActivity.changeData(AdminMainActivity.this,"user" ,"");
            SplashActivity.changeData(AdminMainActivity.this,"password" ,"");
            SplashActivity.changeData(AdminMainActivity.this,"token" ,"");
            startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    }
}
