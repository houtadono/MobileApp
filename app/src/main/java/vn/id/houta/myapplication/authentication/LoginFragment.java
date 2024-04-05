package vn.id.houta.myapplication.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import vn.id.houta.myapplication.MainActivity;
import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.util.NetworkUtils;

public class LoginFragment extends Fragment {
    View view;
    EditText email, password;
    CheckBox checkBox;
    boolean remember;
    TextView btn_register, btn_forget;
    Button btn_login;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Intent appLinkIntent = this.getActivity().getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


        sharedPreferences = this.getActivity().getSharedPreferences("MyPrefs", this.getContext().MODE_PRIVATE);
        String txt_email = sharedPreferences.getString("Email", "");
        String txt_password = sharedPreferences.getString("Password", "");
        remember = !txt_password.isEmpty();

        auth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        checkBox = view.findViewById(R.id.checkBox);
        btn_forget = view.findViewById(R.id.btn_forget);
        btn_register = view.findViewById(R.id.btn_register);
        btn_login = view.findViewById(R.id.btn_login);

        email.setText(txt_email);
        password.setText(txt_password);
        checkBox.setChecked(remember);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_bottom, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_bottom
                );
                fragmentTransaction.hide(LoginFragment.this);
                fragmentTransaction.replace(R.id.frame_layout, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right
                );
                fragmentTransaction.hide(LoginFragment.this);
                fragmentTransaction.replace(R.id.frame_layout, new ForgetFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();
                remember = checkBox.isChecked();

                if (txt_email.isEmpty() || txt_password.isEmpty()){
                    Toast.makeText(getActivity(), "Hãy nhập tất cả các mục", Toast.LENGTH_SHORT).show();
                } else {
                    if(NetworkUtils.isNetworkAvailable(requireContext())) {
                        performLogin(txt_email, txt_password);
                    }else{
                        NetworkUtils.showNetworkAlert(getContext(), new Runnable() {
                            @Override
                            public void run() {
                                if (NetworkUtils.isNetworkAvailable(requireContext())) {
                                    performLogin(txt_email, txt_password);
                                } else {
                                    NetworkUtils.showNetworkAlert(getContext(), this);
                                }
                            }
                        });
                    }
                }
            }
        });
        return view;
    }
    private void performLogin(String txt_email, String txt_password) {
        auth.signInWithEmailAndPassword(txt_email, txt_password)
            .addOnCompleteListener(task -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Email", txt_email);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    requireActivity().startActivity(intent);
                    requireActivity().finish();
                    Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if (remember) {
                        editor.putString("Password", txt_password);
                    } else {
                        editor.putString("Password", "");
                    }
                } else {
                    editor.putString("Password", "");
                    Toast.makeText(getActivity(), "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView imageView = requireActivity().findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        layoutParams.topMargin = -20;
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
        imageView.startAnimation(animation);
        imageView.setLayoutParams(layoutParams);
    }
}