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
import pe.com.dogit.activities.ListAdoptionActivity;
import pe.com.dogit.adapters.VisitsAdapter;
import pe.com.dogit.models.User;
import pe.com.dogit.models.Visit;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitFragment extends Fragment {

    FloatingActionButton addVisitFloatingActionButton;
    private RecyclerView visitsRecyclerView;
    private VisitsAdapter visitsAdapter;
    private RecyclerView.LayoutManager visitsLayoutManager;
    private List<Visit> visits;
    private User user;

    public VisitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit, container, false);
        visitsRecyclerView = view.findViewById(R.id.visitsRecyclerView);
        visits = new ArrayList<>();
        visitsAdapter = (new VisitsAdapter()).setVisits(visits);
        visitsLayoutManager = new LinearLayoutManager(view.getContext());
        visitsRecyclerView.setAdapter(visitsAdapter);
        visitsRecyclerView.setLayoutManager(visitsLayoutManager);
        user = DOgITApp.getInstance().getMyUser();
        addVisitFloatingActionButton = view.findViewById(R.id.addVisitFloatingActionButton);
        addVisitFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                ListAdoptionActivity.class));
            }
        });
        getVisits();
        return view;
    }

    private void getVisits() {
        AndroidNetworking
                .get(DOgITService.VISIT_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            visits = Visit.build(response.getJSONArray("visits"));
                            List<Visit> myVisits = new ArrayList<>();
                            for (int i=0;i<visits.size();i++) {
                                if(visits.get(i).getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                                    myVisits.add(visits.get(i));
                                } else {
                                    if (visits.get(i).getPublication().getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                                        myVisits.add(visits.get(i));
                                    }
                                }
                            }
                            visitsAdapter.setVisits(myVisits);
                            visitsAdapter.notifyDataSetChanged();
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
        getVisits();
    }

}
