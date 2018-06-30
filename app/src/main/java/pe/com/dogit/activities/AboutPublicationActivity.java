package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Publication;
import pe.com.dogit.network.DOgITService;

public class AboutPublicationActivity extends AppCompatActivity {

    ANImageView photoUserANImageView;
    TextView nameUserTextView;
    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView dateTextView;
    TextView requirementTextView;
    FloatingActionButton sendRequestFloatingActionButton;

    Publication publication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_publication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoUserANImageView = findViewById(R.id.photoUserANImageView);
        photoANImageView = findViewById(R.id.photoANImageView);
        nameUserTextView = findViewById(R.id.nameUserTextView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionBeforeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        dateTextView = findViewById(R.id.dateTextView);
        requirementTextView = findViewById(R.id.requirementTextView);
        sendRequestFloatingActionButton = findViewById(R.id.sendRequestFloatingActionButton);

        publication = DOgITApp.getInstance().getCurrentPublication();

        setPublicationInformation();

        if (DOgITApp.getInstance().getMyUser().getType().equals("admin")) {
            sendRequestFloatingActionButton.setVisibility(View.INVISIBLE);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        if (publication.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_button_edit, menu);
        } else {
            if (DOgITApp.getInstance().getMyUser().getType().equals("admin")) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_button_delete, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(publication.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                    Intent intent = new Intent(this, AddPublicationActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_edit_publication, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                deletePublication();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deletePublication() {
        AndroidNetworking.delete(DOgITService.PUBLICATION_EDIT_URL)
                .addPathParameter("publication_id", DOgITApp.getInstance().getCurrentPublication().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().setCurrentPublication(null);
                        Toast.makeText(getApplicationContext(), R.string.publication_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_delete, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setPublicationInformation() {
        photoUserANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setImageUrl(publication.getUser().getPhoto());
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(publication.getPet().getPhoto());
        nameUserTextView.setText(publication.getUser().getName() + " " + publication.getUser().getLastName());
        nameTextView.setText(publication.getPet().getName());
        descriptionTextView.setText(publication.getDescription());
        addressTextView.setText(publication.getAddress());
        requirementTextView.setText(publication.getRequirements());
        String date = publication.getPublicationDate().substring(0,10);
        String year = date.substring(0,4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        dateTextView.setText(day + "/" + month + "/" + year);
    }

    public void goToSendRequestActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        SendRequestActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        setPublicationInformation();
        if(DOgITApp.getInstance().getCurrentPublication() == null) {
            finish();
        }
    }

}
