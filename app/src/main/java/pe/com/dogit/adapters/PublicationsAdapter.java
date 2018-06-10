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
import pe.com.dogit.activities.AboutPublicationActivity;
import pe.com.dogit.models.Publication;

public class PublicationsAdapter extends RecyclerView.Adapter<PublicationsAdapter.ViewHolder>  {
    private List<Publication> publications;

    public PublicationsAdapter(List<Publication> publications) {
        this.publications = publications;
    }

    public PublicationsAdapter() {
    }

    @Override
    public PublicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PublicationsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_publication, parent, false));
    }

    @Override
    public void onBindViewHolder(PublicationsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(publications.get(position).getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(publications.get(position).getPet().getPhoto());
        holder.descriptionTextView.setText(publications.get(position).getDescription());
        holder.publicationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentPublication(publications.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutPublicationActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public PublicationsAdapter setPublications(List<Publication> publications) {
        this.publications = publications;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView publicationCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            publicationCardView = (CardView) itemView.findViewById(R.id.publicationCardView);
        }
    }
}
