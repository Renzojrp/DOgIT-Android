package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Pet {
    private String id;
    private String name;
    private String gender;
    private Double weigth;
    private Double size;
    private int age;
    private String rescueDate;
    private String photo;
    private String description;
    private User user;

    public Pet() {
    }


    public Pet(String id, String name, String gender, Double weigth, Double size, int age, String rescueDate, String photo, String description, User user) {
        this.setId(id);
        this.setName(name);
        this.setGender(gender);
        this.setWeigth(weigth);
        this.setSize(size);
        this.setAge(age);
        this.setRescueDate(rescueDate);
        this.setPhoto(photo);
        this.setDescription(description);
        this.setUser(user);
    }


    public String getId() {
        return id;
    }

    public Pet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Pet setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Double getWeigth() {
        return weigth;
    }

    public Pet setWeigth(Double weigth) {
        this.weigth = weigth;
        return this;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public int getAge() {
        return age;
    }

    public Pet setAge(int age) {
        this.age = age;
        return this;
    }

    public String getRescueDate() {
        return rescueDate;
    }

    public Pet setRescueDate(String rescueDate) {
        this.rescueDate = rescueDate;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Pet setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Pet setDescription(String description) {
        this.description = description;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Pet setUser(User user) {
        this.user = user;
        return this;
    }

    public static Pet build(JSONObject jsonPet) {
        if(jsonPet == null) return null;
        Pet pet = new Pet();
        try {
            pet.setId(jsonPet.getString("_id"));
            pet.setName(jsonPet.getString("name"));
            pet.setGender(jsonPet.getString("gender"));
            pet.setWeigth(jsonPet.getDouble("weigth"));
            pet.setAge(jsonPet.getInt("age"));
            pet.setSize(jsonPet.getDouble("size"));
            pet.setDescription(jsonPet.getString("description"));
            pet.setPhoto(jsonPet.getString("photo"));
            pet.setRescueDate(jsonPet.getString("rescue_date"));
            pet.setUser(User.build(jsonPet.getJSONObject("user")));

            return pet;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Pet> build(JSONArray jsonPets) {
        if(jsonPets == null) return null;
        int length = jsonPets.length();
        List<Pet> pets = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                pets.add(Pet.build(jsonPets.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return pets;
    }
}