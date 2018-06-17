package pe.com.dogit.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class RecoverPasswordActivity extends AppCompatActivity {

    TextInputLayout emailTextInputLayout;
    ProgressBar sendMailProgressBar;
    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    boolean correctEmail = false;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        sendMailProgressBar = findViewById(R.id.sendMailProgressBar);

        sendMailProgressBar.setVisibility(View.GONE);
    }

    public void sendMailButton(View v) {
        sendMailProgressBar.setVisibility(View.VISIBLE);

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputLayout.getEditText().getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        if(correctEmail) {
            getPasswordByEmail();
        } else {
            sendMailProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getPasswordByEmail() {
        AndroidNetworking
                .get(DOgITService.USER_EMAIL_URL)
                .addPathParameter("email",emailTextInputLayout.getEditText().getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            user = User.build(response.getJSONObject("user"));
                            sendMail();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),  R.string.mail_not_exist, Toast.LENGTH_SHORT).show();
                            sendMailProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),  R.string.mail_not_exist, Toast.LENGTH_SHORT).show();
                        sendMailProgressBar.setVisibility(View.INVISIBLE);
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
                message.setSubject("DOgIT - Recuperar contraseña");
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(emailTextInputLayout.getEditText().getText().toString()));
                message.setContent("Tu contraseña es: " + user.getPassword(), "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(),  R.string.mail_send, Toast.LENGTH_SHORT).show();
        finish();
    }

}
