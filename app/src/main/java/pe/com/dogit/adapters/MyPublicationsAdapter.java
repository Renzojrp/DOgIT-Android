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
import pe.com.dogit.activities.AboutMyPublicationActivity;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Publication;

public class MyPublicationsAdapter extends RecyclerView.Adapter<MyPublicationsAdapter.ViewHolder>  {
    private List<Publication> publications;

    public MyPublicationsAdapter(List<Publication> publications) {
        this.publications = publications;
    }

    public MyPublicationsAdapter() {
    }

    @Override
    public MyPublicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPublicationsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_publication, parent, false));
    }

    @Override
    public void onBindViewHolder(MyPublicationsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(publications.get(position).getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(publications.get(position).getPet().getPhoto());
        holder.descriptionTextView.setText(publications.get(position).getDescription());
        holder.petCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentPublication(publications.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutMyPublicationActivity.class));
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

    public MyPublicationsAdapter setPublications(List<Publication> publications) {
        this.publications = publications;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView petCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            petCardView = (CardView) itemView.findViewById(R.id.petCardView);
        }
    }
}
