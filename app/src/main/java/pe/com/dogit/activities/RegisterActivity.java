package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import pe.com.dogit.R;
import pe.com.dogit.network.DOgITService;

public class RegisterActivity extends AppCompatActivity {
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
    TextView signInTextView;
    Button signUpButton;
    ProgressBar signUpProgressBar;

    int day;
    int month;
    int year;

    boolean correctEmail = false;
    boolean correctPassword= false;
    boolean correctName = false;
    boolean correctLastName = false;
    boolean correctDNI = false;
    boolean correctWorkPlace = false;
    boolean correctAddress = false;
    boolean correctMobilePhone = false;
    boolean correctBirthDate = false;
    boolean correctGender = false;

    List<String> gender = new ArrayList<>();

    String TAG = "DOgIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        dniTextInputLayout = findViewById(R.id.dniTextInputLayout);
        workPlaceTextInputLayout = findViewById(R.id.workPlaceTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        mobilePhoneTextInputLayout = findViewById(R.id.mobilePhoneTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });
        signInTextView = findViewById(R.id.signInTextView);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        signUpButton = findViewById(R.id.signUpButton);
        signUpProgressBar.setVisibility(View.GONE);

        gender.add("");
        gender.add(getResources().getString(R.string.male_gender));
        gender.add(getResources().getString(R.string.female_gender));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        birthDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                correctBirthDate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    public void signUpClick(View v) {
        signUpProgressBar.setVisibility(View.VISIBLE);
        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputLayout.getEditText().getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Záéíóú ]+$");
        if (!pattern.matcher(Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString()).matches()
                || (nameTextInputLayout.getEditText().getText().toString().length() > 30)) {
            nameTextInputLayout.setError(getResources().getString(R.string.invalid_name));
            correctName = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if (!pattern.matcher(Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString()).matches()
                || lastNameTextInputLayout.getEditText().getText().toString().length() > 30) {
            lastNameTextInputLayout.setError(getResources().getString(R.string.invalid_lastName));
            correctLastName = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            lastNameTextInputLayout.setError(null);
            correctLastName = true;
        }

        if(Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().length() < 8) {
            passwordTextInputLayout.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            passwordTextInputLayout.setError(null);
            correctPassword = true;
        }

        if(Objects.requireNonNull(dniTextInputLayout.getEditText()).getText().toString().length() != 8) {
            dniTextInputLayout.setError(getResources().getString(R.string.invalid_dni));
            correctDNI = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            dniTextInputLayout.setError(null);
            correctDNI = true;
        }

        if(addressTextInputLayout.getEditText().getText().toString().length() == 0) {
            addressTextInputLayout.setError(getResources().getString(R.string.invalid_address));
            correctAddress = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            addressTextInputLayout.setError(null);
            correctAddress = true;
        }

        if(workPlaceTextInputLayout.getEditText().getText().toString().length() == 0) {
            workPlaceTextInputLayout.setError(getResources().getString(R.string.invalid_workPlace));
            correctWorkPlace = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            workPlaceTextInputLayout.setError(null);
            correctWorkPlace = true;
        }

        if(mobilePhoneTextInputLayout.getEditText().getText().toString().length() == 0) {
            mobilePhoneTextInputLayout.setError(getResources().getString(R.string.invalid_mobilePhone));
            correctMobilePhone = false;
            signUpProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mobilePhoneTextInputLayout.setError(null);
            correctMobilePhone = true;
        }

        if (correctBirthDate) {
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
                signUpProgressBar.setVisibility(View.INVISIBLE);
                correctBirthDate = true;
            }
        } else {
            signUpProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), R.string.invalid_birthdate, Toast.LENGTH_SHORT).show();
        }

        if(genderSpinner.getSelectedItemPosition() == 0) {
            signUpProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), R.string.invalid_gender, Toast.LENGTH_SHORT).show();
        } else {
            correctGender = true;
        }

        if(correctEmail && correctPassword && correctName && correctLastName && correctMobilePhone && correctWorkPlace
                && correctAddress && correctDNI && correctBirthDate && correctGender) {
            signUpUser();
        }
    }

    private void signUpUser() {
        AndroidNetworking.post(DOgITService.SIGNUP_URL)
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
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("Error")){
                                Toast.makeText(getApplicationContext(), R.string.user_exists, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.user_saved, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_user_saved, Toast.LENGTH_SHORT).show();
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void goToLoginActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }
}
