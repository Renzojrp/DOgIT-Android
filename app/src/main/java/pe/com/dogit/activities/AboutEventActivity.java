package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Assistence;
import pe.com.dogit.models.Event;
import pe.com.dogit.network.DOgITService;

public class AboutEventActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView dateTextView;
    TextView capacityTextView;

    Boolean assistenceExist = false;

    Event event;

    String TAG = "DOgIT";
    List<Assistence> assistences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        addressTextView = findViewById(R.id.addressTextView);
        dateTextView = findViewById(R.id.dateTextView);
        capacityTextView = findViewById(R.id.capacityTextView);

        event = DOgITApp.getInstance().getCurrentEvent();

        getAssistencesByEvent();

        setEventInformation();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button_event_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_event:
                if(event.getUser().getId().equals(DOgITApp.getInstance().getCurrentUser().getId())) {
                    Intent intent = new Intent(this, AddEventActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_edit_event, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
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
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            assistences = Assistence.build(response.getJSONArray("assistances"));
                            Log.d(TAG, "Found Assistences: " + String.valueOf(assistences.size()));
                            capacityTextView.setText(assistences.size() + "/" + event.getCapacity());
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

    private void getAssistencesByUser() {
        AndroidNetworking
                .get(DOgITService.ASSISTENCE_USER_URL)
                .addPathParameter("user_id", DOgITApp.getInstance().getCurrentUser().getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            assistences = Assistence.build(response.getJSONArray("assistances"));
                            if (assistences.size() != 0) {
                                for (int i = 0; i < assistences.size(); i++){
                                    if (DOgITApp.getInstance().getCurrentUser().getId().equals(assistences.get(i).getUser().getId())
                                            && event.getId().equals(assistences.get(i).getEvent().getId())) {
                                        assistenceExist = true;
                                    }
                                }
                                if (assistenceExist) {
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
                        Log.d(TAG, anError.getMessage());
                    }
                });
    }

    private void saveAssistence() {
        AndroidNetworking
                .post(DOgITService.ASSISTENCE_URL)
                .addBodyParameter("user", DOgITApp.getInstance().getCurrentUser().getId())
                .addBodyParameter("event", event.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
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
