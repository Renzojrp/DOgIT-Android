package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Pet;

public class AboutPetActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView weightTextView;
    TextView rescueDateTextView;
    TextView sizeTextView;
    TextView genderTextView;

    Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        weightTextView = findViewById(R.id.weightTextView);
        rescueDateTextView = findViewById(R.id.rescueDateTextView);
        sizeTextView = findViewById(R.id.sizeTextView);
        genderTextView = findViewById(R.id.genderTextView);

        pet = DOgITApp.getInstance().getCurrentPet();

        setPeInformation();
    }

    private void setPeInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(pet.getPhoto());
        nameTextView.setText(pet.getName());
        descriptionTextView.setText(pet.getDescription());
        weightTextView.setText(String.valueOf(pet.getWeigth()) + " Kg");
        rescueDateTextView.setText((pet.getRescueDate()));
        sizeTextView.setText(String.valueOf(pet.getSize()) + " cm");
        if (pet.getGender().equals("1")) {
            genderTextView.setText(getResources().getString(R.string.male_gender));
        } else {
            if (pet.getGender().equals("2")) {
                genderTextView.setText(getResources().getString(R.string.female_gender));
            } else {
                genderTextView.setText("");
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, AddPetActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPeInformation();
        if(DOgITApp.getInstance().getCurrentPet() == null) {
            finish();
        }
    }

}
