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
    private String status;

    public User() {
    }

    public User(String id, String email, String password, String name, String lastName, String signupDate,
                String birthDate, String gender, String mobilePhone, String photo, String address, String workPlace,
                int dni, String status) {
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
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static User build(JSONObject jsonUser) {
        if(jsonUser == null) return null;
        User user = new User();
        try {
            user.setId(jsonUser.getString("_id"));
            user.setEmail(jsonUser.getString("email"));
            user.setPassword(jsonUser.getString("password"));
            user.setName(jsonUser.getString("name"));
            user.setLastName(jsonUser.getString("lastName"));
            user.setSignupDate(jsonUser.getString("signupDate"));
            user.setBirthDate(jsonUser.getString("birthDate"));
            user.setGender(jsonUser.getString("gender"));
            user.setMobilePhone(jsonUser.getString("mobilePhone"));
            user.setPhoto(jsonUser.getString("photo"));
            user.setDni(jsonUser.getInt("dni"));
            user.setAddress(jsonUser.getString("address"));
            user.setWorkPlace(jsonUser.getString("workPlace"));
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
