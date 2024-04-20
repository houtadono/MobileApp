package vn.id.houta.myapplication.module4;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import vn.id.houta.myapplication.R;


public class Module4Fragment extends Fragment {
    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView select;
    private ViewPager2 viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_module4, container, false);
        item1 = view.findViewById(R.id.item1);
        item2 = view.findViewById(R.id.item2);
        item3 = view.findViewById(R.id.item3);

        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(new FragmentStateAdapter(requireActivity()) {
            private static final int NUM_PAGES = 3;
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new Module43Fragment();
                    case 1:
                        return new Module42Fragment();
                    case 2:
                        return new Module41Fragment();
                    default:
                        return null;
                }
            }
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
        });
        select = view.findViewById(R.id.select);
        item1.setOnClickListener(v -> {
            viewPager.setCurrentItem(0);
            item1.setSelected(true);
            item2.setSelected(false);
            item3.setSelected(false);
            select.animate().x(0).setDuration(100);
        });

        item2.setOnClickListener(v -> {
            viewPager.setCurrentItem(1);
            item1.setSelected(false);
            item2.setSelected(true);
            item3.setSelected(false);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
        });

        item3.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
            item1.setSelected(false);
            item2.setSelected(false);
            item3.setSelected(true);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
        });
        item1.performClick();
        return view;
    }
}