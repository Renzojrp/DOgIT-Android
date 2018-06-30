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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.activities.AddEventActivity;
import pe.com.dogit.adapters.EventsAdapter;
import pe.com.dogit.models.Event;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private List<Event> events;
    private User user;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);

        events = new ArrayList<>();
        eventsAdapter = (new EventsAdapter()).setEvents(events);
        eventsLayoutManager = new LinearLayoutManager(view.getContext());
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        user = DOgITApp.getInstance().getMyUser();
        getEvents();
        return view;
    }

    private void getEvents() {
        AndroidNetworking
                .get(DOgITService.EVENT_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            events = Event.build(response.getJSONArray("events"));

                            List<Event> availableEvents = new ArrayList<>();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = null;
                            for(int i = 0; i < events.size(); i++) {
                                try {
                                    date = sdf.parse(events.get(i).getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Calendar c = Calendar.getInstance();
                                if(c.getTime().compareTo(date) <= 0) {
                                    availableEvents.add(events.get(i));
                                }
                            }
                            eventsAdapter.setEvents(availableEvents);
                            eventsAdapter.notifyDataSetChanged();
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
        getEvents();
    }

}
