package pe.com.dogit.fragments;


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
import pe.com.dogit.adapters.PublicationsAdapter;
import pe.com.dogit.adapters.RequestsAdapter;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Request;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private RecyclerView requestsRecyclerView;
    private RequestsAdapter requestsAdapter;
    private RecyclerView.LayoutManager requestsLayoutManager;
    private List<Request> requests;
    private List<Pet> pets;
    private User user;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        requestsRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        requests = new ArrayList<>();
        requestsAdapter = (new RequestsAdapter()).setRequests(requests);
        requestsLayoutManager = new LinearLayoutManager(view.getContext());
        requestsRecyclerView.setAdapter(requestsAdapter);
        requestsRecyclerView.setLayoutManager(requestsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        pets = DOgITApp.getInstance().getCurrentPets();
        getMyRequets();
        return view;
    }

    private void getMyRequets() {
        AndroidNetworking
                .get(DOgITService.REQUEST_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            requests = Request.build(response.getJSONArray("requests"));
                            List<Request> myRequests = new ArrayList<>();
                            for(int i = 0; i<requests.size(); i++) {
                                if(requests.get(i).getPublication().getUser().getId().equals(DOgITApp.getInstance().getCurrentUser().getId())){
                                    myRequests.add(requests.get(i));
                                }
                            }
                            requestsAdapter.setRequests(myRequests);
                            requestsAdapter.notifyDataSetChanged();
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
        getMyRequets();
    }

}