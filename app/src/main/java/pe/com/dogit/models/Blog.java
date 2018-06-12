package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Blog {
    private String id;
    private User user;
    private Pet pet;
    private String description;
    private String date;

    public Blog() {
    }

    public Blog(String id, User user, Pet pet, String description, String date) {
        this.setId(id);
        this.setUser(user);
        this.setPet(pet);
        this.setDescription(description);
        this.setDate(date);
    }

    public String getId() {
        return id;
    }

    public Blog setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Blog setUser(User user) {
        this.user = user;
        return this;
    }

    public Pet getPet() {
        return pet;
    }

    public Blog setPet(Pet pet) {
        this.pet = pet;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Blog setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Blog setDate(String date) {
        this.date = date;
        return this;
    }

    public static Blog build(JSONObject jsonBlog) {
        if(jsonBlog == null) return null;
        Blog blog= new Blog();
        try {
            blog.setId(jsonBlog.getString("_id"));
            blog.setUser(User.build(jsonBlog.getJSONObject("user")));
            blog.setPet(Pet.build(jsonBlog.getJSONObject("pet")));
            blog.setDescription(jsonBlog.getString("description"));
            blog.setDate(jsonBlog.getString("date"));
            return blog;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Blog> build(JSONArray jsonBlogs) {
        if(jsonBlogs == null) return null;
        int length = jsonBlogs.length();
        List<Blog> blogs = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                blogs.add(Blog.build(jsonBlogs.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return blogs;
    }
}
