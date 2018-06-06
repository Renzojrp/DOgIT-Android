package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddPetActivity extends AppCompatActivity {

    private ANImageView photoANImageView;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout descriptionTextInputLayout;
    private TextInputLayout weigthTextInputLayout;
    private TextInputLayout sizeTextInputLayout;
    private TextInputLayout ageTextInputLayout;
    private Spinner genderSpinner;
    private EditText rescueEditText;

    Boolean correctName = false;
    Boolean correctDescription = false;
    Boolean correctWeigth = false;
    Boolean correctSize = false;
    Boolean correctAge = false;
    Boolean correctGender = false;
    Boolean correctUrl = false;

    int day;
    int month;
    int year;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;
    private Uri uriSavedImage;
    private UploadTask uploadTask;

    User user;
    List<Publication> publications;
    List<String> gender = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = DOgITApp.getInstance().getCurrentUser();

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        weigthTextInputLayout = findViewById(R.id.weigthTextInputLayout);
        sizeTextInputLayout = findViewById(R.id.sizeTextInputLayout);
        ageTextInputLayout = findViewById(R.id.ageTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);
        rescueEditText = findViewById(R.id.rescueEditText);
        rescueEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });

        gender.add(getResources().getString(R.string.male_gender));
        gender.add(getResources().getString(R.string.female_gender));

        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        layoutByOrigin();
    }

    public void layoutByOrigin() {
        if(DOgITApp.getInstance().getCurrentPet() != null) {
            url = Uri.parse(DOgITApp.getInstance().getCurrentPet().getPhoto());
            photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
            photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPet().getPhoto());
            nameTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPet().getName());
            descriptionTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPet().getDescription());
            weigthTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPet().getWeigth().toString());
            sizeTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentPet().getSize().toString());
            ageTextInputLayout.getEditText().setText(String.valueOf(DOgITApp.getInstance().getCurrentPet().getAge()));
            rescueEditText.setText(DOgITApp.getInstance().getCurrentPet().getRescueDate());
            if (DOgITApp.getInstance().getCurrentPet().getGender().equals("0")) {
                genderSpinner.setSelection(0);
            } else {
                if (DOgITApp.getInstance().getCurrentPet().getGender().equals("1")) {
                    genderSpinner.setSelection(1);
                }
            }
        }
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
                        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                        photoANImageView.setImageUrl(url.toString());
                    }
                }
            });
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
                if(DOgITApp.getInstance().getCurrentPet() == null) {
                    finish();
                } else {
                    getMyPublications();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void floatingActionButtonClick(View view) {
        if(nameTextInputLayout.getEditText().getText().toString().length() == 0) {
            nameTextInputLayout.setError(getResources().getString(R.string.empty_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if(descriptionTextInputLayout.getEditText().getText().toString().length() == 0) {
            descriptionTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDescription = false;
        } else {
            descriptionTextInputLayout.setError(null);
            correctDescription = true;
        }

        if(weigthTextInputLayout.getEditText().getText().toString().length() == 0) {
            weigthTextInputLayout.setError(getResources().getString(R.string.empty_weigth));
            correctWeigth = false;
        } else {
            weigthTextInputLayout.setError(null);
            correctWeigth = true;
        }

        if(sizeTextInputLayout.getEditText().getText().toString().length() == 0) {
            sizeTextInputLayout.setError(getResources().getString(R.string.empty_size));
            correctSize = false;
        } else {
            sizeTextInputLayout.setError(null);
            correctSize = true;
        }

        if(ageTextInputLayout.getEditText().getText().toString().length() == 0) {
            ageTextInputLayout.setError(getResources().getString(R.string.empty_age));
            correctAge = false;
        } else {
            ageTextInputLayout.setError(null);
            correctAge = true;
        }

        if( url == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_photo, Toast.LENGTH_SHORT ).show();
            correctUrl = false;
        } else {
            correctUrl = true;
        }

        if(genderSpinner.getSelectedItemPosition() == 3) {
            Toast.makeText(getApplicationContext(), R.string.invalid_gender, Toast.LENGTH_SHORT).show();
        } else {
            correctGender = true;
        }

        if(correctName && correctDescription && correctWeigth && correctSize && correctAge && correctUrl && correctGender) {
            if (DOgITApp.getInstance().getCurrentPet() == null) {
                savePet();
            } else {
                editPet();
            }
        }
    }

    public void onDateButton() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                rescueEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void savePet() {
        AndroidNetworking.post(DOgITService.PET_URL)
                .addBodyParameter("user", user.getId())
                .addBodyParameter("name", nameTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("weigth", weigthTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("size", sizeTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("age", ageTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("photo", url.toString())
                .addBodyParameter("gender", Long.toString(genderSpinner.getSelectedItemId()))
                .addBodyParameter("rescue_date", rescueEditText.getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.pet_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editPet() {
        AndroidNetworking.put(DOgITService.PET_EDIT_URL)
                .addPathParameter("pet_id", DOgITApp.getInstance().getCurrentPet().getId())
                .addBodyParameter("name", nameTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("weigth", weigthTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("size", sizeTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("age", ageTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("photo", url.toString())
                .addBodyParameter("gender", Long.toString(genderSpinner.getSelectedItemId()))
                .addBodyParameter("rescue_date", rescueEditText.getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().getCurrentPet().setName(nameTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentPet().setDescription(descriptionTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentPet().setWeigth(Double.parseDouble(weigthTextInputLayout.getEditText().getText().toString()));
                        DOgITApp.getInstance().getCurrentPet().setId(sizeTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentPet().setId(ageTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentPet().setPhoto(url.toString());
                        DOgITApp.getInstance().getCurrentPet().setGender(Long.toString(genderSpinner.getSelectedItemId()));
                        DOgITApp.getInstance().getCurrentPet().setRescueDate(rescueEditText.getText().toString());
                        Toast.makeText(getApplicationContext(), R.string.pet_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePet() {
        AndroidNetworking.delete(DOgITService.PET_EDIT_URL)
                .addPathParameter("pet_id", DOgITApp.getInstance().getCurrentPet().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().setCurrentPet(null);
                        Toast.makeText(getApplicationContext(), R.string.pet_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_delete, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMyPublications() {
        AndroidNetworking
                .get(DOgITService.PUBLICATION_PET_URL)
                .addPathParameter("pet_id", DOgITApp.getInstance().getCurrentPet().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            publications = Publication.build(response.getJSONArray("publications"));
                            if(publications.size() == 0) {
                                deletePet();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_pet_publication, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_publication, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
