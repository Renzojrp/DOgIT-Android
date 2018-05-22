package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Assistence {
    private String id;
    private User user;
    private Event event;

    public Assistence() {
    }

    public Assistence(String id, User user, Event event) {
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

    public Assistence setUser(User user) {
        this.user = user;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Assistence setEvent(Event event) {
        this.event = event;
        return  this;
    }

    public static Assistence build(JSONObject jsonAssistence) {
        if(jsonAssistence == null) return null;
        Assistence assistence = new Assistence();
        try {
            assistence.setId(jsonAssistence.getString("_id"));
            assistence.setUser(User.build(jsonAssistence.getJSONObject("user")));
            assistence.setEvent(Event.build(jsonAssistence.getJSONObject("event")));
            return assistence;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Assistence> build(JSONArray jsonAssistences) {
        if(jsonAssistences == null) return null;
        int length = jsonAssistences.length();
        List<Assistence> assistences = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                assistences.add(Assistence.build(jsonAssistences.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return assistences;
    }
}
