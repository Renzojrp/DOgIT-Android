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
import pe.com.dogit.models.Request;

public class AboutRequestActivity extends AppCompatActivity {

    TextView nameTextView;
    ANImageView photoANImageView;
    TextView nameUserTextView;
    ANImageView photoUserANImageView;
    TextView descriptionTextView;

    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        photoUserANImageView = findViewById(R.id.photoUserANImageView);
        nameUserTextView = findViewById(R.id.nameUserTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        request = DOgITApp.getInstance().getCurrentRequest();

        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(request.getPublication().getPet().getPhoto());
        nameTextView.setText(request.getPublication().getPet().getName());
        photoUserANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setImageUrl(request.getUser().getPhoto());
        nameUserTextView.setText(request.getUser().getName());
        descriptionTextView.setText(request.getMessage());
    }

}
