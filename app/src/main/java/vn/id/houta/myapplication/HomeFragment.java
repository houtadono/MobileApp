package vn.id.houta.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.module4.Module4Fragment;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String timeOfDay;
        if (hourOfDay < 12) {
            timeOfDay = "buổi sáng";
        } else if (hourOfDay < 18) {
            timeOfDay = "buổi chiều";
        } else {
            timeOfDay = "buổi tối";
        }

        String name = new FirebaseHelper().getNameCurrentUser();
        String greeting = "Chào "+timeOfDay+", "+ name;
        ((TextView)view.findViewById(R.id.textViewGreeting)).setText(greeting);

        view.findViewById(R.id.btn_get_started).setOnClickListener(v -> {
            requireActivity().findViewById(R.id.icon3).performClick();
        });
        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        Glide.with(requireContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .circleCrop().into((ImageView) view.findViewById(R.id.avatarUser));

        view.findViewById(R.id.module4).setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(HomeFragment.this);
            fragmentTransaction.add(R.id.frame_layout, new Module4Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }
}