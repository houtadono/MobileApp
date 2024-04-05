package vn.id.houta.myapplication.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import vn.id.houta.myapplication.AuthenticationActivity;
import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.RegisterActivity;

public class RegisterFragment extends Fragment {
    View view;
    Fragment currentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        RadioButton radio0 = view.findViewById(R.id.radio0);
        RadioButton radio1 = view.findViewById(R.id.radio1);
        RadioButton radio2 = view.findViewById(R.id.radio2);
        RadioButton radio3 = view.findViewById(R.id.radio3);
        RadioButton[] radioButtons = new RadioButton[]{radio0, radio1, radio2, radio3};
        for (int i = 0; i < radioButtons.length; i++) {
            final int position = i;
            radioButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < radioButtons.length; j++) {
                        radioButtons[j].setChecked(j == position);
                    }
                }
            });
        }

        MaterialCardView cardBoy = view.findViewById(R.id.cardBoy);
        MaterialCardView cardGirl = view.findViewById(R.id.cardGirl);
        cardBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBoy.setActivated(true);
                cardBoy.setStrokeColor(ContextCompat.getColorStateList(getContext(),R.color.my_primary));
                cardGirl.setActivated(false);
                cardGirl.setStrokeColor(ContextCompat.getColorStateList(getContext(),R.color.transparent));
                ((TextView)view.findViewById(R.id.textSexBoy)).setTextColor(
                        ContextCompat.getColorStateList(getContext(),R.color.my_primary)
                );
                ((TextView)view.findViewById(R.id.textSexGirl)).setTextColor(
                        ContextCompat.getColorStateList(getContext(),R.color.black)
                );
            }
        });
        cardGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBoy.setActivated(false);
                cardBoy.setStrokeColor(ContextCompat.getColorStateList(getContext(),R.color.transparent));
                cardGirl.setActivated(true);
                cardGirl.setStrokeColor(ContextCompat.getColorStateList(getContext(),R.color.my_primary));
                ((TextView)view.findViewById(R.id.textSexBoy)).setTextColor(
                        ContextCompat.getColorStateList(getContext(),R.color.black)
                );
                ((TextView)view.findViewById(R.id.textSexGirl)).setTextColor(
                        ContextCompat.getColorStateList(getContext(),R.color.my_primary)
                );
            }
        });
        EditText editTextName = (EditText) view.findViewById(R.id.editTextName);
        view.findViewById(R.id.btn_register1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = editTextName.getText().toString();
                if (txt_name.isEmpty()){
                    Toast.makeText(getActivity(), "Hãy nhập tên của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                int sex = -1;
                if( cardBoy.isActivated() ) {
                    sex = 1;
                }
                if (cardGirl.isActivated() ) {
                    sex = 0;
                }
                if( sex == - 1){
                    Toast.makeText(getActivity(), "Hãy chọn giới tính", Toast.LENGTH_SHORT).show();
                    return;
                }
                String age = null;
                for (int i = 0; i < radioButtons.length; i++) {
                    if (radioButtons[i].isChecked()){
                        age = radioButtons[i].getText().toString();
                        break;
                    }
                }
                if (age == null){
                    Toast.makeText(getActivity(), "Hãy chọn tuổi của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterSubFragment registerSubFragment = new RegisterSubFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_bottom, R.anim.slide_out_bottom,
                                R.anim.slide_in_bottom, R.anim.slide_out_bottom
                        )
                        .hide(RegisterFragment.this)
                        .add(R.id.frame_layout, registerSubFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public static class RegisterSubFragment extends Fragment {
        EditText email, password, repassword;
        CheckBox checkBox;
        private FirebaseAuth auth;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_register_sub, container, false);
            // TODO register logic + internet check
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            repassword = view.findViewById(R.id.repassword);
            checkBox = view.findViewById(R.id.checkBox);
            auth = FirebaseAuth.getInstance();

            view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();
                    String txt_repassword = repassword.getText().toString();
                    boolean check_box = checkBox.isChecked();

                    if (txt_email.isEmpty() || txt_password.isEmpty() || txt_repassword.isEmpty()){
                        Toast.makeText(requireContext(), "Hãy nhập tất cả các mục", Toast.LENGTH_SHORT).show();
                    } else if (!check_box){
                        Toast.makeText(requireContext(), "Bạn chưa đồng ý với các điều khoản và điều kiện", Toast.LENGTH_SHORT).show();
                    }else if (txt_password.length() < 6 ){
                        Toast.makeText(requireContext(), "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    } else if (!txt_password.contentEquals(txt_repassword)){
                        Toast.makeText(requireContext(), "Mật khẩu phải không khớp", Toast.LENGTH_SHORT).show();
                    } else {
                        register(txt_email, txt_password);
                    }
                }
            });

            return view;
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

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("users");
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("id", userid);
                            userMap.put("imageURL", "default");
                            userMap.put("status", "offline");
                            userMap.put("search", email);

                            usersRef.document(userid).set(userMap) .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                            .setCustomAnimations(
                                                    R.anim.slide_in_bottom, R.anim.slide_out_left
                                            )
                                            .replace(R.id.frame_layout, new LoginFragment())
                                            .commit();
                                        Toast.makeText(requireContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), "Bạn không thể đăng ký bằng email hoặc mật khẩu này", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
}