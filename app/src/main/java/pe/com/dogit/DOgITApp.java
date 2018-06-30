package pe.com.dogit;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import java.util.List;

import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.Blog;
import pe.com.dogit.models.Event;
import pe.com.dogit.models.Pet;
import pe.com.dogit.models.Postadoption;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.Request;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class DOgITApp extends Application {
    private static DOgITApp instance;
    private DOgITService DOgITService;

    public DOgITApp() {
        super();
        instance = this;
    }

    public static DOgITApp getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        DOgITService = new DOgITService();
    }

    public User getMyUser() {
        return DOgITService.getMyUser();
    }

    public DOgITApp setMyUser(User user) {
        DOgITService.setMyUser(user);
        return this;
    }

    public User getCurrentUser() {
        return DOgITService.getMyUser();
    }

    public DOgITApp setCurrentUser(User user) {
        DOgITService.setMyUser(user);
        return this;
    }

    public String getCurrentToken() {
        return DOgITService.getCurrentToken();
    }

    public DOgITApp setCurrentToken(String token) {
        DOgITService.setCurrentToken(token);
        return this;
    }

    public Pet getCurrentPet() {
        return DOgITService.getCurrentPet();
    }

    public DOgITApp setCurrentPet(Pet pet) {
        DOgITService.setCurrentPet(pet);
        return this;
    }

    public List<Pet> getCurrentPets() {
        return DOgITService.getCurrentPets();
    }

    public DOgITApp setCurrentPets(List<Pet> pets){
        DOgITService.setCurrentPets(pets);
        return this;
    }

    public Publication getCurrentPublication() {
        return DOgITService.getCurrentPublication();
    }

    public DOgITApp setCurrentPublication(Publication publication) {
        DOgITService.setCurrentPublication(publication);
        return this;
    }

    public Event getCurrentEvent() {
        return DOgITService.getCurrentEvent();
    }

    public DOgITApp setCurrentEvent(Event event) {
        DOgITService.setCurrentEvent(event);
        return this;
    }

    public Blog getCurrentBlog() {
        return DOgITService.getCurrentBlog();
    }

    public DOgITApp setCurrentBlog(Blog blog) {
        DOgITService.setCurrentBlog(blog);
        return this;
    }

    public Request getCurrentRequest() {
        return DOgITService.getCurrentRequest();
    }

    public DOgITApp setCurrentRequest(Request request) {
        DOgITService.setCurrentRequest(request);
        return this;
    }

    public Adoption getCurrentAdoption() {
        return DOgITService.getCurrentAdoption();
    }

    public DOgITApp setCurrentAdoption(Adoption adoption) {
        DOgITService.setCurrentAdoption(adoption);
        return this;
    }

    public Postadoption getCurrentPostadoption() {
        return DOgITService.getCurrentPostadoption();
    }

    public DOgITApp setCurrentPostadoption(Postadoption postadoption) {
        DOgITService.setCurrentPostadoption(postadoption);
        return this;
    }
}
