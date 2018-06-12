package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Adoption {
    private String id;
    private User user;
    private Publication publication;
    private String date;

    public Adoption() {
    }

    public Adoption(String id, User user, Publication publication, String date) {
        this.setId(id);
        this.setUser(user);
        this.setPublication(publication);
        this.setDate(date);
    }

    public String getId() {
        return id;
    }

    public Adoption setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Adoption setUser(User user) {
        this.user = user;
        return this;
    }

    public Publication getPublication() {
        return publication;
    }

    public Adoption setPublication(Publication publication) {
        this.publication = publication;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Adoption setDate(String date) {
        this.date = date;
        return this;
    }

    public static Adoption build(JSONObject jsonAdoption) {
        if(jsonAdoption == null) return null;
        Adoption adoption= new Adoption();
        try {
            adoption.setId(jsonAdoption.getString("_id"));
            adoption.setUser(User.build(jsonAdoption.getJSONObject("user")));
            adoption.setPublication(Publication.build(jsonAdoption.getJSONObject("publication")));
            adoption.setDate(jsonAdoption.getString("date"));
            return adoption;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Adoption> build(JSONArray jsonAdoptions) {
        if(jsonAdoptions == null) return null;
        int length = jsonAdoptions.length();
        List<Adoption> adoptions = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                adoptions.add(Adoption.build(jsonAdoptions.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return adoptions;
    }
}
