package me.zsj.interessant;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import me.zsj.interessant.base.ToolbarActivity;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.utils.AnimUtils;
import me.zsj.interessant.utils.ScreenUtils;
import me.zsj.interessant.utils.TimeUtils;

/**
 * Created by zsj on 2016/10/27.
 */

public class PlayActivity extends ToolbarActivity
        implements SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener, View.OnClickListener, MediaPlayer.OnInfoListener {

    private SurfaceHolder surfaceHolder;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView endTime;
    private FrameLayout mediaController;
    private ImageButton pause;
    private ProgressBar progressBar;

    private MediaPlayer mediaPlayer;

    private Handler handler = new Handler();

    private ItemList item;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean isVideoSizeKnown = false;
    private boolean isVideoReadyToBePlayed = false;
    private boolean showSystemUi;


    @Override
    public int providerLayoutId() {
        return R.layout.play_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!LibsChecker.checkVitamioLibs(this)) return;

        mediaController = (FrameLayout) findViewById(R.id.media_controller);
        pause = (ImageButton) findViewById(R.id.pause);
        pause.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        currentTime = (TextView) findViewById(R.id.current_time);
        endTime = (TextView) findViewById(R.id.end_time);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        hideSystemUI();

        item = getIntent().getParcelableExtra("item");
        String playUrl = item.data.playUrl;

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);

        playMovie(playUrl);
    }

    private void playMovie(String path) {
        try {
            mediaPlayer = new MediaPlayer(this);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            startVideoPlayback();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable progressCallback = new Runnable() {
        @Override
        public void run() {
            if (!mediaPlayer.isPlaying()) return;

            int progress = TimeUtils.millsToSec((int) mediaPlayer.getCurrentPosition());
            currentTime.setText(TimeUtils.secToTime(progress));
            seekBar.setProgress(progress);

            handler.postDelayed(this, 1000);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        handler.post(progressCallback);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(progressCallback);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekTo(seekBar.getProgress() * 1000);
                    if (mediaPlayer.isPlaying()) {
                        handler.removeCallbacks(progressCallback);
                        handler.post(progressCallback);
                    }
                }
            };

    private void seekTo(long mesc) {
        mediaPlayer.seekTo(mesc);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        seekBar.setMax((int) item.data.duration);
        seekBar.setProgress(0);
        endTime.setText(TimeUtils.secToTime((int) item.data.duration));
        mVideoWidth = ScreenUtils.getWidth(this);
        mVideoHeight = ScreenUtils.getHeight(this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        handler.removeCallbacks(progressCallback);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mediaPlayer.start();
                progressBar.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.removeCallbacks(progressCallback);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isVideoReadyToBePlayed = true;
        if (isVideoReadyToBePlayed && isVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    @Override
    public void onClick(View v) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pause.setSelected(true);
        } else {
            mediaPlayer.start();
            pause.setSelected(false);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        isVideoSizeKnown = true;
        if (isVideoReadyToBePlayed && isVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    private void startVideoPlayback() {
        handler.post(progressCallback);
        surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mediaPlayer.start();
    }

    private static final int FLAG_HIDE_SYSTEM_UI = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE;

    private static final int FLAG_SHOW_SYSTEM_UI = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private void hideSystemUI() {
        toolbar.setAlpha(0);
        AnimUtils.animOut(mediaController);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_HIDE_SYSTEM_UI);
    }

    private void showSystemUI() {
        toolbar.setAlpha(1f);
        AnimUtils.animIn(mediaController);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_SHOW_SYSTEM_UI);
        mediaController.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSystemUI();
            }
        }, 3000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (showSystemUi) {
                hideSystemUI();
                showSystemUi = false;
            } else {
                showSystemUI();
                showSystemUi = true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mVideoWidth = 0;
            mVideoHeight = 0;
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(progressCallback);
        release();
    }

}
