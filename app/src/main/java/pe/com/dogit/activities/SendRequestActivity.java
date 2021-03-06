package pe.com.dogit.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
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
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.Request;
import pe.com.dogit.network.DOgITService;

public class SendRequestActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView questionTextView;
    TextInputLayout answerTextInputLayout;

    Boolean correctAnswer;

    Publication publication;

    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionBeforeTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextInputLayout = findViewById(R.id.answerTextInputLayout);

        publication = DOgITApp.getInstance().getCurrentPublication();

        setPublicationInformation();
    }

    private void setPublicationInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(publication.getPet().getPhoto());
        nameTextView.setText(publication.getPet().getName());
        descriptionTextView.setText(publication.getDescription());
        questionTextView.setText(getResources().getString(R.string.question_request) + " " + publication.getPet().getName()
                + getResources().getString(R.string.question_mark));
    }

    public void sendRequest(View v) {
        if(answerTextInputLayout.getEditText().getText().toString().length() == 0) {
            answerTextInputLayout.setError(getResources().getString(R.string.empty_answer));
            correctAnswer = false;
        } else {
            answerTextInputLayout.setError(null);
            correctAnswer = true;
        }

        if(correctAnswer) {
            if(publication.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                Toast.makeText(getApplicationContext(), R.string.error_request_publication, Toast.LENGTH_SHORT).show();
            } else {
                getRequestByUser();
            }
        }
    }

    private void getRequestByUser() {
        AndroidNetworking
                .get(DOgITService.REQUEST_USER_URL)
                .addPathParameter("user_id", DOgITApp.getInstance().getMyUser().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            List<Request> requestsToReject = Request.build(response.getJSONArray("requests"));
                            if(requestsToReject.size() == 0) {
                                sendResquest();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.request_send_already, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void sendResquest() {
        AndroidNetworking.post(DOgITService.REQUEST_URL)
                .addBodyParameter("user", DOgITApp.getInstance().getMyUser().getId())
                .addBodyParameter("publication", publication.getId())
                .addBodyParameter("message", answerTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sendMail();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_request_send, Toast.LENGTH_SHORT).show();
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
                message.setSubject(getResources().getString(R.string.mail_send_request));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(publication.getUser().getEmail()));
                message.setContent(publication.getUser().getName() + " " + getResources().getString(R.string.mail_send_request_request)
                        + " " + publication.getPet().getName(), "text/html; charset=utf-8");
                Transport.send(message);
                Toast.makeText(getApplicationContext(), R.string.request_send, Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
