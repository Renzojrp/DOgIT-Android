package pe.com.dogit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddPostadoptionActivity extends AppCompatActivity {


    private ANImageView photoPostAdotionANImageView;
    private ANImageView photoANImageView;
    Button galeryButton;
    private TextInputLayout descriptionTextInputLayout;
    private Spinner petSpinner;
    private List<Adoption> adoptions = new ArrayList<>();;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;
    private Uri uriSavedImage;
    private UploadTask uploadTask;

    List<Adoption> petAdoption = new ArrayList<>();

    User user;

    Boolean correctUrl = false;
    Boolean correctDescription = false;

    List<String> namePet = new ArrayList<>();
    List<String> idPet = new ArrayList<>();
    String idPetSelected;
    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_postadoption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoPostAdotionANImageView = findViewById(R.id.photoPostAdotionANImageView);
        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);

        petSpinner = findViewById(R.id.petSpinner);

        user = DOgITApp.getInstance().getMyUser();

        getAdoptions();

        storageReference = FirebaseStorage.getInstance().getReference();

        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                idPetSelected = idPet.get(pos);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(petAdoption.get(pos).getPublication().getPet().getPhoto());
                position = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idPetSelected = idPet.get(0);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(petAdoption.get(0).getPublication().getPet().getPhoto());
                position = 0;
            }
        });
    }

    private void getAdoptions() {
        AndroidNetworking
                .get(DOgITService.ADOPTION_USER_URL)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;

                        try {
                            adoptions = Adoption.build(response.getJSONArray("adoptions"));
                            for(int i = 0; i<adoptions.size(); i++) {
                                petAdoption.add(adoptions.get(i));
                                idPet.add(adoptions.get(i).getId());
                                namePet.add(adoptions.get(i).getPublication().getPet().getName());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( AddPostadoptionActivity.this, android.R.layout.simple_spinner_item, namePet);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            petSpinner.setAdapter(dataAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    public void floatingActionButtonClick(View view) {

        if(descriptionTextInputLayout.getEditText().getText().toString().length() == 0) {
            descriptionTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDescription = false;
        } else {
            descriptionTextInputLayout.setError(null);
            correctDescription = true;
        }

        if( url == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_photo, Toast.LENGTH_SHORT ).show();
            correctUrl = false;
        } else {
            correctUrl = true;
        }

        if(correctDescription && correctUrl) {
            savePostAdoption();
        }
    }

    private void savePostAdoption() {
        AndroidNetworking.post(DOgITService.POSTADOPTION_URL)
                .addBodyParameter("user", user.getId())
                .addBodyParameter("photo", url.toString())
                .addBodyParameter("adoption", idPetSelected)
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
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
                        Toast.makeText(getApplicationContext(), R.string.error_pet_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void galeryClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            uriSavedImage = data.getData();
            final StorageReference filepath = storageReference.child("user").child(uriSavedImage.getLastPathSegment());
            uploadTask = filepath.putFile(uriSavedImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        url = task.getResult();
                        photoPostAdotionANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                        photoPostAdotionANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                        photoPostAdotionANImageView.setImageUrl(url.toString());
                    }
                }
            });
        }
    }

}
