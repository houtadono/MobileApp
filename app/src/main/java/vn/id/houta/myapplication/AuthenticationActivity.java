package vn.id.houta.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import vn.id.houta.myapplication.authentication.LoginFragment;
import vn.id.houta.myapplication.databinding.ActivityMainBinding;

public class AuthenticationActivity extends AppCompatActivity {
    private LoginFragment loginFrament;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        loginFrament = new LoginFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top,
                R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_layout, loginFrament).commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getSupportFragmentManager().popBackStack();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}