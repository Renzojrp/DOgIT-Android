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
import pe.com.dogit.activities.AddPetActivity;
import pe.com.dogit.adapters.PetsAdapter;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetFragment extends Fragment {

    private FloatingActionButton addPetFloatingActionButton;
    private RecyclerView petsRecyclerView;
    private PetsAdapter petsAdapter;
    private RecyclerView.LayoutManager petsLayoutManager;
    private List<Pet> pets;
    private User user;

    public PetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        addPetFloatingActionButton = view.findViewById(R.id.addPetFloatingActionButton);
        addPetFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AddPetActivity.class));
            }
        });
        petsRecyclerView = view.findViewById(R.id.petsRecyclerView);
        pets = new ArrayList<>();
        petsAdapter = (new PetsAdapter()).setPets(pets);
        petsLayoutManager = new LinearLayoutManager(view.getContext());
        petsRecyclerView.setAdapter(petsAdapter);
        petsRecyclerView.setLayoutManager(petsLayoutManager);
        user = DOgITApp.getInstance().getMyUser();
        getPets();
        return view;
    }

    private void getPets() {
        AndroidNetworking
                .get(DOgITService.PET_USER_URL)
                .addPathParameter("user_id", user.getId())
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            pets = Pet.build(response.getJSONArray("pets"));
                            petsAdapter.setPets(pets);
                            petsAdapter.notifyDataSetChanged();
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
        getPets();
    }

}
