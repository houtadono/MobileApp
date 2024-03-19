package vn.id.houta.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaos.view.PinView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Random;


import vn.id.houta.myapplication.cusmodule.NonSwipeableViewPager;

public class ForgetActivity extends AppCompatActivity {

    private static NonSwipeableViewPager viewPager;
    private MyPagerAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new MyPagerAdapter(this);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        viewPager.setSwipeEnabled(false);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int curent_page = viewPager.getCurrentItem();
                if (curent_page == 0)
                    this.finish();
                else
                    viewPager.setCurrentItem(0);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends PagerAdapter {
        Context context;
        Button btn_get_authcode, btn_confimr_authcode, btn_confimr_restore;
        EditText email;
        PinView pin;
        String txt_email;
        int error_code = 0;
        private ArrayList<EditText> editTexts = new ArrayList<>();

        public MyPagerAdapter(Context context){
            this.context = context;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (LinearLayout) object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = null;
            if(position == 0){
                view = layoutInflater.inflate(R.layout.forgetpass_type_email,container,false);
                btn_get_authcode = (Button) view.findViewById(R.id.btn_get_authcode);
                email = (EditText) view.findViewById(R.id.email);
                btn_get_authcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_email = email.getText().toString();
                        error_code = 0;

//                        DatabaseReference ref = database.getReference("/Users");
//                        Query query = ref.orderByChild("search").equalTo(txt_email);
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    String code = generateRandomCode(4);
//                                    long expirationTime = System.currentTimeMillis() / 1000 + 5 * 60;
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                        String userId = snapshot.getKey();
//                                        DatabaseReference codeRef = FirebaseDatabase.getInstance().getReference("/Codes").child(userId);
//                                        // Create map to store code information
//                                        Map<String, Object> infoCode = new HashMap<>();
//                                        infoCode.put("search", txt_email);
//                                        infoCode.put("code", code);
//                                        infoCode.put("expiration_time", expirationTime);
//                                        // Update or set code information
//                                        codeRef.updateChildren(infoCode);
//                                        error_code = 0;
//                                    }
//                                }else{
//                                    error_code = 1;
//                                    Toast.makeText(ForgetActivity.this, "Địa chỉ Email này chưa đăng ký", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Toast.makeText(ForgetActivity.this, "Có lỗi xảy ra! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
//                                error_code = 1;
//                            }
//                        });

//                        try {
//                            JSONObject requestData = new JSONObject();
//                            requestData.put("email", email);
//
//                            URL url = new URL("http://houtadono.pythonanywhere.com/request_reset_password");
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setRequestMethod("POST");
//                            connection.setRequestProperty("Content-Type", "application/json");
//                            connection.setDoOutput(true);
//
//                            // Ghi dữ liệu JSON vào luồng đầu ra của kết nối
//                            OutputStream os = connection.getOutputStream();
//                            os.write(requestData.toString().getBytes());
//                            os.flush();
//                            os.close();
//
//                            int responseCode = connection.getResponseCode();
//                            System.out.println("Response Code: " + responseCode);
//
//                            if (responseCode == HttpURLConnection.HTTP_OK) {
//                                System.out.println("Reset password request sent successfully.");
//                            } else {
//                                System.out.println("Failed to send reset password request.");
//                            }
//                            connection.disconnect();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        if(error_code==0) {
                            auth.sendPasswordResetEmail(txt_email)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ForgetActivity.this, "Link đặt lại mật khẩu đã được gửi tới Email đăng ký của bạn", Toast.LENGTH_SHORT).show();
                                            viewPager.setCurrentItem(1);
                                            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                                            //                                        Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                                            //                                        startActivity(intent);
                                            //                                        finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ForgetActivity.this, "Có lỗi xảy ra! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                            error_code = 1;
                                        }
                                    });
                        }
                    }
                });
            }else if(position == 1){
                view = layoutInflater.inflate(R.layout.forgetpass_authcode,container,false);
                pin = (PinView) view.findViewById(R.id.pinview);
                pin.setText("");

                btn_confimr_authcode = (Button) view.findViewById(R.id.btn_confimr_authcode);
                btn_confimr_authcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pin_code = pin.getText().toString();
                        if(pin_code.length() == 4){
                            DatabaseReference codeRef = database.getReference("/Codes");
                            System.out.println("email: " + txt_email);
                            Query query = codeRef.orderByChild("search").equalTo(txt_email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String code = (String) snapshot.child("code").getValue().toString().trim();
                                        String actionCode = (String) snapshot.child("actionCode").getValue().toString().trim();
                                        System.out.println(code);
                                        auth.verifyPasswordResetCode(actionCode).addOnSuccessListener(email_code -> {
                                            System.out.println("email-code: "+ email_code);
                                        });
                                        break;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Đã xảy ra lỗi: " + databaseError.getMessage());
                                }
                            });
//                            viewPager.setCurrentItem(2);
//                            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                        }
                    }
                });
            }else if (position == 2){
                view = layoutInflater.inflate(R.layout.forgetpass_restore,container,false);
                btn_confimr_restore = (Button) view.findViewById(R.id.btn_confimr_restore);
                btn_confimr_restore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                        Toast.makeText(ForgetActivity.this, "Khôi phục mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            container.addView(view);
            return view;

        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout)object);
        }

        @Override
        public int getCount() {
            return 3;
        }

        private String generateRandomCode(int length) {
            String characters = "0123456789";
            Random random = new Random();
            StringBuilder code = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                code.append(characters.charAt(random.nextInt(characters.length())));
            }
            return code.toString();
        }
    }
}
