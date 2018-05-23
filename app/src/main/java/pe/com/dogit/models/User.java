package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String signupDate;
    private String birthDate;
    private String gender;
    private String mobilePhone;
    private String photo;
    private String address;
    private String workPlace;
    private int dni;

    public User() {
    }

    public User(String id, String email, String password, String name, String lastName, String signupDate,
                String birthDate, String gender, String mobilePhone, String photo, String address, String workPlace, int dni) {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setName(name);
        this.setLastName(lastName);
        this.setSignupDate(signupDate);
        this.setBirthDate(birthDate);
        this.setGender(gender);
        this.setMobilePhone(mobilePhone);
        this.setPhoto(photo);
        this.setAddress(address);
        this.setWorkPlace(workPlace);
        this.setDni(dni);
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public User setSignupDate(String signupDate) {
        this.signupDate = signupDate;
        return this;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public User setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public User setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public User setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public User setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
        return this;
    }

    public int getDni() {
        return dni;
    }

    public User setDni(int dni) {
        this.dni = dni;
        return this;
    }

    public static User build(JSONObject jsonUser) {
        if(jsonUser == null) return null;
        User user = new User();
        try {
            user.setId(jsonUser.getString("_id"))
                    .setEmail(jsonUser.getString("email"))
                    .setPassword(jsonUser.getString("password"))
                    .setName(jsonUser.getString("name"))
                    .setLastName(jsonUser.getString("lastName"))
                    .setSignupDate(jsonUser.getString("signupDate"))
                    .setBirthDate(jsonUser.getString("birthDate"))
                    .setGender(jsonUser.getString("gender"))
                    .setMobilePhone(jsonUser.getString("mobilePhone"))
                    .setPhoto(jsonUser.getString("photo"))
                    .setDni(jsonUser.getInt("dni"))
                    .setAddress(jsonUser.getString("address"))
                    .setWorkPlace(jsonUser.getString("workPlace"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> build(JSONArray jsonUsers) {
        if(jsonUsers == null) return null;
        int length = jsonUsers.length();
        List<User> users = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                users.add(User.build(jsonUsers.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return users;
    }
}
