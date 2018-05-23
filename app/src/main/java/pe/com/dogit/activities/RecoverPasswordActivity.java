package pe.com.dogit.activities;

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

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class RecoverPasswordActivity extends AppCompatActivity {

    TextInputLayout emailTextInputLayout;

    String email;
    String password;
    Session session;

    boolean correctEmail = false;

    String TAG = "DOgIT";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);

        email = "dogitutp@gmail.com";
        password = "posito0310";
    }

    public void sendMailButton(View v) {

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputLayout.getEditText().getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        if(correctEmail) {
            getPasswordByEmail();
        }
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
                message.setContent("Tu contraseña es: " + user.getPassword().toString(), "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPasswordByEmail() {
        AndroidNetworking
                .get(DOgITService.USER_EMAIL_URL)
                .addPathParameter("email",emailTextInputLayout.getEditText().getText().toString())
                .setTag(TAG)
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
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());
                    }
                });
    }
}
