package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Publication {
    private String id;
    private User user;
    private Pet pet;
    private String description;
    private String requirements;
    private String publicationDate;
    private String status;
    private String photo;
    private String address;
    private String date;

    public Publication() {
    }

    public Publication(String id, User user, Pet pet, String description, String requirements, String publicationDate, String status) {
        this.setId(id);
        this.setUser(user);
        this.setPet(pet);
        this.setDescription(description);
        this.setRequirements(requirements);
        this.setPublicationDate(publicationDate);
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public Publication setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Publication setUser(User user) {
        this.user = user;
        return this;
    }

    public Pet getPet() {
        return pet;
    }

    public Publication setPet(Pet pet) {
        this.pet = pet;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Publication setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRequirements() {
        return requirements;
    }

    public Publication setRequirements(String requirements) {
        this.requirements = requirements;
        return this;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public Publication setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Publication setStatus(String status) {
        this.status = status;
        return this;
    }

    public static Publication build(JSONObject jsonPublication) {
        if(jsonPublication == null) return null;
        Publication publication = new Publication();
        try {
            publication.setId(jsonPublication.getString("_id"))
                    .setUser(User.build(jsonPublication.getJSONObject("user")))
                    .setPet(Pet.build(jsonPublication.getJSONObject("pet")))
                    .setDescription(jsonPublication.getString("description"))
                    .setRequirements(jsonPublication.getString("requirements"))
                    .setPublicationDate(jsonPublication.getString("publicationDate"))
                    .setStatus(jsonPublication.getString("status"))
                    .setDate(jsonPublication.getString("date"))
                    .setAddress(jsonPublication.getString("address"));
            return publication;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Publication> build(JSONArray jsonPublications) {
        if(jsonPublications == null) return null;
        int length = jsonPublications.length();
        List<Publication> publications = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                publications.add(Publication.build(jsonPublications.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return publications;
    }


    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public Publication setDate(String date) {
        this.date = date;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
