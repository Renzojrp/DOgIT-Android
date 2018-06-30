package pe.com.dogit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONObject;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.Blog;
import pe.com.dogit.network.DOgITService;

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
        descriptionTextView = findViewById(R.id.descriptionBeforeTextView);
        dateTextView = findViewById(R.id.dateTextView);

        blog = DOgITApp.getInstance().getCurrentBlog();

        setPublicationInformation();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        if (blog.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_button_edit, menu);
        } else {
            if (DOgITApp.getInstance().getMyUser().getType().equals("admin")) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_button_delete, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(blog.getUser().getId().equals(DOgITApp.getInstance().getMyUser().getId())) {
                    Intent intent = new Intent(this, AddBlogActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_edit_publication, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                deleteBlog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteBlog() {
        AndroidNetworking.put(DOgITService.BLOG_EDIT_URL)
                .addPathParameter("blog_id", DOgITApp.getInstance().getCurrentBlog().getId())
                .addBodyParameter("status", "N")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DOgITApp.getInstance().setCurrentBlog(null);
                        Toast.makeText(getApplicationContext(), R.string.publication_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_publication_delete, Toast.LENGTH_SHORT).show();
                    }
                });
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
        String date = blog.getDate().substring(0,10);
        String year = date.substring(0,4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        dateTextView.setText(day + "/" + month + "/" + year);
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
