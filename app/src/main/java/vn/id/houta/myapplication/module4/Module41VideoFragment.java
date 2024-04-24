package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.Objects;

import vn.id.houta.myapplication.MainActivity;
import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Lesson;
import vn.id.houta.myapplication.util.DialogUtils;


public class Module41VideoFragment extends Fragment {
    LinearLayout full_toolbar;
    ExoPlayer exoPlayer;
    ImageView bt_fullscreen;
    boolean isFullScreen=false;
    boolean isLock = false;
    TextView textViewSubTittleVideo;
    private Handler handler;
    int save_time = 0;
    View view;
    Lesson lesson, nextLesson;
    ArrayList<Lesson> listLesson;
    FirebaseHelper fb;
    private long[] customMarker = {10 * 1000};
    private long startTimeMillis;

    public Module41VideoFragment(Lesson lesson, ArrayList<Lesson> listLesson) {
        this.lesson = lesson;
        this.listLesson = listLesson;
        if (listLesson.size() >= lesson.getStt())
            this.nextLesson = listLesson.get(lesson.getStt());
        startTimeMillis= System.currentTimeMillis();
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                                      Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_module4_1_video, container, false);
        fb = new FirebaseHelper();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("call01");
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    bt_fullscreen.performClick();
                }else{
                    requireActivity().getSupportFragmentManager().popBackStack();
                    System.out.println("call02");
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        toolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        ((TextView) view.findViewById(R.id.textViewTittleVideo)).setText(this.lesson.getTitle());
        int startTimeMillis = this.lesson.getUserLesson().getTimesStudy() * 1000;
        save_time = startTimeMillis/1000;
        full_toolbar = view.findViewById(R.id.full_toolbar);

        PlayerView playerView = view.findViewById(R.id.player);
        customMarker[0] = (long)(lesson.getTimesTotal() * 0.7 *1000);
        playerView.setExtraAdGroupMarkers(customMarker,
                new boolean[]{false});
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    long currentTime = exoPlayer.getCurrentPosition()/1000;
                    if (currentTime == customMarker[0]/1000) {
                        customMarker[0] -= 2000;
                        exoPlayer.pause();
                        System.out.println("Showwww");
                        final boolean[] setIsLearned = {lesson.getUserLesson().isLearned()};
                        System.out.println(setIsLearned[0]);
                        DialogUtils.showQuestionVideoAlert(requireContext(),
                                Integer.valueOf(lesson.getName().charAt(lesson.getName().length()-1)-'0'), lesson,
                                () -> {
                                    exoPlayer.play();
                                    System.out.println(setIsLearned[0] + " "+lesson.getUserLesson().isLearned());
                                    if(!setIsLearned[0] && lesson.getUserLesson().isLearned()) {
                                        Module41Fragment.count_learned += 1;
                                        fb.setLearnedLessonForUser(lesson.getLessonId(), true);
                                    }
                                }
                        );
                    }
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        bt_fullscreen = view.findViewById(R.id.bt_fullscreen);
        ImageView bt_lockscreen = view.findViewById(R.id.exo_lock);
        textViewSubTittleVideo = view.findViewById(R.id.textViewSubTittleVideo);
        textViewSubTittleVideo.setText(this.lesson.getTitle());
        textViewSubTittleVideo.setVisibility(View.GONE);

        bt_fullscreen.setOnClickListener(view ->
        {
            if (!isFullScreen)
            {
                bt_fullscreen.setImageDrawable(
                        ContextCompat
                                .getDrawable(requireContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen_exit));
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                textViewSubTittleVideo.setVisibility(View.VISIBLE);
                ((MainActivity)requireActivity()).hideBottomNavigationBarAndStatus();
                full_toolbar.setVisibility(View.GONE);
            } else
            {
                bt_fullscreen.setImageDrawable(ContextCompat
                        .getDrawable(requireContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen));
               requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                textViewSubTittleVideo.setVisibility(View.GONE);
                ((MainActivity)requireActivity()).showBottomNavigationBarAndStatus();
                full_toolbar.setVisibility(View.VISIBLE);
            }
            isFullScreen = !isFullScreen;
        });

        bt_lockscreen.setOnClickListener(view ->
        {
            if (!isLock)
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.ic_baseline_lock));
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } else
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.ic_outline_lock_open));
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            isLock = !isLock;
            lockScreen(isLock);
        });

        //instance the player with skip back duration 5 second or forward 5 second
        //5000 millisecond = 5 second

        exoPlayer = new ExoPlayer.Builder(requireContext())
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(5000)
                .build();
        playerView.setPlayer(exoPlayer);
        //screen alway active
        playerView.setKeepScreenOn(true);

        //track state
        exoPlayer.addListener(new Player.Listener()
        {
            @Override
            public void onPlaybackStateChanged(int playbackState)
            {
                //when data video fetch stream from internet
                if (playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {
                    //then if streamed is loaded we hide the progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

//        Uri videoUrl = Uri.parse("android.resource://"+requireContext().getPackageName()+"/"+R.raw.videoso1);
        Uri videoUrl = Uri.parse(this.lesson.getLink());

        MediaItem media = MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(media);
        exoPlayer.prepare();
        final boolean[] isFirstTimeReady = {true};

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                switch (state) {
                    case Player.STATE_READY:
                        if (startTimeMillis > 0 & isFirstTimeReady[0]) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Tiếp tục học")
                                    .setMessage("Bạn có muốn xem tiếp tục từ " + lesson.getTimeStudyStr() + " ?")
                                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                                        exoPlayer.seekTo(0);
                                        exoPlayer.play();
                                    })
                                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                                        exoPlayer.seekTo(startTimeMillis);
                                        exoPlayer.play();
                                    }).create().show();
                        }else{
                            exoPlayer.play();
                        }
                        save_time = (int) exoPlayer.getCurrentPosition() / 1000;
                        isFirstTimeReady[0] = false;
                        break;
                    case Player.STATE_BUFFERING:
                        Log.d("ExoPlayer", "Video đang được tải hoặc chuẩn bị để phát state 2");
                        break;
                    case Player.STATE_IDLE:
                        save_time = (int) exoPlayer.getCurrentPosition() / 1000;
                    case Player.STATE_ENDED:
                        save_time = 0;
                        Log.d("ExoPlayer", "Video không thể phát / kết thúc 4");
                        break;
                }
            }
        });

        // Heart
        LikeButton btn_heart = view.findViewById(R.id.heart_button);
        btn_heart.setLiked(this.lesson.getUserLesson().isLiked());
        btn_heart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                lesson.getUserLesson().setLiked(true);
                new FirebaseHelper().setHeartLessonUser(lesson.getLessonId(), true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                lesson.getUserLesson().setLiked(false);
                new FirebaseHelper().setHeartLessonUser(lesson.getLessonId(), false);
            }
        });

        TextView textView_heartCount = view.findViewById(R.id.textViewHeartCount);
        textView_heartCount.setText(String.valueOf(this.lesson.getLikeCount()));
        fb.getHeartCountLesson(c->{
            textView_heartCount.setText(String.valueOf(c));
        }, this.lesson.getLessonId());

        // Feedback
        view.findViewById(R.id.btn_feedback).setOnClickListener(v -> {
            exoPlayer.pause();
            DialogUtils.showFeebackVideoAlert(getContext(), () -> exoPlayer.play());
        });

        if(this.nextLesson != null){
            ((TextView)view.findViewById(R.id.textViewNextTittleVideo)).setText(this.nextLesson.getTitle());
            view.findViewById(R.id.btn_next_lesson).setOnClickListener(v -> {
                onBackPressedCallback.handleOnBackPressed();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment nextFragment = new Module41VideoFragment(this.nextLesson, listLesson);
                fragmentTransaction.add(R.id.frame_layout, nextFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }else{
            view.findViewById(R.id.card_next_lesson).setVisibility(View.GONE);
        }
        return view;
    }

    private void showDialogForAdMarker(int index) {
        // Code để hiển thị dialog ở đây
        // Ví dụ:
        System.out.println("SHowoo");
//        Toast.makeText(MainActivity.this, "Đã đến đánh dấu quảng cáo thứ " + (index + 1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
    }

    void lockScreen(boolean lock)
    {
        //just hide the control for lock screen and vise versa
        LinearLayout sec_mid = view.findViewById(R.id.sec_controlvid1);
        LinearLayout sec_bottom = view.findViewById(R.id.sec_controlvid2);
        if(lock)
        {
            sec_mid.setVisibility(View.INVISIBLE);
            sec_bottom.setVisibility(View.INVISIBLE);
        }
        else
        {
            sec_mid.setVisibility(View.VISIBLE);
            sec_bottom.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStop()
    {
        super.onStop();
        System.out.println("state: "+ exoPlayer.getPlaybackState());
//        if (exoPlayer.getPlaybackState() == Player.STATE_BUFFERING){
//            return;
//        }
//        int timesStudy = (int) exoPlayer.getCurrentPosition() / 1000;
        long viewDurationMillis = System.currentTimeMillis() - startTimeMillis;

        lesson.getUserLesson().setTimesStudy(save_time);
        fb.increaseTimeLearnToday((int)viewDurationMillis/1000);
        fb.saveTimeStudyLessonForUser(lesson.getLessonId(), save_time);
        exoPlayer.stop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        exoPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        exoPlayer.pause();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}