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
import pe.com.dogit.activities.AboutPostadoptionActivity;
import pe.com.dogit.models.Postadoption;

public class PostadoptionsAdapter extends RecyclerView.Adapter<PostadoptionsAdapter.ViewHolder>  {
    private List<Postadoption> postadoptions;

    public PostadoptionsAdapter(List<Postadoption> postadoptions) {
        this.postadoptions = postadoptions;
    }

    public PostadoptionsAdapter() {
    }

    @Override
    public PostadoptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostadoptionsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_postadoption, parent, false));
    }

    @Override
    public void onBindViewHolder(PostadoptionsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(postadoptions.get(position).getAdoption().getPublication().getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(postadoptions.get(position).getPhoto());
        holder.descriptionTextView.setText(postadoptions.get(position).getDescription());
        holder.postadoptionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentPostadoption(postadoptions.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutPostadoptionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return postadoptions.size();
    }

    public List<Postadoption> getPostadoptions() {
        return postadoptions;
    }

    public PostadoptionsAdapter setPostadoptions(List<Postadoption> postadoptions) {
        this.postadoptions = postadoptions;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView postadoptionCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionBeforeTextView);
            postadoptionCardView = (CardView) itemView.findViewById(R.id.postadoptionCardView);
        }
    }

}
