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
import pe.com.dogit.activities.AddEventActivity;
import pe.com.dogit.activities.LoginActivity;
import pe.com.dogit.adapters.EventsAdapter;
import pe.com.dogit.models.Event;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private FloatingActionButton addEventFloatingActionButton;
    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private List<Event> events;
    private static String TAG = "DOgIT";
    private User user;


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        addEventFloatingActionButton = view.findViewById(R.id.addEventFloatingActionButton);
        addEventFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(), AddEventActivity.class));
            }
        });
        events = new ArrayList<>();
        eventsAdapter = (new EventsAdapter()).setEvents(events);
        eventsLayoutManager = new LinearLayoutManager(view.getContext());
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        getEvents();
        return view;
    }

    private void getEvents() {
        AndroidNetworking
                .get(DOgITService.EVENT_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            events = Event.build(response.getJSONArray("events"));
                            Log.d(TAG, "Found Events: " + String.valueOf(events.size()));
                            eventsAdapter.setEvents(events);
                            eventsAdapter.notifyDataSetChanged();
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
        getEvents();
    }

}
