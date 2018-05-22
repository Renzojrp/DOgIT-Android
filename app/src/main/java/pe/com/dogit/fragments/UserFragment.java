package pe.com.dogit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.activities.AddPetActivity;
import pe.com.dogit.activities.EditUserProfileActivity;
import pe.com.dogit.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private FloatingActionButton editUserFloatingActionButton;
    private ANImageView photoANImageView;
    private TextView displayNameTextView;
    private TextView emailTextView;
    private TextView mobilePhoneTextView;
    private TextView genderTextView;
    private TextView birthDateTextView;

    User user;
    String TAG = "DOgIT";

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        user = DOgITApp.getInstance().getCurrentUser();

        photoANImageView = view.findViewById(R.id.photoANImageView);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        mobilePhoneTextView = view.findViewById(R.id.mobilePhoneTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        birthDateTextView = view.findViewById(R.id.birthDateTextView);
        editUserFloatingActionButton = view.findViewById(R.id.editUserFloatingActionButton);
        editUserFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                EditUserProfileActivity.class));
            }
        });

        userInformation();

        return view;
    }

    public void userInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(user.getPhoto());
        displayNameTextView.setText(user.getName() + " " + user.getLastName());
        emailTextView.setText(user.getEmail());
        mobilePhoneTextView.setText((user.getMobilePhone().toString()));
        if (user.getGender().equals("1")) {
            genderTextView.setText(getResources().getString(R.string.male_gender));
        } else {
            if (user.getGender().equals("2")) {
                genderTextView.setText(getResources().getString(R.string.female_gender));
            } else {
                genderTextView.setText("");
            }
        }
        birthDateTextView.setText(user.getBirthDate());
    }

    @Override
    public void onResume() {
        super.onResume();
        userInformation();
    }

}
