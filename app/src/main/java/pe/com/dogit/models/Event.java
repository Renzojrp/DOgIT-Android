package pe.com.dogit.models;

import android.graphics.Typeface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id;
    private User user;
    private String photo;
    private String name;
    private String address;
    private String description;
    private String date;
    private Integer capacity;

    public Event() {
    }

    public Event(String id, User user, String photo, String name, String address, String description, String date, Integer capacity) {
        this.setId(id);
        this.setUser(user);
        this.setPhoto(photo);
        this.setName(name);
        this.setAddress(address);
        this.setDescription(description);
        this.setDate(date);
        this.setCapacity(capacity);
    }

    public String getId() {
        return id;
    }

    public Event setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Event setUser(User user) {
        this.user = user;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Event setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Event setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Event setDate(String date) {
        this.date = date;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Event setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public static Event build(JSONObject jsonEvent) {
        if(jsonEvent == null) return null;
        Event event = new Event();
        try {
            event.setId(jsonEvent.getString("_id"))
                    .setUser(User.build(jsonEvent.getJSONObject("user")))
                    .setName(jsonEvent.getString("name"))
                    .setPhoto(jsonEvent.getString("photo"))
                    .setAddress(jsonEvent.getString("address"))
                    .setDescription(jsonEvent.getString("description"))
                    .setDate(jsonEvent.getString("date"))
                    .setCapacity(jsonEvent.getInt("capacity"));
            return event;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Event> build(JSONArray jsonEvents) {
        if(jsonEvents == null) return null;
        int length = jsonEvents.length();
        List<Event> events = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                events.add(Event.build(jsonEvents.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return events;
    }



}
