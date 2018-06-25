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
import pe.com.dogit.adapters.UsersAdapter;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager usersLayoutManager;
    private List<User> users;
    private User user;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        users = new ArrayList<>();
        usersAdapter = (new UsersAdapter()).setUsers(users);
        usersLayoutManager = new LinearLayoutManager(view.getContext());
        usersRecyclerView.setAdapter(usersAdapter);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        user = DOgITApp.getInstance().getMyUser();

        getUsers();
        return view;
    }

    private void getUsers() {
        AndroidNetworking
                .get(DOgITService.USER_TYPE_URL)
                .addPathParameter("type", "user")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            users = User.build(response.getJSONArray("users"));
                            usersAdapter.setUsers(users);
                            usersAdapter.notifyDataSetChanged();
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
        getUsers();
    }

}
