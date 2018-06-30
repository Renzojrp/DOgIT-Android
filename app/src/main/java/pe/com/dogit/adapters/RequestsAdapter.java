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
import pe.com.dogit.activities.AboutRequestActivity;
import pe.com.dogit.models.Request;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder>  {
    private List<Request> requests;

    public RequestsAdapter(List<Request> requests) {
        this.requests = requests;
    }

    public RequestsAdapter() {
    }

    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequestsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_request, parent, false));
    }

    @Override
    public void onBindViewHolder(RequestsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(requests.get(position).getPublication().getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(requests.get(position).getPublication().getPet().getPhoto());
        holder.descriptionTextView.setText(requests.get(position).getPublication().getDescription());
        holder.requestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentRequest(requests.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutRequestActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public RequestsAdapter setRequests(List<Request> requests) {
        this.requests = requests;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView requestCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionBeforeTextView);
            requestCardView = (CardView) itemView.findViewById(R.id.requestCardView);
        }
    }
}
