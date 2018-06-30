package pe.com.dogit.adapters;

import android.annotation.SuppressLint;
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
import pe.com.dogit.activities.AboutEventActivity;
import pe.com.dogit.models.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> events;

    public EventsAdapter() {
    }

    public EventsAdapter(List<Event> events) {
        this.events = events;
    }
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_event, parent, false));
    }

    @Override
    public void onBindViewHolder(
            EventsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoPetANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setImageUrl(events.get(position).getPhoto());
        holder.nameTextView.setText(events.get(position).getName());
        holder.descriptionTextView.setText(String.valueOf(events.get(position).getDescription()));
        holder.dateTextView.setText(events.get(position).getDate());
        holder.eventCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentEvent(events.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutEventActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public List<Event> getEvents() {
        return events;
    }

    public EventsAdapter setEvents(List<Event> events) {
        this.events = events;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoPetANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView dateTextView;
        CardView eventCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoPetANImageView = itemView.findViewById(R.id.photoPetANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionBeforeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            eventCardView = itemView.findViewById(R.id.eventCardView);
        }
    }
}
