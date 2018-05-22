package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
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
import java.util.Objects;
import java.util.regex.Pattern;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class EditUserProfileActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextInputLayout nameTextInputLayout;
    TextInputLayout lastNameTextInputLayout;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextInputLayout dniTextInputLayout;
    TextInputLayout addressTextInputLayout;
    TextInputLayout mobilePhoneTextInputLayout;
    TextInputLayout workPlaceTextInputLayout;
    Spinner genderSpinner;
    EditText birthDateEditText;

    public final Calendar c = Calendar.getInstance();

    private int day;
    private int month;
    private int year;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;
    private Uri uriSavedImage;
    private UploadTask uploadTask;

    List<String> gender = new ArrayList<>();

    User user;
    String TAG = "DOgIT";

    boolean correctEmail = false;
    boolean correctPassword= false;
    boolean correctName = false;
    boolean correctLastName = false;
    boolean correctDNI = false;
    boolean correctAddress = false;
    boolean correctMobilePhone = false;
    boolean correctWorkPlace = false;
    boolean correctBirthDate = false;
    boolean correctGender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        mobilePhoneTextInputLayout = findViewById(R.id.mobilePhoneTextInputLayout);
        dniTextInputLayout = findViewById(R.id.dniTextInputLayout);
        workPlaceTextInputLayout = findViewById(R.id.workPlaceTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });
        user = DOgITApp.getInstance().getCurrentUser();

        url = Uri.parse(user.getPhoto());
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        nameTextInputLayout.getEditText().setText(user.getName());
        lastNameTextInputLayout.getEditText().setText(user.getLastName());
        emailTextInputLayout.getEditText().setText(user.getEmail());
        passwordTextInputLayout.getEditText().setText(user.getPassword());
        mobilePhoneTextInputLayout.getEditText().setText(user.getMobilePhone());
        birthDateEditText.setText(user.getBirthDate());
        dniTextInputLayout.getEditText().setText(String.valueOf(user.getDni()));
        workPlaceTextInputLayout.getEditText().setText(user.getWorkPlace());
        addressTextInputLayout.getEditText().setText(user.getAddress());

        gender.add("");
        gender.add(getResources().getString(R.string.male_gender));
        gender.add(getResources().getString(R.string.female_gender));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        if (user.getGender().equals("1")) {
            genderSpinner.setSelection(1);
        } else {
            if (user.getGender().equals("2")) {
                genderSpinner.setSelection(2);
            } else {
                genderSpinner.setSelection(0);
            }
        }

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void onDateButton() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthDateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void updateUserClick(View view) {
        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputLayout.getEditText().getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Záéíóú ]+$");
        if (!pattern.matcher(nameTextInputLayout.getEditText().getText().toString()).matches()
                || nameTextInputLayout.getEditText().getText().toString().length() > 30) {
            nameTextInputLayout.setError(getResources().getString(R.string.invalid_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if (!pattern.matcher(lastNameTextInputLayout.getEditText().getText().toString()).matches()
                || lastNameTextInputLayout.getEditText().getText().toString().length() > 30) {
            lastNameTextInputLayout.setError(getResources().getString(R.string.invalid_lastName));
            correctLastName = false;
        } else {
            lastNameTextInputLayout.setError(null);
            correctLastName = true;
        }

        if(passwordTextInputLayout.getEditText().getText().toString().length() < 8) {
            passwordTextInputLayout.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
        } else {
            passwordTextInputLayout.setError(null);
            correctPassword = true;
        }

        if(dniTextInputLayout.getEditText().getText().toString().length() != 8) {
            dniTextInputLayout.setError(getResources().getString(R.string.invalid_dni));
            correctDNI = false;
        } else {
            dniTextInputLayout.setError(null);
            correctDNI = true;
        }

        if(addressTextInputLayout.getEditText().getText().toString().length() == 0) {
            addressTextInputLayout.setError(getResources().getString(R.string.invalid_address));
            correctAddress = false;
        } else {
            addressTextInputLayout.setError(null);
            correctAddress = true;
        }

        if(workPlaceTextInputLayout.getEditText().getText().toString().length() == 0) {
            workPlaceTextInputLayout.setError(getResources().getString(R.string.invalid_workPlace));
            correctWorkPlace = false;
        } else {
            workPlaceTextInputLayout.setError(null);
            correctWorkPlace = true;
        }

        if(mobilePhoneTextInputLayout.getEditText().getText().toString().length() == 0) {
            mobilePhoneTextInputLayout.setError(getResources().getString(R.string.invalid_mobilePhone));
            correctMobilePhone = false;
        } else {
            mobilePhoneTextInputLayout.setError(null);
            correctMobilePhone = true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(birthDateEditText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        if(c.getTime().compareTo(date) < 0) {
            correctBirthDate = false;
            Toast.makeText(getApplicationContext(), R.string.invalid_birthdate, Toast.LENGTH_SHORT).show();
        } else {
            correctBirthDate = true;
        }

        if(genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.invalid_gender, Toast.LENGTH_SHORT).show();
        } else {
            correctGender = true;
        }

        if(correctEmail && correctPassword && correctName && correctLastName && correctMobilePhone && correctWorkPlace
                && correctAddress && correctDNI && correctBirthDate && correctGender) {
            editProfile();
        }
    }

    private void editProfile() {
        AndroidNetworking.put(DOgITService.USERS_EDIT_URL)
                .addBodyParameter("photo", url.toString())
                .addBodyParameter("email", Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("name", Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("lastName", Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("password", Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("dni", Objects.requireNonNull(dniTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("workPlace", Objects.requireNonNull(workPlaceTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("address", Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("mobilePhone", Objects.requireNonNull(mobilePhoneTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("gender", Long.toString(genderSpinner.getSelectedItemId()))
                .addBodyParameter("birthDate", birthDateEditText.getText().toString())
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        user.setPhoto(url.toString());
                        user.setName(nameTextInputLayout.getEditText().getText().toString());
                        user.setLastName(lastNameTextInputLayout.getEditText().getText().toString());
                        user.setEmail(emailTextInputLayout.getEditText().getText().toString());
                        user.setPassword(passwordTextInputLayout.getEditText().getText().toString());
                        user.setMobilePhone(mobilePhoneTextInputLayout.getEditText().getText().toString());
                        user.setBirthDate(birthDateEditText.getText().toString());
                        user.setGender(Long.toString(genderSpinner.getSelectedItemId()));
                        user.setDni(Integer.parseInt(dniTextInputLayout.getEditText().getText().toString()));
                        user.setAddress(addressTextInputLayout.getEditText().getText().toString());
                        user.setWorkPlace(workPlaceTextInputLayout.getEditText().getText().toString());
                        Toast.makeText(getApplicationContext(), R.string.profile_update, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_profile_update, Toast.LENGTH_SHORT).show();
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
            StorageReference filepath = storageReference.child("user").child(uriSavedImage.getLastPathSegment());
            uploadTask = filepath.putFile(uriSavedImage);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditUserProfileActivity.this, "Se subio exitosamente", Toast.LENGTH_SHORT).show();
                    url = taskSnapshot.getDownloadUrl();
                    photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                    photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                    photoANImageView.setImageUrl(url.toString());
                }
            });
        }
    }

    public void cancelClick(View v) {
        finish();
    }

}