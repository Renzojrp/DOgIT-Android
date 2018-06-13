package pe.com.dogit.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.adapters.AdoptionByMyPublicationsAdapter;
import pe.com.dogit.adapters.AdoptionsAdapter;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class ListAdoptionActivity extends AppCompatActivity {

    private RecyclerView adoptionsRecyclerView;
    private AdoptionByMyPublicationsAdapter adoptionByMyPublicationsAdapter;
    private RecyclerView.LayoutManager adoptionsLayoutManager;
    private List<Adoption> adoptions;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_adoption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adoptionsRecyclerView = findViewById(R.id.adoptionsRecyclerView);
        adoptions = new ArrayList<>();
        adoptionByMyPublicationsAdapter = (new AdoptionByMyPublicationsAdapter()).setAdoptions(adoptions);
        adoptionsLayoutManager = new LinearLayoutManager(this);
        adoptionsRecyclerView.setAdapter(adoptionByMyPublicationsAdapter);
        adoptionsRecyclerView.setLayoutManager(adoptionsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        getAdoptions();
    }

    private void getAdoptions() {
        AndroidNetworking
                .get(DOgITService.ADOPTION_URL)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            adoptions = Adoption.build(response.getJSONArray("adoptions"));
                            List<Adoption> adoptionsByMyPublications = new ArrayList<>();
                            for(int i = 0; i<adoptions.size(); i++) {
                                if(adoptions.get(i).getPublication().getUser().getId().equals(DOgITApp.getInstance().getCurrentUser().getId())){
                                    adoptionsByMyPublications.add(adoptions.get(i));
                                }
                            }
                            adoptionByMyPublicationsAdapter.setAdoptions(adoptionsByMyPublications);
                            adoptionByMyPublicationsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

}
