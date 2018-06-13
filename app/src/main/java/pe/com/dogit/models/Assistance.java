package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Assistance {
    private String id;
    private User user;
    private Event event;

    public Assistance() {
    }

    public Assistance(String id, User user, Event event) {
        this.setId(id);
        this.setUser(user);
        this.setEvent(event);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Assistance setUser(User user) {
        this.user = user;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Assistance setEvent(Event event) {
        this.event = event;
        return  this;
    }

    public static Assistance build(JSONObject jsonAssistence) {
        if(jsonAssistence == null) return null;
        Assistance assistance = new Assistance();
        try {
            assistance.setId(jsonAssistence.getString("_id"));
            assistance.setUser(User.build(jsonAssistence.getJSONObject("user")));
            assistance.setEvent(Event.build(jsonAssistence.getJSONObject("event")));
            return assistance;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Assistance> build(JSONArray jsonAssistences) {
        if(jsonAssistences == null) return null;
        int length = jsonAssistences.length();
        List<Assistance> assists = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                assists.add(Assistance.build(jsonAssistences.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return assists;
    }
}
