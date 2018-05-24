package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddMyPublicationActivity extends AppCompatActivity {

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

    int day;
    int month;
    int year;

    User user;

    String TAG = "DOgIT";
    private static final int GALERY_INTENT = 1;

    private StorageReference storageReference;
    private Uri url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_publication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        requirementsTextInputLayout = findViewById(R.id.requirementsTextInputLayout);
        petSpinner = findViewById(R.id.petSpinner);

        user = DOgITApp.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        user = DOgITApp.getInstance().getCurrentUser();

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idPetSelected = idPet.get(0);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPets().get(0).getPhoto());
            }
        });

        layoutByOrigin();
    }

    public void layoutByOrigin() {
        if (DOgITApp.getInstance().getCurrentEvent() != null) {
            url = Uri.parse(DOgITApp.getInstance().getCurrentEvent().getPhoto());
            photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
            photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentEvent().getPhoto());
            requirementsTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getRequirements());
            descriptionTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getDescription());
            addressTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPublication().getAddress());

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
                .setTag(TAG)
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
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //DOgITApp.getInstance().getCurrentPublication().setPhoto(url.toString());
                        //DOgITApp.getInstance().getCurrentPublication().setPet(petTextInputLayout.getEditText().getText().toString());
                        //DOgITApp.getInstance().getCurrentPublication().setDate(dateEditText.getText().toString());
                        //DOgITApp.getInstance().getCurrentPublication().setDescription(descriptionTextInputLayout.getEditText().getText().toString());
                        //DOgITApp.getInstance().getCurrentPublication().setAddress(addressTextInputLayout.getEditText().getText().toString());

                        Toast.makeText(getApplicationContext(), R.string.publication_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePublication() {
        AndroidNetworking.delete(DOgITService.PUBLICATION_EDIT_URL)
                .addPathParameter("publication_id", DOgITApp.getInstance().getCurrentPublication().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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

