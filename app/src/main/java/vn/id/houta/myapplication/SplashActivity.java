package vn.id.houta.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    boolean isFirstRun;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MotionLayout motionLayout = findViewById(R.id.motionLayout);
        new Handler().postDelayed(() -> {
            motionLayout.transitionToEnd();
            new Handler().postDelayed(() -> {
                sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
                Intent i;
                if (!isFirstRun) {
                    auth = FirebaseAuth.getInstance();
                    if(auth.getCurrentUser() != null)
                        i = new Intent(getApplicationContext(), MainActivity.class);
                    else
                        i = new Intent(getApplicationContext(), AuthenticationActivity.class);
                }else{
                    i = new Intent(getApplicationContext(), OnboardingActivity.class);
                }
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_top);
            }, 0);
        }, 0);
    }
}