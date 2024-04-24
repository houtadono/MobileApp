package vn.id.houta.myapplication;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.id.houta.myapplication.databinding.ActivityMainBinding;
import vn.id.houta.myapplication.module4.Module41VideoFragment;
import vn.id.houta.myapplication.module4.Module4Fragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int count_fragment_home = 0;
    private Fragment fragmentHome, activeFragment;
    CardView bottom_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fragmentHome = new HomeFragment();
        activeFragment = fragmentHome;

        bottom_layout = findViewById(R.id.bottom_layout);
        bottom_layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        final int[] iconIds = {R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5};
        final int[] cardIds = {R.id.card_item_nav1, R.id.card_item_nav2, R.id.card_item_nav3, R.id.card_item_nav4, R.id.card_item_nav5};
        final int[] titleIds = {R.id.title1, R.id.title2, R.id.title3, R.id.title4, R.id.title5};

        for (int i = 0; i < iconIds.length; i++) {
            final int finalI = i;
            final ImageButton icon = findViewById(iconIds[i]);
            icon.setOnClickListener(v -> {
                for(int j = 0; j< iconIds.length ; j++) {
                    ((CardView)findViewById(cardIds[j])).setCardBackgroundColor(getResources().getColor(R.color.white));
//                    findViewById(titleIds[j]).setVisibility(View.GONE);
                    View t = findViewById(titleIds[j]);
                    if (t.getVisibility() == View.VISIBLE) animateTitle(t);
                    findViewById(iconIds[j]).setSelected(false);
                }
                ((CardView)findViewById(cardIds[finalI])).setCardBackgroundColor(getResources().getColor(R.color.light_blue));
//                findViewById(titleIds[finalI]).setVisibility(View.VISIBLE);
                animateTitle(findViewById(titleIds[finalI]));
                icon.setSelected(true);

                switch (finalI){
                    case 0:
                        replaceFragment(fragmentHome);
                        break;
                    case 2:
                        replaceFragment(new Module4Fragment());
                        break;
                    case 4:
                        replaceFragment(new SettingFragment());
                        break;
                }
            });
        }
//        binding.bottomNavigationView.setBackground(null);
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.home){
//                replaceFragment(fragmentHome);
//                setMenuItemText(item, "Home");
//            }else if (item.getItemId() == R.id.note){
//                replaceFragment(fragmentHome);
//                setMenuItemText(item, "Note");
//            } if (item.getItemId() == R.id.setting){
//                replaceFragment(new SettingFragment());
//                setMenuItemText(item, "Setting");
//            }
//            return true;
//        });

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
//                        binding.bottomNavigationView.setSelectedItemId(R.id.home);
                        findViewById(iconIds[0]).performClick();
                    }
                }else{
                    if(num > 0)
                        getSupportFragmentManager().popBackStack();
                    else
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Xác nhận thoát")
                                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> MainActivity.super.getOnBackPressedDispatcher()).create().show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        findViewById(iconIds[0]).performClick();
    }

    private void animateTitle(View titleView) {
        int v = (titleView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(100);
        TransitionManager.beginDelayedTransition(bottom_layout, autoTransition);
        titleView.setVisibility(v);
    }


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
        }else if(currentFragment != null){
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
//        binding.bottomNavigationView.setVisibility(View.GONE);
        bottom_layout.setVisibility(View.GONE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    public void showBottomNavigationBarAndStatus() {
        bottom_layout.setVisibility(View.VISIBLE);
//        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}