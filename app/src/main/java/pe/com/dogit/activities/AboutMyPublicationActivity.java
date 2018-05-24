package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Publication;

public class AboutMyPublicationActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView petTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView dateTextView;

    Publication publication;

    String TAG = "DOgIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_my_publication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        petTextView = findViewById(R.id.petTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        addressTextView = findViewById(R.id.addressTextView);
        dateTextView = findViewById(R.id.dateTextView);

        publication = DOgITApp.getInstance().getCurrentPublication();

        setPublicationInformation();
    }

    private void setPublicationInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(publication.getPet().getPhoto());
        petTextView.setText(publication.getPet().getName());
        descriptionTextView.setText(publication.getDescription());
        addressTextView.setText(publication.getAddress());
        dateTextView.setText((publication.getDate()));
    }



}
