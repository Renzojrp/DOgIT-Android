package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Visit {
    private String id;
    private User user;
    private Publication publication;
    private String date;
    private String place;
    private String message;


    public Visit() {
    }

    public Visit(String id, User user, Publication publication, String date, String place, String message) {
        this.setId(id);
        this.setUser(user);
        this.setPublication(publication);
        this.setDate(date);
        this.setPlace(place);
        this.setMessage(message);
    }

    public String getId() {
        return id;
    }

    public Visit setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Visit setUser(User user) {
        this.user = user;
        return this;
    }

    public Publication getPublication() {
        return publication;
    }

    public Visit setPublication(Publication publication) {
        this.publication = publication;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Visit setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public Visit setPlace(String place) {
        this.place = place;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Visit setMessage(String message) {
        this.message = message;
        return this;
    }

    public static Visit build(JSONObject jsonVisit) {
        if(jsonVisit == null) return null;
        Visit visit= new Visit();
        try {
            visit.setId(jsonVisit.getString("_id"));
            visit.setUser(User.build(jsonVisit.getJSONObject("user")));
            visit.setPublication(Publication.build(jsonVisit.getJSONObject("publication")));
            visit.setDate(jsonVisit.getString("date"));
            visit.setPlace(jsonVisit.getString("place"));
            visit.setMessage(jsonVisit.getString("message"));
            return visit;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Visit> build(JSONArray jsonVisits) {
        if(jsonVisits == null) return null;
        int length = jsonVisits.length();
        List<Visit> visits = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                visits.add(Visit.build(jsonVisits.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return visits;
    }
}
