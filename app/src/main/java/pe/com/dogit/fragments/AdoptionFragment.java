package pe.com.dogit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import pe.com.dogit.adapters.AdoptionsAdapter;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoptionFragment extends Fragment {

    private RecyclerView adoptionsRecyclerView;
    private AdoptionsAdapter adoptionsAdapter;
    private RecyclerView.LayoutManager adoptionsLayoutManager;
    private List<Adoption> adoptions;
    private List<Pet> pets;
    private User user;


    public AdoptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publication, container, false);
        adoptionsRecyclerView = view.findViewById(R.id.publicationsRecyclerView);
        adoptions = new ArrayList<>();
        adoptionsAdapter = (new AdoptionsAdapter()).setAdoptions(adoptions);
        adoptionsLayoutManager = new LinearLayoutManager(view.getContext());
        adoptionsRecyclerView.setAdapter(adoptionsAdapter);
        adoptionsRecyclerView.setLayoutManager(adoptionsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        pets = DOgITApp.getInstance().getCurrentPets();
        getAdoptions();
        return view;
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
                            adoptionsAdapter.setAdoptions(adoptions);
                            adoptionsAdapter.notifyDataSetChanged();
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
