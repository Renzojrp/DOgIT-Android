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
import pe.com.dogit.models.Postadoption;

public class AboutPostadoptionActivity extends AppCompatActivity {

    TextView nameTextView;
    ANImageView photoBeforeANImageView;
    ANImageView photoAfterANImageView;
    TextView descriptionBeforeTextView;
    TextView descriptionAfterTextView;

    Postadoption postadoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_postadoption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTextView = findViewById(R.id.nameTextView);
        photoBeforeANImageView = findViewById(R.id.photoBeforeANImageView);
        photoAfterANImageView = findViewById(R.id.photoAfterANImageView);
        descriptionBeforeTextView = findViewById(R.id.descriptionBeforeTextView);
        descriptionAfterTextView = findViewById(R.id.descriptionAfterTextView);

        postadoption = DOgITApp.getInstance().getCurrentPostadoption();
        nameTextView.setText(postadoption.getAdoption().getPublication().getPet().getName());
        photoBeforeANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoBeforeANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoBeforeANImageView.setImageUrl(postadoption.getAdoption().getPublication().getPet().getPhoto());
        photoAfterANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoAfterANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoAfterANImageView.setImageUrl(postadoption.getPhoto());
        descriptionBeforeTextView.setText(postadoption.getAdoption().getPublication().getPet().getDescription());
        descriptionAfterTextView.setText(postadoption.getDescription());
    }

}
