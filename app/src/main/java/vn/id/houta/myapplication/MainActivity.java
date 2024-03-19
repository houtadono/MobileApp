package vn.id.houta.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.id.houta.myapplication.databinding.ActivityMainBinding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int count_fragment_home = 0;

    private Fragment fragmentHome, activeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentHome = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentHome).commit();
        activeFragment = fragmentHome;

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                replaceFragment(fragmentHome);
            }else if (item.getItemId() == R.id.note){
                replaceFragment(fragmentHome);
            } if (item.getItemId() == R.id.setting){
                replaceFragment(new SettingFragment());
            }
            return true;
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int num = getSupportFragmentManager().getBackStackEntryCount();
                if( !(activeFragment instanceof HomeFragment) ){
                    if(num > count_fragment_home)
                        getSupportFragmentManager().popBackStack();
                    else
                        binding.bottomNavigationView.setSelectedItemId(R.id.home);
                }else{
                    if(num > 0)
                        getSupportFragmentManager().popBackStack();
                    else
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Xác nhận thoát")
                                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // Gọi phương thức onBackPressed() của lớp cha để thoát ứng dụng
                                        MainActivity.super.onBackPressed();
                                    }
                                }).create().show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        if (!(fragment instanceof HomeFragment)) {
//            fragmentTransaction.addToBackStack(null);
//        }
        fragmentTransaction.hide(activeFragment);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        if( activeFragment instanceof HomeFragment ){
            count_fragment_home = getSupportFragmentManager().getBackStackEntryCount();
            System.out.println(count_fragment_home);
        }
        fragmentTransaction.commit();
        activeFragment = fragment;
    }

}