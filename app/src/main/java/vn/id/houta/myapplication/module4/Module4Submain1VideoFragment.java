package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import vn.id.houta.myapplication.R;


public class Module4Submain1VideoFragment extends Fragment {
    VideoView videoView;
    ExoPlayer exoPlayer;
    ImageView bt_fullscreen;
    boolean isFullScreen=false;
    boolean isLock = false;
    Handler handler;
    View view;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_module4_submain1_video, container, false);
//        videoView = (VideoView) view.findViewById(R.id.videoView);
//        MediaController mediaController = new MediaController(this.getContext());
//        mediaController.setMediaPlayer(videoView);
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(Uri.parse("android.resource://"+this.getContext().getPackageName()+"/"+R.raw.videoso1));
//        videoView.start();

        handler = new Handler(Looper.getMainLooper());

        PlayerView playerView = view.findViewById(R.id.player);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        bt_fullscreen = view.findViewById(R.id.bt_fullscreen);
        ImageView bt_lockscreen = view.findViewById(R.id.exo_lock);

        bt_fullscreen.setOnClickListener(view ->
        {
            if (!isFullScreen)
            {
                bt_fullscreen.setImageDrawable(
                        ContextCompat
                                .getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen_exit));
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else
            {
                bt_fullscreen.setImageDrawable(ContextCompat
                        .getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_fullscreen));
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            isFullScreen = !isFullScreen;
        });

        bt_lockscreen.setOnClickListener(view ->
        {
            if (!isLock)
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_baseline_lock));
            } else
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.ic_outline_lock_open));
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

        Uri videoUrl = Uri.parse("android.resource://"+this.getContext().getPackageName()+"/"+R.raw.videoso1);
        MediaItem media = MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(media);
        exoPlayer.prepare();
        exoPlayer.play();

        return view;
    }
    private Runnable updateProgressAction = () -> onProgress();

    //at 4 second
    long ad = 4000;
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

    //when is in lock screen we not accept for backpress button
//    @Override
//    public void onBackPressed()
//    {
//        //on lock screen back press button not work
//        if(isLock) return;
//
//        //if user is in landscape mode we turn to portriat mode first then we can exit the app.
//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            bt_fullscreen.performClick();
//        }
//        else super.onBackPressed();
//    }

    // pause or release the player prevent memory leak
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        exoPlayer.stop();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        exoPlayer.release();
//    }
//
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        exoPlayer.pause();
//    }
}