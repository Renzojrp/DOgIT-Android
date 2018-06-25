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
import pe.com.dogit.activities.AboutUserActivity;
import pe.com.dogit.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {
    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    public UsersAdapter() {
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsersAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(users.get(position).getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(users.get(position).getPhoto());
        holder.mailTextView.setText(users.get(position).getEmail());
        holder.phoneTextView.setText(users.get(position).getMobilePhone());
        holder.userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentUser(users.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutUserActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public List<User> getUsers() {
        return users;
    }

    public UsersAdapter setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView mailTextView;
        TextView phoneTextView;
        CardView userCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            mailTextView = (TextView) itemView.findViewById(R.id.mailTextView);
            phoneTextView = (TextView) itemView.findViewById(R.id.phoneTextView);
            userCardView = (CardView) itemView.findViewById(R.id.userCardView);
        }
    }
}
