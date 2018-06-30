package pe.com.dogit.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.activities.AboutAdoptionActivity;
import pe.com.dogit.models.Adoption;

public class AdoptionsAdapter extends RecyclerView.Adapter<AdoptionsAdapter.ViewHolder>  {
    private List<Adoption> adoptions;

    public AdoptionsAdapter(List<Adoption> adoptions) {
        this.adoptions = adoptions;
    }

    public AdoptionsAdapter() {
    }

    @Override
    public AdoptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdoptionsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_adoption, parent, false));
    }

    @Override
    public void onBindViewHolder(AdoptionsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(adoptions.get(position).getPublication().getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(adoptions.get(position).getPublication().getPet().getPhoto());
        holder.descriptionTextView.setText(adoptions.get(position).getPublication().getDescription());
        holder.adoptionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentAdoption(adoptions.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutAdoptionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adoptions.size();
    }

    public List<Adoption> getAdoptions() {
        return adoptions;
    }

    public AdoptionsAdapter setAdoptions(List<Adoption> adoptions) {
        this.adoptions = adoptions;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView adoptionCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionBeforeTextView);
            adoptionCardView = (CardView) itemView.findViewById(R.id.adoptionCardView);
        }
    }
}
