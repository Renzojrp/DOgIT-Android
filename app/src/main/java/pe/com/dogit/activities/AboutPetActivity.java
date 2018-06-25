package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import pe.com.dogit.models.Pet;
import pe.com.dogit.network.DOgITService;

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
        if (pet.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
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
                Intent intent = new Intent(this, AddPetActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_delete:
                deletePet();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deletePet() {
        AndroidNetworking.put(DOgITService.PET_EDIT_URL)
                .addPathParameter("pet_id", DOgITApp.getInstance().getCurrentPet().getId())
                .addBodyParameter("status", "N")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.pet_delete, Toast.LENGTH_SHORT).show();
                        DOgITApp.getInstance().setCurrentPet(null);
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_delete, Toast.LENGTH_SHORT).show();
                    }
                });
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
