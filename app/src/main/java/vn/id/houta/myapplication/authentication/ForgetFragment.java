package vn.id.houta.myapplication.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vn.id.houta.myapplication.R;

public class ForgetFragment extends Fragment {
    static FirebaseAuth auth;
    static FirebaseDatabase database;
    static String txt_email;
    static PinView pin;
    static String actionCode = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ImageView imageView = requireActivity().findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        layoutParams.topMargin = 40;
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left);
        imageView.startAnimation(animation);
        imageView.setLayoutParams(layoutParams);

        final EditText editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        view.findViewById(R.id.btn_get_authcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email = editTextEmail.getText().toString();
                auth.sendPasswordResetEmail(txt_email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Link đặt lại mật khẩu đã được gửi tới Email đăng ký của bạn", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_in_right, R.anim.slide_out_left,
                                            R.anim.slide_in_left, R.anim.slide_out_right
                                    )
                                    .hide(ForgetFragment.this)
                                    .add(R.id.frame_layout, new ForgetCodeFragment())
                                    .addToBackStack(null)
                                    .commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Có lỗi xảy ra! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return view;
    }
    public static class ForgetCodeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_forget_code, container, false);
            pin = (PinView) view.findViewById(R.id.pinview);
            pin.setText("");
            view.findViewById(R.id.btn_confirm_authcode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pin_code = pin.getText().toString();
                    if (pin_code.length() == 4) {
                        DatabaseReference codeRef = database.getReference("/Codes");
                        System.out.println("email: " + txt_email);
                        Query query = codeRef.orderByChild("search").equalTo(txt_email);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String code = (String) snapshot.child("code").getValue().toString().trim();
                                    String actionCodeFB = (String) snapshot.child("actionCode").getValue().toString().trim();

                                    if(pin_code.equals(code)){
                                        auth.verifyPasswordResetCode(actionCodeFB).addOnSuccessListener(email_code -> {
                                            System.out.println("email-code: " + email_code);
                                            if(email_code.equals(txt_email)){
                                                actionCode = actionCodeFB;
                                                Toast.makeText(getActivity(), "Mã xác nhận chính xác", Toast.LENGTH_SHORT).show();
                                                requireActivity().getSupportFragmentManager().beginTransaction()
                                                    .setCustomAnimations(
                                                            R.anim.slide_in_right, R.anim.slide_out_left,
                                                            R.anim.slide_in_left, R.anim.slide_out_right
                                                    )
                                                    .hide(ForgetCodeFragment.this)
                                                    .add(R.id.frame_layout, new ForgetResetFragment())
                                                    .addToBackStack(null)
                                                    .commit();
                                            }else
                                                Toast.makeText(getActivity(), "Mã xác chưa chính xác hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Mã xác chưa chính xác hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                                        });
                                    }else
                                        Toast.makeText(getActivity(), "Mã xác nhận chưa chính xác!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "Có lỗi xảy ra! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                System.out.println("Đã xảy ra lỗi: " + databaseError.getMessage());
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(), "Hãy nhập đủ mã xác nhận!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    }
    public static class ForgetResetFragment extends Fragment {
        EditText password, repassword;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_forget_reset, container, false);
            password = view.findViewById(R.id.password);
            repassword = view.findViewById(R.id.repassword);
            view.findViewById(R.id.btn_confimr_restore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String txt_password = password.getText().toString().trim();
                    if (txt_password.equals(repassword.getText().toString().trim())){
                        if(txt_password.length() < 6){
                            Toast.makeText(getActivity(), "Mật khẩu quá yếu! Vui lòng nhập mật khẩu khác", Toast.LENGTH_SHORT).show();
                        }else{
                            auth.confirmPasswordReset(actionCode, txt_password).addOnSuccessListener(resp -> {
                                Toast.makeText(getActivity(), "Khôi phục mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Quá trình khôi phục mật khẩu hết hạn!", Toast.LENGTH_SHORT).show();
                            });
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_in_bottom, R.anim.slide_out_left
                                    )
                                    .replace(R.id.frame_layout, new LoginFragment())
                                    .commit();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Mật khẩu phải không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    }
}