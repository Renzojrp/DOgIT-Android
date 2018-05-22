package pe.com.dogit.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.activities.AddPetActivity;
import pe.com.dogit.models.Pet;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.ViewHolder> {
    private List<Pet> pets;

    public PetsAdapter() {
    }

    public PetsAdapter(List<Pet> pets) {
        this.pets = pets;
    }
    @Override
    public PetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_pet, parent, false));
    }

    @Override
    public void onBindViewHolder(
            PetsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoPetANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setImageUrl(pets.get(position).getPhoto());
        holder.nameTextView.setText(pets.get(position).getName());
        holder.descriptionTextView.setText(String.valueOf(pets.get(position).getDescription()));
        if (pets.get(position).getGender().equals("0")) {
            holder.genderTextView.setText(R.string.male_gender);
        } else {
            holder.genderTextView.setText(R.string.female_gender);
        }
        holder.petConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentPet(pets.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AddPetActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return pets.size();
    }

    public List<Pet> getPets() {
        return pets;
    }

    public PetsAdapter setPets(List<Pet> pets) {
        this.pets = pets;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoPetANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView genderTextView;
        ConstraintLayout petConstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            photoPetANImageView = itemView.findViewById(R.id.photoPetANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            petConstraintLayout = itemView.findViewById(R.id.petConstraintLayout);
        }
    }
}