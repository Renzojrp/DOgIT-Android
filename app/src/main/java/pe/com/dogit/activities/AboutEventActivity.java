package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
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
import pe.com.dogit.models.Assistance;
import pe.com.dogit.models.Event;
import pe.com.dogit.network.DOgITService;

public class AboutEventActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView dateTextView;
    TextView capacityTextView;
    FloatingActionButton assistenceEventFloatingActionButton;

    Boolean assistanceExist = false;

    Event event;

    List<Assistance> assists;

    String email = "dogitutp@gmail.com";
    String password = "posito0310";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionBeforeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        dateTextView = findViewById(R.id.dateTextView);
        capacityTextView = findViewById(R.id.capacityTextView);
        assistenceEventFloatingActionButton = findViewById(R.id.assistenceEventFloatingActionButton);

        event = DOgITApp.getInstance().getCurrentEvent();

        getAssistencesByEvent();

        setEventInformation();

        if (DOgITApp.getInstance().getMyUser().getType().equals("admin")) {
            assistenceEventFloatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (event.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_button_edit, menu);
        } else {
            if (DOgITApp.getInstance().getMyUser().getType().equals("admin")) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_button_delete, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(event.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                    Intent intent = new Intent(this, AddEventActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_edit_event, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                deleteEvent();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
                        for (int i = 0;i<assists.size();i++) {
                            sendMail(assists.get(i));
                        }
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_event_delete, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMail(Assistance assistanceByEvent) {

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
                message.setSubject("DOgIT - Cancelación de Evento");
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(assistanceByEvent.getUser().getEmail()));
                message.setContent(assistanceByEvent.getUser().getName() + ", lamentamos informarle que el evento " +
                        assistanceByEvent.getEvent().getName() + " ha sido cancelado.", "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEventInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(event.getPhoto());
        nameTextView.setText(event.getName());
        descriptionTextView.setText(event.getDescription());
        addressTextView.setText(event.getAddress());
        dateTextView.setText((event.getDate()));
    }

    public void saveAssistenceButton(View view) {
        getAssistencesByUser();
    }

    private void getAssistencesByEvent() {
        AndroidNetworking
                .get(DOgITService.ASSISTENCE_EVENT_URL)
                .addPathParameter("event_id", event.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            assists = Assistance.build(response.getJSONArray("assistances"));
                            capacityTextView.setText(assists.size() + "/" + event.getCapacity());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        //Log.d(TAG, anError.getMessage());
                    }
                });
    }

    private void getAssistencesByUser() {
        AndroidNetworking
                .get(DOgITService.ASSISTENCE_USER_URL)
                .addPathParameter("user_id", DOgITApp.getInstance().getMyUser().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            assists = Assistance.build(response.getJSONArray("assistances"));
                            if (assists.size() != 0) {
                                for (int i = 0; i < assists.size(); i++){
                                    if (DOgITApp.getInstance().getMyUser().getId().equals(assists.get(i).getUser().getId())
                                            && event.getId().equals(assists.get(i).getEvent().getId())) {
                                        assistanceExist = true;
                                    }
                                }
                                if (assistanceExist) {
                                    Toast.makeText(getApplicationContext(), R.string.error_assistence_saved, Toast.LENGTH_SHORT).show();
                                } else {
                                    saveAssistence();
                                }
                            } else {
                                saveAssistence();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        //Log.d(TAG, anError.getMessage());
                    }
                });
    }

    private void saveAssistence() {
        AndroidNetworking
                .post(DOgITService.ASSISTENCE_URL)
                .addBodyParameter("user", DOgITApp.getInstance().getMyUser().getId())
                .addBodyParameter("event", event.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.assistence_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_assistence_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventInformation();
        if(DOgITApp.getInstance().getCurrentEvent() == null) {
            finish();
        }
    }
}
