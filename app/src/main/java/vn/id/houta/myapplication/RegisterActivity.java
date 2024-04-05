package vn.id.houta.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, repassword;
    CheckBox checkBox;
    Button btn_register;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        checkBox = findViewById(R.id.checkBox);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_repassword = repassword.getText().toString();
                boolean check_box = checkBox.isChecked();

                if (txt_email.isEmpty() || txt_password.isEmpty() || txt_repassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập tất cả các mục", Toast.LENGTH_SHORT).show();
                } else if (!check_box){
                    Toast.makeText(RegisterActivity.this, "Bạn chưa đồng ý với các điều khoản và điều kiện", Toast.LENGTH_SHORT).show();
                }else if (txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.contentEquals(txt_repassword)){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_email, txt_password);
                }
            }
        });
    }

    private void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", email);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, AuthenticationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Bạn không thể đăng ký bằng email hoặc mật khẩu này", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}