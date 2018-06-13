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

import pe.com.dogit.R;
import pe.com.dogit.activities.ScheduleVisitActivity;
import pe.com.dogit.models.Adoption;

public class AdoptionByMyPublicationsAdapter extends RecyclerView.Adapter<AdoptionByMyPublicationsAdapter.ViewHolder>  {
    private List<Adoption> adoptions;

    public AdoptionByMyPublicationsAdapter(List<Adoption> adoptions) {
        this.adoptions = adoptions;
    }

    public AdoptionByMyPublicationsAdapter() {
    }

    @Override
    public AdoptionByMyPublicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdoptionByMyPublicationsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_adoption, parent, false));
    }

    @Override
    public void onBindViewHolder(AdoptionByMyPublicationsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(adoptions.get(position).getPublication().getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(adoptions.get(position).getPublication().getPet().getPhoto());
        holder.descriptionTextView.setText(adoptions.get(position).getPublication().getDescription());
        holder.adoptionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                ScheduleVisitActivity.class));
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

    public AdoptionByMyPublicationsAdapter setAdoptions(List<Adoption> adoptions) {
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
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            adoptionCardView = (CardView) itemView.findViewById(R.id.adoptionCardView);
        }
    }
}