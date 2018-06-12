package pe.com.dogit.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

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
import pe.com.dogit.models.Request;
import pe.com.dogit.network.DOgITService;

public class AboutRequestActivity extends AppCompatActivity {

    TextView nameTextView;
    ANImageView photoANImageView;
    TextView nameUserTextView;
    ANImageView photoUserANImageView;
    TextView descriptionTextView;

    Request request;

    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        photoUserANImageView = findViewById(R.id.photoUserANImageView);
        nameUserTextView = findViewById(R.id.nameUserTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        request = DOgITApp.getInstance().getCurrentRequest();

        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(request.getPublication().getPet().getPhoto());
        nameTextView.setText(request.getPublication().getPet().getName());
        photoUserANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setImageUrl(request.getUser().getPhoto());
        nameUserTextView.setText(request.getUser().getName());
        descriptionTextView.setText(request.getMessage());
    }

    public void refuseButton(View v) {
        AndroidNetworking.put(DOgITService.REQUEST_EDIT_URL)
                .addPathParameter("request_id", DOgITApp.getInstance().getCurrentRequest().getId())
                .addBodyParameter("status", "R")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.request_refuse, Toast.LENGTH_SHORT).show();
                        sendMail("R");
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_request_refuse, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void acceptButton(View v) {
        AndroidNetworking.put(DOgITService.REQUEST_EDIT_URL)
                .addPathParameter("request_id", DOgITApp.getInstance().getCurrentRequest().getId())
                .addBodyParameter("status", "A")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.request_accept, Toast.LENGTH_SHORT).show();
                        sendMail("A");
                        changePublicationStatus();
                        createAdoption();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_request_accept, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMail(String answer) {

        String status;
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
            if (answer.equals("A")) {
                status = getResources().getString(R.string.approved);
            } else {
                status = getResources().getString(R.string.rejected);
            }
            try {
                message.setFrom(new InternetAddress(email));
                message.setSubject(getResources().getString(R.string.response_mail_subject));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(request.getUser().getEmail()));
                message.setContent(request.getUser().getName() + " " + getResources().getString(R.string.response_mail) + " " + request.getPublication().getPet().getName() +
                        " " + getResources().getString(R.string.response_mail_body) + " " + status, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAdoption() {
        AndroidNetworking.post(DOgITService.ADOPTION_URL)
                .addBodyParameter("user", request.getUser().getId())
                .addBodyParameter("publication", request.getPublication().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                    @Override
                    public void onError(ANError error) {

                    }
                });
    }

    public void changePublicationStatus() {
        AndroidNetworking.put(DOgITService.PUBLICATION_EDIT_URL)
                .addPathParameter("publication_id", DOgITApp.getInstance().getCurrentRequest().getPublication().getId())
                .addBodyParameter("status", "N")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                    @Override
                    public void onError(ANError error) {

                    }
                });
    }

}
