package pe.com.dogit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.com.dogit.R;
import pe.com.dogit.activities.AddMyPublicationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPublicationFragment extends Fragment {
    FloatingActionButton addPublicationFloatingActionButton;

    public MyPublicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_publication, container, false);

        addPublicationFloatingActionButton = view.findViewById(R.id.addPublicationFloatingActionButton);
        addPublicationFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AddMyPublicationActivity.class));
            }
        });

        return view;
    }

}
