package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Log;

import java.util.Objects;

import vn.id.houta.myapplication.MainActivity;
import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.model.Lesson;
import vn.id.houta.myapplication.util.FeedbackUtils;


public class Module41VideoFragment extends Fragment {
    LinearLayout full_toolbar;
    ExoPlayer exoPlayer;
    ImageView bt_fullscreen;
    boolean isFullScreen=false;
    boolean isLock = false;
    TextView textViewSubTittleVideo;
    private Handler handler;
    View view;
    Lesson lesson;

    public Module41VideoFragment(Lesson lesson) {
        this.lesson = lesson;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_module4_1_video, container, false);
//        videoView = (VideoView) view.findViewById(R.id.videoView);
//        MediaController mediaController = new MediaController(this.getContext());
//        mediaController.setMediaPlayer(videoView);
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(Uri.parse("android.resource://"+this.getContext().getPackageName()+"/"+R.raw.videoso1));
//        videoView.start();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.release();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ((TextView) view.findViewById(R.id.textViewTittleVideo)).setText(this.lesson.getTitle());
        int startTimeMillis = this.lesson.getTimeStudy() * 1000;
        full_toolbar = view.findViewById(R.id.full_toolbar);
        handler  = new Handler(Looper.getMainLooper());

        PlayerView playerView = view.findViewById(R.id.player);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        bt_fullscreen = view.findViewById(R.id.bt_fullscreen);
        ImageView bt_lockscreen = view.findViewById(R.id.exo_lock);
        textViewSubTittleVideo = view.findViewById(R.id.textViewSubTittleVideo);
        textViewSubTittleVideo.setVisibility(View.GONE);

        bt_fullscreen.setOnClickListener(view ->
        {
            if (!isFullScreen)
            {
                bt_fullscreen.setImageDrawable(
                        ContextCompat
                                .getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen_exit));
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                textViewSubTittleVideo.setVisibility(View.VISIBLE);
                ((MainActivity)requireActivity()).hideBottomNavigationBarAndStatus();
                full_toolbar.setVisibility(View.GONE);
            } else
            {
                bt_fullscreen.setImageDrawable(ContextCompat
                        .getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen));
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
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_lock));
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } else
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_outline_lock_open));
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            isLock = !isLock;
            lockScreen(isLock);
        });

        //instance the player with skip back duration 5 second or forward 5 second
        //5000 millisecond = 5 second
        exoPlayer = new ExoPlayer.Builder(this.getContext())
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

                if(!exoPlayer.getPlayWhenReady())
                {
                    handler.removeCallbacks(updateProgressAction);
                }
                else
                {
                    onProgress();
                }
            }
        });

//        Uri videoUrl = Uri.parse("android.resource://"+this.getContext().getPackageName()+"/"+R.raw.videoso1);
        Uri videoUrl = Uri.parse("https://v1.yt-cdn.xyz/sv1/api/v1/download/?dm=y2meta.net&id=RFXrPwsV1kM&t=720p");

        MediaItem media = MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(media);
        exoPlayer.prepare();
        final boolean[] isFirstTimeReady = {true};

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                // Kiểm tra khi nào video có thể phát
                switch (state) {
                    case Player.STATE_READY:
                        // Trạng thái STATE_READY chỉ ra rằng video đã sẵn sàng để phát
                        Log.d("ExoPlayer", "Video có thể phát");
                        if (startTimeMillis > 0 & isFirstTimeReady[0]) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Tiếp tục học")
                                    .setMessage("Bạn có muốn xem tiếp tục từ " + lesson.getTimeStudyStr() + " ?")
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            exoPlayer.seekTo(0);
                                            exoPlayer.play();
                                        }
                                    })
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            exoPlayer.seekTo(startTimeMillis);
                                            exoPlayer.play();
                                        }
                                    }).create().show();
                        }else{
                            exoPlayer.play();
                        }
                        isFirstTimeReady[0] = false;
                        break;
                    case Player.STATE_BUFFERING:
                        // Trạng thái STATE_BUFFERING chỉ ra rằng video đang được tải hoặc chuẩn bị để phát
                        Log.d("ExoPlayer", "Video đang được tải hoặc chuẩn bị để phát");
                        break;
                    case Player.STATE_IDLE:
                    case Player.STATE_ENDED:
                        // Trạng thái STATE_IDLE và STATE_ENDED chỉ ra rằng video không thể phát
                        Log.d("ExoPlayer", "Video không thể phát");
                        break;
                }
            }
        });

//        if (startTimeMillis > 0)
//            new AlertDialog.Builder(this.getContext())
//                .setTitle("Tiếp tục học")
//                .setMessage("Bạn có muốn xem tiếp tục từ " + this.lesson.getTimeStudyStr()+ " ?")
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        exoPlayer.play();
//                    }
//                })
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        exoPlayer.seekTo(startTimeMillis);
//                        exoPlayer.play();
//                    }
//                }).create().show();
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                {
//                    bt_fullscreen.performClick();
//                }
//                exoPlayer.release();
//                requireActivity().getSupportFragmentManager().popBackStack();
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        view.findViewById(R.id.btn_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackUtils.showFeebackVideoAlert(getContext());
            }
        });
        return view;
    }

    private OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            System.out.println("Call back from video player");
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                bt_fullscreen.performClick();
            }else{
                exoPlayer.release();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
    }

    private Runnable updateProgressAction = () -> onProgress();
    boolean check = false;
    private void onProgress()
    {
        ExoPlayer player= exoPlayer;
        long position = player == null? 0 : player.getCurrentPosition();
        handler.removeCallbacks(updateProgressAction);
        int playbackState = player ==null? Player.STATE_IDLE : player.getPlaybackState();
        if(playbackState != Player.STATE_IDLE && playbackState!= Player.STATE_ENDED)
        {
            long delayMs ;
            if(player.getPlayWhenReady() && playbackState == Player.STATE_READY)
            {
                delayMs  = 1000 - position % 1000;
                if(delayMs < 200)
                {
                    delayMs+=1000;
                }
            }
            else{
                delayMs = 1000;
            }
        }
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

    public void continueBackPressed(){
        onBackPressedCallback.setEnabled(true);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
    }

    public void stopVideo(){
        System.out.println("stopVideo");
        exoPlayer.stop();
        onBackPressedCallback.setEnabled(false);
        onBackPressedCallback.remove();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    // pause or release the player prevent memory leak
    @Override
    public void onStop()
    {
        super.onStop();
        onBackPressedCallback.setEnabled(false);
        exoPlayer.stop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        exoPlayer.release();
        handler.removeCallbacksAndMessages(null);
        onBackPressedCallback.setEnabled(false);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        exoPlayer.pause();
        handler.removeCallbacksAndMessages(null); // Hủy bỏ tất cả các tin nhắn và ràng buộc
        onBackPressedCallback.setEnabled(false);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null); // Hủy bỏ tất cả các tin nhắn và ràng buộc
        onBackPressedCallback.setEnabled(false);
    }

}