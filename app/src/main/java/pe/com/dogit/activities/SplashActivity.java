package pe.com.dogit.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class SplashActivity extends AppCompatActivity {

    Intent intent;

    User user;
    String token;

    private static final String STRING_PREFERENCES = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!getData(SplashActivity.this,"user").equals("") && !getData(SplashActivity.this,"password").equals("")) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            signIn(getData(SplashActivity.this,"user"), getData(SplashActivity.this,"password"));
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public static void setData(Context context, String keyPref, String data) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(keyPref, data).apply();
    }

    public static void changeData(Context context, String keyPref, String data) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(keyPref, data).apply();
    }

    public String getData(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    private void signIn(String email, final String password) {
        AndroidNetworking.post(DOgITService.SIGNIN_URL)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            user = User.build(response.getJSONObject("user"));
                            user.setPassword(password);
                            token = response.getString("token");
                            DOgITApp.getInstance().setCurrentToken("Bearer " + token);
                            DOgITApp.getInstance().setCurrentUser(user);
                            SplashActivity.setData(SplashActivity.this,"user" ,user.getEmail());
                            SplashActivity.setData(SplashActivity.this,"password" ,user.getPassword());
                            SplashActivity.setData(SplashActivity.this,"token" ,token);
                            Toast.makeText(getApplicationContext(),  getString(R.string.hello) + " " + DOgITApp.getInstance().getCurrentUser().getName(), Toast.LENGTH_SHORT).show();
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
