package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Adoption;

public class AboutAdoptionActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView dateTextView;
    TextView requirementTextView;

    Adoption adoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_adoption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionBeforeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        dateTextView = findViewById(R.id.dateTextView);
        requirementTextView = findViewById(R.id.requirementTextView);

        adoption = DOgITApp.getInstance().getCurrentAdoption();

        setAdoptionInformation();
    }

    private void setAdoptionInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(adoption.getPublication().getPet().getPhoto());
        nameTextView.setText(adoption.getPublication().getPet().getName());
        descriptionTextView.setText(adoption.getPublication().getDescription());
        addressTextView.setText(adoption.getPublication().getAddress());
        requirementTextView.setText(adoption.getPublication().getRequirements());
        String date = adoption.getPublication().getPublicationDate().substring(0,10);
        String year = date.substring(0,4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        dateTextView.setText(day + "/" + month + "/" + year);
    }

}
