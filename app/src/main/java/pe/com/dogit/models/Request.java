package pe.com.dogit.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private String id;
    private User user;
    private Publication publication;
    private String requestDate;
    private String message;
    private String status;

    public Request() {
    }

    public Request(String id, User user, Publication publication, String requestDate, String message, String status) {
        this.setId(id);
        this.setUser(user);
        this.setPublication(publication);
        this.setRequestDate(requestDate);
        this.setMessage(message);
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public Request setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Request setUser(User user) {
        this.user = user;
        return this;
    }

    public Publication getPublication() {
        return publication;
    }

    public Request setPublication(Publication publication) {
        this.publication = publication;
        return this;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public Request setRequestDate(String requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Request setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Request setStatus(String status) {
        this.status = status;
        return this;
    }

    public static Request build(JSONObject jsonRequest) {
        if(jsonRequest == null) return null;
        Request request= new Request();
        try {
            request.setId(jsonRequest.getString("_id"));
            request.setUser(User.build(jsonRequest.getJSONObject("user")));
            request.setPublication(Publication.build(jsonRequest.getJSONObject("publication")));
            request.setMessage(jsonRequest.getString("message"));
            request.setStatus(jsonRequest.getString("status"));
            return request;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Request> build(JSONArray jsonRequests) {
        if(jsonRequests == null) return null;
        int length = jsonRequests.length();
        List<Request> requests = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                requests.add(Request.build(jsonRequests.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return requests;
    }
}
