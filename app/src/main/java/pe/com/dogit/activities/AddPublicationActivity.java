package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddPublicationActivity extends AppCompatActivity {

    private ANImageView photoANImageView;
    Button galeryButton;
    private TextInputLayout descriptionTextInputLayout;
    private TextInputLayout addressTextInputLayout;
    private TextInputLayout requirementsTextInputLayout;
    private Spinner petSpinner;

    Boolean correctDescription = false;
    Boolean correctAddress = false;
    Boolean correctRequirements = false;

    List<String> namePet = new ArrayList<>();
    List<String> idPet = new ArrayList<>();
    String idPetSelected;
    Integer position;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        requirementsTextInputLayout = findViewById(R.id.requirementsTextInputLayout);
        petSpinner = findViewById(R.id.petSpinner);

        user = DOgITApp.getInstance().getMyUser();

        for(int i = 0; i<DOgITApp.getInstance().getCurrentPets().size(); i++) {
            idPet.add(DOgITApp.getInstance().getCurrentPets().get(i).getId());
            namePet.add(DOgITApp.getInstance().getCurrentPets().get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namePet);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(dataAdapter);

        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                idPetSelected = idPet.get(pos);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPets().get(pos).getPhoto());
                position = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idPetSelected = idPet.get(0);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPets().get(0).getPhoto());
                position = 0;
            }
        });

        layoutByOrigin();
    }

    public void layoutByOrigin() {
        if (DOgITApp.getInstance().getCurrentPublication() != null) {
            requirementsTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getRequirements());
            descriptionTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getDescription());
            addressTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getAddress());
            for(int i=0;i<idPet.size();i++) {
                if(idPet.get(i).equals(DOgITApp.getInstance().getCurrentPublication().getPet().getId())) {
                    position = i;
                    petSpinner.setSelection(position);
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if(DOgITApp.getInstance().getCurrentPublication() == null) {
                    finish();
                } else {
                    deletePublication();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void floatingActionButtonClick(View view) {

        if(descriptionTextInputLayout.getEditText().getText().toString().length() == 0) {
            descriptionTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDescription = false;
        } else {
            descriptionTextInputLayout.setError(null);
            correctDescription = true;
        }

        if(addressTextInputLayout.getEditText().getText().toString().length() == 0) {
            addressTextInputLayout.setError(getResources().getString(R.string.empty_location));
            correctAddress = false;
        } else {
            addressTextInputLayout.setError(null);
            correctAddress = true;
        }

        if(requirementsTextInputLayout.getEditText().getText().toString().length() == 0) {
            requirementsTextInputLayout.setError(getResources().getString(R.string.empty_requirements));
            correctRequirements = false;
        } else {
            requirementsTextInputLayout.setError(null);
            correctRequirements = true;
        }

        if(correctDescription && correctAddress && correctRequirements) {
            if (DOgITApp.getInstance().getCurrentPublication() == null) {
                savePublication();
            } else {
                editPublication();
            }
        }
    }

    private void savePublication() {
        AndroidNetworking.post(DOgITService.PUBLICATION_URL)
                .addBodyParameter("user", user.getId())
                .addBodyParameter("pet", idPetSelected)
                .addBodyParameter("requirements", requirementsTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("address", addressTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.publication_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editPublication() {
        AndroidNetworking.put(DOgITService.PUBLICATION_EDIT_URL)
                .addPathParameter("publication_id", DOgITApp.getInstance().getCurrentPublication().getId())
                .addBodyParameter("pet", idPetSelected)
                .addBodyParameter("requirements", requirementsTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("address", addressTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().getCurrentPublication().setPet(DOgITApp.getInstance().getCurrentPets().get(position));
                        DOgITApp.getInstance().getCurrentPublication().setDescription(descriptionTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentPublication().setAddress(addressTextInputLayout.getEditText().getText().toString());

                        Toast.makeText(getApplicationContext(), R.string.publication_update, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_update, Toast.LENGTH_SHORT).show();
                    }
                });
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

}

