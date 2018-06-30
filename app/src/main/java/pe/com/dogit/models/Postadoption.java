package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Postadoption {
    private String id;
    private Adoption adoption;
    private String photo;
    private String description;

    public Postadoption() {
    }

    public Postadoption(String id, Adoption adoption, String photo, String description) {
        this.setId(id);
        this.setAdoption(adoption);
        this.setPhoto(photo);
        this.setDescription(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Adoption getAdoption() {
        return adoption;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Postadoption build(JSONObject jsonPostadoption) {
        if(jsonPostadoption == null) return null;
        Postadoption postadoption = new Postadoption();
        try {
            postadoption.setId(jsonPostadoption.getString("_id"));
            postadoption.setAdoption(Adoption.build(jsonPostadoption.getJSONObject("adoption")));
            postadoption.setPhoto(jsonPostadoption.getString("photo"));
            postadoption.setDescription(jsonPostadoption.getString("description"));
            return postadoption;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Postadoption> build(JSONArray jsonPostadoptions) {
        if(jsonPostadoptions == null) return null;
        int length = jsonPostadoptions.length();
        List<Postadoption> postadoptions = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                postadoptions.add(Postadoption.build(jsonPostadoptions.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return postadoptions;
    }
}
