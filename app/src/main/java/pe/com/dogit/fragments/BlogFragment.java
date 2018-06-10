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
import pe.com.dogit.activities.AddBlogActivity;
import pe.com.dogit.activities.AddPublicationActivity;
import pe.com.dogit.adapters.BlogsAdapter;
import pe.com.dogit.models.Blog;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogFragment extends Fragment {

    FloatingActionButton addBlogFloatingActionButton;
    private RecyclerView blogsRecyclerView;
    private BlogsAdapter blogsAdapter;
    private RecyclerView.LayoutManager blogsLayoutManager;
    private List<Blog> blogs;
    private List<Pet> pets;
    private User user;

    public BlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publication, container, false);
        blogsRecyclerView = view.findViewById(R.id.publicationsRecyclerView);
        blogs = new ArrayList<>();
        blogsAdapter = (new BlogsAdapter()).setBlogs(blogs);
        blogsLayoutManager = new LinearLayoutManager(view.getContext());
        blogsRecyclerView.setAdapter(blogsAdapter);
        blogsRecyclerView.setLayoutManager(blogsLayoutManager);
        user = DOgITApp.getInstance().getCurrentUser();
        pets = DOgITApp.getInstance().getCurrentPets();
        addBlogFloatingActionButton = view.findViewById(R.id.addPublicationFloatingActionButton);
        addBlogFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddBlogActivity(v);
            }
        });
        getMyBlogs();
        return view;
    }

    public void goToAddBlogActivity(View v) {
        if(pets.size()!= 0) {
            v.getContext()
                    .startActivity(new Intent(v.getContext(),
                            AddBlogActivity.class));
        } else {
            Toast.makeText(v.getContext(), R.string.error_go_add_pet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyBlogs() {
        AndroidNetworking
                .get(DOgITService.BLOG_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            blogs = Blog.build(response.getJSONArray("blogs"));
                            blogsAdapter.setBlogs(blogs);
                            blogsAdapter.notifyDataSetChanged();
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
        getMyBlogs();
    }

}
