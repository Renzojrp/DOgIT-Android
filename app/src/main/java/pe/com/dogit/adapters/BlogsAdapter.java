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
import pe.com.dogit.activities.AboutBlogActivity;
import pe.com.dogit.models.Blog;

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.ViewHolder>  {
    private List<Blog> blogs;

    public BlogsAdapter(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public BlogsAdapter() {
    }

    @Override
    public BlogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlogsAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(BlogsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(blogs.get(position).getPet().getName());
        holder.photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoANImageView.setImageUrl(blogs.get(position).getPet().getPhoto());
        holder.descriptionTextView.setText(blogs.get(position).getDescription());
        holder.blogCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOgITApp.getInstance().setCurrentBlog(blogs.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), AboutBlogActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public BlogsAdapter setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ANImageView photoANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        CardView blogCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            photoANImageView = (ANImageView) itemView.findViewById(R.id.photoANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionBeforeTextView);
            blogCardView = (CardView) itemView.findViewById(R.id.blogCardView);
        }
    }
}
