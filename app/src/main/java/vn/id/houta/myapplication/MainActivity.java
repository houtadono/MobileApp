package vn.id.houta.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.id.houta.myapplication.databinding.ActivityMainBinding;
import vn.id.houta.myapplication.module4.Module41VideoFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int count_fragment_home = 0;
    private Fragment fragmentHome, activeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
                    else{
//                        getSupportFragmentManager().popBackStack();
//                        getSupportFragmentManager().popBackStack();
//                        replaceFragment(fragmentHome);
                        binding.bottomNavigationView.setSelectedItemId(R.id.home);
                    }
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
                                        MainActivity.super.getOnBackPressedDispatcher();
                                    }
                                }).create().show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

//    public void backToHome(){
//        binding.bottomNavigationView.setSelectedItemId(R.id.home);
////        replaceFragment(fragmentHome);
//    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(activeFragment);
//        if (!fragment.isAdded()) {
//            fragmentTransaction.add(R.id.frame_layout, fragment);
//            fragmentTransaction.addToBackStack(null);
//        } else {
//            fragmentTransaction.show(fragment);
//        }
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof Module41VideoFragment) {
            getSupportFragmentManager().popBackStack();
//            ((Module4Submain1VideoFragment) currentFragment).stopVideo();
        }else{
            fragmentTransaction.hide(activeFragment);
        }
//        if (!(fragment instanceof HomeFragment)) {
//            fragmentTransaction.addToBackStack(null);
//        }
//        if (activeFragment instanceof Module4Submain1VideoFragment) {
//            ((Module4Submain1VideoFragment) activeFragment).stopVideo();
//        }
//        if( fragment instanceof HomeFragment ){
//            fragmentTransaction.replace(R.id.frame_layout, fragment);
//        }
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
    public void hideBottomNavigationBarAndStatus() {
        binding.bottomNavigationView.setVisibility(View.GONE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    public void showBottomNavigationBarAndStatus() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}