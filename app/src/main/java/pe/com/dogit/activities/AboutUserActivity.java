package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import java.util.Objects;
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
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AboutUserActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;
    TextView mobilePhoneTextView;
    TextView genderTextView;
    TextView birthDateTextView;
    TextView dniTextView;
    TextView workPlaceTextView;

    User user;

    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = DOgITApp.getInstance().getCurrentUser();

        photoANImageView = findViewById(R.id.photoANImageView);
        displayNameTextView = findViewById(R.id.displayNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mobilePhoneTextView = findViewById(R.id.mobilePhoneTextView);
        genderTextView = findViewById(R.id.genderTextView);
        birthDateTextView = findViewById(R.id.birthDateTextView);
        dniTextView = findViewById(R.id.dniTextView);
        workPlaceTextView = findViewById(R.id.workPlaceTextView);

        userInformation();

    }

    public void userInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        displayNameTextView.setText(user.getName() + " " + user.getLastName());
        emailTextView.setText(user.getEmail());
        mobilePhoneTextView.setText((user.getMobilePhone()));
        if (user.getGender().equals("1")) {
            genderTextView.setText(getResources().getString(R.string.man_gender));
        } else {
            if (user.getGender().equals("2")) {
                genderTextView.setText(getResources().getString(R.string.women_gender));
            } else {
                genderTextView.setText("");
            }
        }
        birthDateTextView.setText(user.getBirthDate());
        dniTextView.setText(String.valueOf(user.getDni()));
        workPlaceTextView.setText(user.getWorkPlace());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (DOgITApp.getInstance().getCurrentUser().getStatus().equals("A")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_to_lock, menu);
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_to_unlock, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lock:
                lockUser("B");
                return true;
            case R.id.action_unlock:
                lockUser("A");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void sendMail(String status) {

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

        String subject;
        String content;
        if (status.equals("B")) {
            subject = getResources().getString(R.string.subject_account_lock);
            content = DOgITApp.getInstance().getCurrentUser().getName() + getResources().getString(R.string.content_account_lock);
        } else {
            subject = getResources().getString(R.string.subject_account_unlock);
            content = DOgITApp.getInstance().getCurrentUser().getName() + getResources().getString(R.string.content_account_unlock);
        }

        if(session != null) {
            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(email));
                message.setSubject(subject);
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(user.getEmail()));
                message.setContent(content, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void lockUser(final String status) {
        AndroidNetworking.put(DOgITService.USERS_EDIT_URL)
                .addBodyParameter("status", status)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().getCurrentUser().setStatus("B");
                        sendMail(status);
                        Toast.makeText(getApplicationContext(), R.string.user_status, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_status_update, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
