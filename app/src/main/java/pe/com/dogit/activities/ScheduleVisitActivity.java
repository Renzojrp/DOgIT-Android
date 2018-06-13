package pe.com.dogit.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.network.DOgITService;

public class ScheduleVisitActivity extends AppCompatActivity {

    TextView nameTextView;
    ANImageView photoANImageView;
    TextView nameUserTextView;
    ANImageView photoUserANImageView;
    EditText dateEditText;
    TextInputLayout locationTextInputLayout;
    TextInputLayout messageTextInputLayout;

    Boolean correctDate = false;
    Boolean correctLocation = false;
    Boolean correctMessage = false;

    Adoption adoption;

    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_visit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTextView = findViewById(R.id.nameTextView);
        photoANImageView = findViewById(R.id.photoANImageView);
        nameUserTextView = findViewById(R.id.nameUserTextView);
        photoUserANImageView = findViewById(R.id.photoUserANImageView);
        dateEditText = findViewById(R.id.dateEditText);
        locationTextInputLayout = findViewById(R.id.locationTextInputLayout);
        messageTextInputLayout= findViewById(R.id.messageTextInputLayout);

        adoption = DOgITApp.getInstance().getCurrentAdoption();

        nameTextView.setText(adoption.getPublication().getPet().getName());
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(adoption.getPublication().getPet().getPhoto());
        nameUserTextView.setText(adoption.getUser().getName());
        photoUserANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setImageUrl(adoption.getUser().getPhoto());

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });

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

        if(messageTextInputLayout.getEditText().getText().toString().length() == 0) {
            messageTextInputLayout.setError(getResources().getString(R.string.empty_message));
            correctMessage = false;
        } else {
            messageTextInputLayout.setError(null);
            correctMessage = true;
        }

        if(correctDate && correctLocation) {
            saveVisit();
        }
    }

    private void saveVisit() {
        AndroidNetworking.post(DOgITService.VISIT_URL)
                .addBodyParameter("user", adoption.getUser().getId())
                .addBodyParameter("publication", adoption.getPublication().getId())
                .addBodyParameter("date", dateEditText.getText().toString())
                .addBodyParameter("place", locationTextInputLayout.getEditText().getText().toString())
                .addBodyParameter("message", messageTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.visit_created, Toast.LENGTH_SHORT).show();
                        sendMail();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_visit_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMail() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        if(session != null) {
            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(email));
                message.setSubject(getResources().getString(R.string.visit_mail_subject));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(adoption.getUser().getEmail()));
                message.setContent(adoption.getUser().getName() + " " + getResources().getString(R.string.response_mail_visit)
                        + " " + adoption.getPublication().getUser().getName() + " " + getResources().getString(R.string.response_mail_visit_to)
                            + " " + adoption.getPublication().getPet().getName() + " " + getResources().getString(R.string.response_mail_visit_date)
                                + " " + dateEditText.getText() + " " + getResources().getString(R.string.response_mail_visit_in) + " " +
                                    locationTextInputLayout.getEditText().getText(), "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(),  R.string.mail_send_visit, Toast.LENGTH_SHORT).show();
        finish();
    }

}
