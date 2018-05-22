package pe.com.dogit.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.R;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailEditText;
    TextInputLayout passwordEditText;
    TextView signUpTextView;
    ProgressBar signInProgressBar;

    private static final String STRING_PREFERENCES = "preferences";

    boolean correctEmail = false;
    boolean correctPassword= false;

    User user;
    String token;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        intent = new Intent(LoginActivity.this, MainActivity.class);
        if(!getData("user").equals("") && !getData("password").equals("")) {
            signIn(getData("user"), getData("password"));
        }

        emailEditText = findViewById(R.id.emailTextInputLayout);
        passwordEditText = findViewById(R.id.passwordTextInputLayout);
        signInProgressBar = findViewById(R.id.signInProgressBar);
        signUpTextView = findViewById(R.id.signUpTextView);
        signInProgressBar.setVisibility(View.GONE);
    }

    private void setData(String keyPref, String data) {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
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

    private String getData(String keyPref) {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    public void signInClick(View v) {
        signInProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getEditText().getText().toString()).matches()){
            emailEditText.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
            signInProgressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            emailEditText.setError(null);
            correctEmail = true;
        }

        if(passwordEditText.getEditText().getText().toString().length() < 8) {
            passwordEditText.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
            signInProgressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            passwordEditText.setError(null);
            correctPassword = true;
        }

        if(correctEmail && correctPassword) {
            signIn(emailEditText.getEditText().getText().toString(), passwordEditText.getEditText().getText().toString());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
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
                            setData("user" ,user.getEmail());
                            setData("password" ,user.getPassword());
                            setData("token" ,token);
                            Toast.makeText(getApplicationContext(),  getString(R.string.hello) + " " + DOgITApp.getInstance().getCurrentUser().getName(), Toast.LENGTH_SHORT).show();
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        signInProgressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
                        signInProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void goToRegisterActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        RegisterActivity.class));
    }
}
