package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class AddBlogActivity extends AppCompatActivity {

    private ANImageView photoANImageView;
    Button galeryButton;
    private TextInputLayout descriptionTextInputLayout;
    private Spinner petSpinner;

    Boolean correctDescription = false;

    List<String> namePet = new ArrayList<>();
    List<String> idPet = new ArrayList<>();
    String idPetSelected;
    Integer position;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        petSpinner = findViewById(R.id.petSpinner);

        user = DOgITApp.getInstance().getMyUser();

        for(int i = 0; i<DOgITApp.getInstance().getCurrentPets().size(); i++) {
            idPet.add(DOgITApp.getInstance().getCurrentPets().get(i).getId());
            namePet.add(DOgITApp.getInstance().getCurrentPets().get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namePet);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(dataAdapter);

        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                idPetSelected = idPet.get(pos);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPets().get(pos).getPhoto());
                position = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idPetSelected = idPet.get(0);
                photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                photoANImageView.setImageUrl(DOgITApp.getInstance().getCurrentPets().get(0).getPhoto());
                position = 0;
            }
        });

        layoutByOrigin();
    }

    public void layoutByOrigin() {
        if (DOgITApp.getInstance().getCurrentBlog() != null) {
            descriptionTextInputLayout.getEditText().setText(DOgITApp.getInstance().getCurrentBlog().getDescription());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if(DOgITApp.getInstance().getCurrentBlog() == null) {
                    finish();
                } else {
                    deleteBlog();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void shareClick(View view) {

        if(descriptionTextInputLayout.getEditText().getText().toString().length() == 0) {
            descriptionTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDescription = false;
        } else {
            descriptionTextInputLayout.setError(null);
            correctDescription = true;
        }

        if(correctDescription) {
            if (DOgITApp.getInstance().getCurrentBlog() == null) {
                saveBlog();
            } else {
                editBlog();
            }
        }
    }

    public void cancelClick(View v) {
        finish();
    }

    private void saveBlog() {
        AndroidNetworking.post(DOgITService.BLOG_URL)
                .addBodyParameter("user", user.getId())
                .addBodyParameter("pet", idPetSelected)
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.blog_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_blog_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editBlog() {
        AndroidNetworking.put(DOgITService.BLOG_EDIT_URL)
                .addPathParameter("blog_id", DOgITApp.getInstance().getCurrentBlog().getId())
                .addBodyParameter("pet", idPetSelected)
                .addBodyParameter("description", descriptionTextInputLayout.getEditText().getText().toString())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().getCurrentBlog().setPet(DOgITApp.getInstance().getCurrentPets().get(position));
                        DOgITApp.getInstance().getCurrentBlog().setDescription(descriptionTextInputLayout.getEditText().getText().toString());

                        Toast.makeText(getApplicationContext(), R.string.publication_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteBlog() {
        AndroidNetworking.put(DOgITService.BLOG_EDIT_URL)
                .addPathParameter("blog_id", DOgITApp.getInstance().getCurrentBlog().getId())
                .addBodyParameter("status", "N")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().setCurrentBlog(null);
                        Toast.makeText(getApplicationContext(), R.string.publication_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_delete, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
