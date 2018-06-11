package pe.com.dogit.network;

import java.util.List;

import pe.com.dogit.models.Blog;
import pe.com.dogit.models.Event;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.Request;
import pe.com.dogit.models.User;

public class DOgITService {
    public static String SIGNIN_URL = "https://dogit.herokuapp.com/api/signin";
    public static String SIGNUP_URL = "https://dogit.herokuapp.com/api/signup";
    public static String USERS_EDIT_URL = "https://dogit.herokuapp.com/api/user/{user_id}";
    public static String USER_EMAIL_URL = "https://dogit.herokuapp.com/api/user/email/{email}";
    public static String PET_URL = "https://dogit.herokuapp.com/api/pet";
    public static String PET_EDIT_URL = "https://dogit.herokuapp.com/api/pet/{pet_id}";
    public static String PET_USER_URL = "https://dogit.herokuapp.com/api/pet/user/{user_id}";
    public static String PUBLICATION_EDIT_URL = "https://dogit.herokuapp.com/api/publication/{publication_id}";
    public static String PUBLICATION_USER_URL = "https://dogit.herokuapp.com/api/publication/user/{user_id}";
    public static String PUBLICATION_PET_URL = "https://dogit.herokuapp.com/api/publication/pet/{pet_id}";
    public static String PUBLICATION_URL = "https://dogit.herokuapp.com/api/publication";
    public static String EVENT_URL = "https://dogit.herokuapp.com/api/event";
    public static String EVENT_EDIT_URL = "https://dogit.herokuapp.com/api/event/{event_id}";
    public static String ASSISTENCE_URL = "https://dogit.herokuapp.com/api/assistance";
    public static String ASSISTENCE_EVENT_URL = "https://dogit.herokuapp.com/api/assistance/event/{event_id}";
    public static String ASSISTENCE_USER_URL = "https://dogit.herokuapp.com/api/assistance/user/{user_id}";
    public static String BLOG_URL = "https://dogit.herokuapp.com/api/blog";
    public static String BLOG_USER_URL = "https://dogit.herokuapp.com/api/blog/user/{user_id}";
    public static String BLOG_EDIT_URL = "https://dogit.herokuapp.com/api/blog/{blog_id}";
    public static String REQUEST_URL = "https://dogit.herokuapp.com/api/request";
    public static String REQUEST_USER_URL = "https://dogit.herokuapp.com/api/request/user/{user_id}";

    private User currentUser;
    private String currentToken;
    private Pet currentPet;
    private List<Pet> currentPets;
    private Publication currentPublication;
    private Event currentEvent;
    private Blog currentBlog;
    private Request currentRequest;

    public User getCurrentUser() {
        return currentUser;
    }

    public DOgITService setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public DOgITService setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
        return this;
    }

    public Pet getCurrentPet() {
        return currentPet;
    }

    public DOgITService setCurrentPet(Pet currentPet) {
        this.currentPet = currentPet;
        return this;
    }

    public List<Pet> getCurrentPets() {
        return currentPets;
    }

    public DOgITService setCurrentPets(List<Pet> currentPets){
        this.currentPets = currentPets;
        return this;
    }

    public Publication getCurrentPublication() {
        return currentPublication;
    }

    public DOgITService setCurrentPublication(Publication currentPublication) {
        this.currentPublication = currentPublication;
        return this;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public DOgITService setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
        return this;
    }

    public Blog getCurrentBlog() {
        return currentBlog;
    }

    public DOgITService setCurrentBlog(Blog currentBlog) {
        this.currentBlog = currentBlog;
        return this;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public DOgITService setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
        return this;
    }
}
