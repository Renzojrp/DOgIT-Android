package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Assistence;
import pe.com.dogit.models.Event;
import pe.com.dogit.models.Pet;

public class AboutPetActivity extends AppCompatActivity {


    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView weightTextView;
    TextView rescueDateTextView;
    TextView sizeTextView;

    Pet pet;

    String TAG = "DOgIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        weightTextView = findViewById(R.id.weighTextView);
        rescueDateTextView = findViewById(R.id.rescueDateTextView);
        sizeTextView = findViewById(R.id.sizeTextView);

        pet = DOgITApp.getInstance().getCurrentPet();

        setPeInformation();
    }

    private void setPeInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(pet.getPhoto());
        nameTextView.setText(pet.getName());
        descriptionTextView.setText(pet.getDescription());
        weightTextView.setText(pet.getWeigth().toString());
        rescueDateTextView.setText((pet.getRescueDate()));
        sizeTextView.setText(pet.getSize().toString());
    }

}
