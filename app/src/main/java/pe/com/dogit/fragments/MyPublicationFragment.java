package pe.com.dogit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import pe.com.dogit.activities.AddMyPublicationActivity;
import pe.com.dogit.adapters.MyPublicationsAdapter;
import pe.com.dogit.adapters.PetsAdapter;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPublicationFragment extends Fragment {
    FloatingActionButton addPublicationFloatingActionButton;
    private RecyclerView publicationsRecyclerView;
    private MyPublicationsAdapter publicationsAdapter;
    private RecyclerView.LayoutManager publicationsLayoutManager;
    private List<Publication> publications;
    private static String TAG = "DOgIT";
    private User user;
    public MyPublicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_publication, container, false);
        publicationsRecyclerView = view.findViewById(R.id.publicationsRecyclerView);
        publications = new ArrayList<>();
        publicationsAdapter = (new MyPublicationsAdapter()).setPublications(publications);
        publicationsLayoutManager = new LinearLayoutManager(view.getContext());
        publicationsRecyclerView.setAdapter(publicationsAdapter);
        publicationsRecyclerView.setLayoutManager(publicationsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        addPublicationFloatingActionButton = view.findViewById(R.id.addPublicationFloatingActionButton);
        addPublicationFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AddMyPublicationActivity.class));
            }
        });
        getMyPublications();
        return view;
    }

    private void getMyPublications() {
        AndroidNetworking
                .get(DOgITService.PUBLICATION_USER_URL)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            publications = Publication.build(response.getJSONArray("publications"));
                            Log.d(TAG, "Found publications: " + String.valueOf(publications.size()));
                            publicationsAdapter.setPublications(publications);
                            publicationsAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        getMyPublications();
    }
}
