package pe.com.dogit.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.com.dogit.R;
import pe.com.dogit.models.Visit;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.ViewHolder> {
    private List<Visit> visits;

    public VisitsAdapter(List<Visit> visits) {
        this.visits = visits;
    }

    public VisitsAdapter() {
    }

    @Override
    public VisitsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VisitsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(VisitsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(visits.get(position).getPublication().getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(visits.get(position).getPublication().getPet().getPhoto());
        holder.placeTextView.setText(visits.get(position).getPlace());
        holder.dateTextView.setText(visits.get(position).getDate());
        holder.visitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public VisitsAdapter setVisits(List<Visit> visits) {
        this.visits = visits;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView placeTextView;
        TextView dateTextView;
        CardView visitCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            placeTextView = (TextView) itemView.findViewById(R.id.placeTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            visitCardView = (CardView) itemView.findViewById(R.id.visitCardView);
        }
    }
}
