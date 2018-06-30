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
import pe.com.dogit.activities.AddPostadoptionActivity;
import pe.com.dogit.activities.AddPublicationActivity;
import pe.com.dogit.adapters.PostadoptionsAdapter;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.Postadoption;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostAdoptionFragment extends Fragment {

    FloatingActionButton addPostadoptionFloatingActionButton;
    private RecyclerView postadoptionsRecyclerView;
    private PostadoptionsAdapter postadoptionsAdapter;
    private RecyclerView.LayoutManager postadoptionsLayoutManager;
    private List<Postadoption> postadoptions;
    private List<Adoption> adoptions;
    private User user;

    public PostAdoptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_adoption, container, false);
        postadoptionsRecyclerView = view.findViewById(R.id.postAdoptionsRecyclerView);
        postadoptions = new ArrayList<>();
        adoptions = new ArrayList<>();
        postadoptionsAdapter = (new PostadoptionsAdapter()).setPostadoptions(postadoptions);
        postadoptionsLayoutManager = new LinearLayoutManager(view.getContext());
        postadoptionsRecyclerView.setAdapter(postadoptionsAdapter);
        postadoptionsRecyclerView.setLayoutManager(postadoptionsLayoutManager);
        addPostadoptionFloatingActionButton = view.findViewById(R.id.addPostadoptionFloatingActionButton);
        addPostadoptionFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddPostadiotionActivity(v);
            }
        });
        user = DOgITApp.getInstance().getMyUser();
        getPostsadoptions();
        getAdoptions();
        return view;
    }

    private void getPostsadoptions() {
        AndroidNetworking
                .get(DOgITService.POSTADOPTION_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            postadoptions = Postadoption.build(response.getJSONArray("postadoptions"));
                            postadoptionsAdapter.setPostadoptions(postadoptions);
                            postadoptionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void getAdoptions() {
        AndroidNetworking
                .get(DOgITService.ADOPTION_USER_URL)
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    public void goToAddPostadiotionActivity(View v) {
        if(adoptions.size()!= 0) {
            v.getContext()
                    .startActivity(new Intent(v.getContext(),
                            AddPostadoptionActivity.class));
        } else {
            Toast.makeText(v.getContext(), R.string.error_go_add_pet, Toast.LENGTH_SHORT).show();
        }
    }

}
