package pe.com.dogit.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextView signUpTextView;
    TextView forgetPasswordTextView;
    ProgressBar signInProgressBar;

    boolean correctEmail = false;
    boolean correctPassword= false;

    User user;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        signInProgressBar = findViewById(R.id.signInProgressBar);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);

        signInProgressBar.setVisibility(View.GONE);
    }

    public void signInClick(View v) {
        signInProgressBar.setVisibility(View.VISIBLE);

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputLayout.getEditText().getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        if(passwordTextInputLayout.getEditText().getText().toString().length() < 8) {
            passwordTextInputLayout.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
        } else {
            passwordTextInputLayout.setError(null);
            correctPassword = true;
        }

        if(correctEmail && correctPassword) {
            signIn(emailTextInputLayout.getEditText().getText().toString(), passwordTextInputLayout.getEditText().getText().toString());
        } else {
            signInProgressBar.setVisibility(View.INVISIBLE);
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
                            token = response.getString("token");
                            DOgITApp.getInstance().setCurrentToken("Bearer " + token);
                            DOgITApp.getInstance().setMyUser(user);
                            SplashActivity.setData(LoginActivity.this,"user" ,user.getEmail());
                            SplashActivity.setData(LoginActivity.this,"password" ,user.getPassword());
                            if (user.getStatus().equals("A")) {
                                Toast.makeText(getApplicationContext(),  getString(R.string.hello) + " " + DOgITApp.getInstance().getMyUser().getName(), Toast.LENGTH_SHORT).show();
                                if (user.getType().equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                }
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),  R.string.user_locked, Toast.LENGTH_SHORT).show();
                            }
                            signInProgressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void goToSendMailActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        RecoverPasswordActivity.class));
    }
}
