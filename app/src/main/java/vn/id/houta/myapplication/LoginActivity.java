package vn.id.houta.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    CheckBox checkBox;
    boolean remember;
    TextView btn_register, btn_forget;
    Button  btn_login;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();



        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String txt_email = sharedPreferences.getString("Email", "");
        String txt_password = sharedPreferences.getString("Password", "");
        remember = !txt_password.isEmpty();

        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        btn_forget = findViewById(R.id.btn_forget);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        email.setText(txt_email);
        password.setText(txt_password);
        checkBox.setChecked(remember);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
            }
        });
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                remember = checkBox.isChecked();

                if (txt_email.isEmpty() || txt_password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Hãy nhập tất cả các mục", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(task -> {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Email", txt_email);
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    if(remember){
                                        editor.putString("Password", txt_password);
                                    }else{
                                        editor.putString("Password", "");
                                    }
                                } else {
                                    editor.putString("Password", "");
                                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                                }
                                editor.apply();
                            });
                }
            }
        });

    }
}