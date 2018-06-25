package pe.com.dogit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import pe.com.dogit.activities.AddPublicationActivity;
import pe.com.dogit.adapters.PublicationsAdapter;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicationsFragment extends Fragment {

    private RecyclerView publicationsRecyclerView;
    private PublicationsAdapter publicationsAdapter;
    private RecyclerView.LayoutManager publicationsLayoutManager;
    private List<Publication> publications;
    private List<Pet> pets;
    private User user;

    public PublicationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publications, container, false);
        publicationsRecyclerView = view.findViewById(R.id.publicationsRecyclerView);
        publications = new ArrayList<>();
        publicationsAdapter = (new PublicationsAdapter()).setPublications(publications);
        publicationsLayoutManager = new LinearLayoutManager(view.getContext());
        publicationsRecyclerView.setAdapter(publicationsAdapter);
        publicationsRecyclerView.setLayoutManager(publicationsLayoutManager);
        user = DOgITApp.getInstance().getMyUser();
        pets = DOgITApp.getInstance().getCurrentPets();
        getPublications();
        return view;
    }

    private void getPublications() {
        AndroidNetworking
                .get(DOgITService.PUBLICATION_STATUS_URL)
                .addPathParameter("status", "A")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            publications = Publication.build(response.getJSONArray("publications"));
                            publicationsAdapter.setPublications(publications);
                            publicationsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPublications();
    }

}
