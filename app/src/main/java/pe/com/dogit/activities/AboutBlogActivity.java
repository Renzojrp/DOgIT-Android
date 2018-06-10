package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.widget.ANImageView;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Blog;
import pe.com.dogit.models.Publication;

public class AboutBlogActivity extends AppCompatActivity {

    ANImageView photoUserANImageView;
    ANImageView photoANImageView;
    TextView nameUserTextView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView dateTextView;

    Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_blog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoUserANImageView = findViewById(R.id.photoUserANImageView);
        photoANImageView = findViewById(R.id.photoANImageView);
        nameUserTextView = findViewById(R.id.nameUserTextView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTextView = findViewById(R.id.dateTextView);

        blog = DOgITApp.getInstance().getCurrentBlog();

        setPublicationInformation();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(blog.getUser().getId().equals(DOgITApp.getInstance().getCurrentUser().getId())) {
                    Intent intent = new Intent(this, AddBlogActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_edit_publication, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void setPublicationInformation() {
        photoUserANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoUserANImageView.setImageUrl(blog.getUser().getPhoto());
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(blog.getPet().getPhoto());
        nameUserTextView.setText(blog.getUser().getName() + " " + blog.getUser().getLastName());
        nameTextView.setText(blog.getPet().getName());
        descriptionTextView.setText(blog.getDescription());
        dateTextView.setText(blog.getDate());
    }

    @Override
    public void onResume() {
        super.onResume();
        setPublicationInformation();
        if(DOgITApp.getInstance().getCurrentBlog() == null) {
            finish();
        }
    }

}
