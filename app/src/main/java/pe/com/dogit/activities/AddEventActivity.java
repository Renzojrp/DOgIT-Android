package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddEventActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    Button galeryButton;
    TextInputLayout nameTextInputLayout;
    EditText dateEditText;
    TextInputLayout detailTextInputLayout;
    TextInputLayout locationTextInputLayout;
    TextInputLayout capabilityTextInputLayout;

    Boolean correctName = false;
    Boolean correctDetail= false;
    Boolean correctDate = false;
    Boolean correctLocation = false;
    Boolean correctCapability = false;
    Boolean correctUrl = false;

    int day;
    int month;
    int year;

    User user;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;
    private Uri uriSavedImage;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        dateEditText = findViewById(R.id.dateEditText);
        detailTextInputLayout = findViewById(R.id.detailTextInputLayout);
        locationTextInputLayout = findViewById(R.id.locationTextInputLayout);
        capabilityTextInputLayout = findViewById(R.id.capabilityTextInputLayout);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        user = DOgITApp.getInstance().getMyUser();

        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                correctDate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        layoutByOrigin();
    }

    public void layoutByOrigin() {
        if(DOgITApp.getInstance().getCurrentEvent() != null) {
            url = Uri.parse(DOgITApp.getInstance().getCurrentEvent().getPhoto());
            photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
            photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentEvent().getPhoto());
            nameTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentEvent().getName());
            detailTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentEvent().getDescription());
            dateEditText.setText(DOgITApp.getInstance().getCurrentEvent().getDate());
            locationTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentEvent().getAddress());
            capabilityTextInputLayout.getEditText().setText(String.valueOf(DOgITApp.getInstance().getCurrentEvent().getCapacity().toString()));
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
                if(DOgITApp.getInstance().getCurrentEvent() == null) {
                    finish();
                } else {
                    deleteEvent();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
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
    public void onDateButton() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void floatingActionButtonClick(View view) {
        if(nameTextInputLayout.getEditText().getText().toString().length() == 0) {
            nameTextInputLayout.setError(getResources().getString(R.string.empty_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if(detailTextInputLayout.getEditText().getText().toString().length() == 0) {
            detailTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDetail = false;
        } else {
            detailTextInputLayout.setError(null);
            correctDetail = true;
        }

        if (correctDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = sdf.parse(dateEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            if(c.getTime().compareTo(date) > 0) {
                correctDate = false;
                Toast.makeText(getApplicationContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
            } else {
                correctDate = true;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
        }

        if(locationTextInputLayout.getEditText().getText().toString().length() == 0) {
            locationTextInputLayout.setError(getResources().getString(R.string.empty_location));
            correctLocation = false;
        } else {
            locationTextInputLayout.setError(null);
            correctLocation = true;
        }

        if(capabilityTextInputLayout.getEditText().getText().toString().length() == 0) {
            capabilityTextInputLayout.setError(getResources().getString(R.string.empty_capability));
            correctCapability = false;
        } else {
            capabilityTextInputLayout.setError(null);
            correctCapability = true;
        }

        if( url == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_photo, Toast.LENGTH_SHORT ).show();
            correctUrl = false;
        } else {
            correctUrl = true;
        }

        if(correctName && correctDetail && correctDate && correctLocation && correctCapability && correctUrl) {
            if (DOgITApp.getInstance().getCurrentEvent() == null) {
                saveEvent();
            } else {
                editEvent();
            }
        }
    }

    private void saveEvent() {
        AndroidNetworking.post(DOgITService.EVENT_URL)
                .addBodyParameter("user", user.getId())
                .addBodyParameter("name", nameTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("description", detailTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("date", dateEditText.getText().toString())
                .addBodyParameter("address", locationTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("capacity", capabilityTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("photo", url.toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.event_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_event_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editEvent() {
        AndroidNetworking.put(DOgITService.EVENT_EDIT_URL)
                .addPathParameter("event_id", DOgITApp.getInstance().getCurrentEvent().getId())
                .addBodyParameter("photo", url.toString())
                .addBodyParameter("name", nameTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("date", dateEditText.getText().toString())
                .addBodyParameter("description", detailTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("location",locationTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("capability",capabilityTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().getCurrentEvent().setPhoto(url.toString());
                        DOgITApp.getInstance().getCurrentEvent().setName(nameTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentEvent().setDate(dateEditText.getText().toString());
                        DOgITApp.getInstance().getCurrentEvent().setDescription(detailTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentEvent().setAddress(locationTextInputLayout.getEditText().getText().toString());
                        DOgITApp.getInstance().getCurrentEvent().setCapacity(Integer.parseInt(capabilityTextInputLayout.getEditText().getText().toString()));

                        Toast.makeText(getApplicationContext(), R.string.event_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_event_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteEvent() {
        AndroidNetworking.put(DOgITService.EVENT_EDIT_URL)
                .addPathParameter("event_id", DOgITApp.getInstance().getCurrentEvent().getId())
                .addBodyParameter("status","N")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().setCurrentEvent(null);
                        Toast.makeText(getApplicationContext(), R.string.event_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_event_delete, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
