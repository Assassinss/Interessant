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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import me.zsj.interessant.base.ToolbarActivity;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.utils.AnimUtils;
import me.zsj.interessant.utils.ScreenUtils;
import me.zsj.interessant.utils.TimeUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author zsj
 */

public class PlayActivity extends ToolbarActivity
        implements SurfaceHolder.Callback, IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, View.OnClickListener {

    private SurfaceHolder surfaceHolder;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView endTime;
    private FrameLayout mediaController;
    private ImageButton pause;
    private ProgressBar progressBar;
    private FrameLayout centerLayout;

    private IjkMediaPlayer mediaPlayer;

    private Handler handler = new Handler();

    private ItemList item;
    private int mVideoWidth;
    private int mVideoHeight;
    private int maxProgress;
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
        centerLayout = (FrameLayout) findViewById(R.id.center_layout);
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
        maxProgress = (int) item.data.duration;

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);

        playMovie(playUrl);
    }

    private void playMovie(String path) {
        try {
            mediaPlayer = new IjkMediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer.setScreenOnWhilePlaying(true);
            startVideoPlayback();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable progressCallback = new Runnable() {
        @Override
        public void run() {
            if (isVideoReadyToBePlayed && isVideoSizeKnown) {
                int progress = TimeUtils.millsToSec((int) mediaPlayer.getCurrentPosition());
                currentTime.setText(TimeUtils.secToTime(progress));
                seekBar.setProgress(progress);

                handler.postDelayed(this, 1000);
            }
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
                }
            };

    private void seekTo(long mesc) {
        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
        mediaPlayer.seekTo(mesc);
        pause.setSelected(false);
        handler.post(progressCallback);
    }

    private boolean surfaceCreated;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!surfaceCreated) {
            seekBar.setMax(maxProgress);
            seekBar.setProgress(0);
            endTime.setText(TimeUtils.secToTime((int) item.data.duration));
            mVideoWidth = ScreenUtils.getWidth(this);
            mVideoHeight = ScreenUtils.getHeight(this);
            progressBar.setVisibility(View.VISIBLE);
            surfaceCreated = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        calculateVideoWidthAndHeight();
        startVideoPlayback();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        handler.removeCallbacks(progressCallback);
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer imp, int percent) {
        if (percent >= 95) percent = 100;
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                mediaPlayer.start();
                progressBar.setVisibility(View.GONE);
                break;
            case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
            case IjkMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                progressBar.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    @Override
    public void onCompletion(IMediaPlayer imp) {
        handler.removeCallbacks(progressCallback);
        pause.setSelected(true);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        isVideoReadyToBePlayed = true;
        if (isVideoReadyToBePlayed && isVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    @Override
    public void onClick(View v) {
        if (mediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    private void start() {
        mediaPlayer.start();
        handler.post(progressCallback);
        pause.setSelected(false);
    }

    private void pause() {
        mediaPlayer.pause();
        pause.setSelected(true);
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer,
                                   int width, int height, int sar_num, int sar_den) {
        float ratio = (float) width / (float) height;
        mVideoHeight = (int) (mVideoWidth / ratio);
        isVideoSizeKnown = true;
        if (isVideoReadyToBePlayed && isVideoSizeKnown) {
            calculateVideoWidthAndHeight();
            startVideoPlayback();
        }
    }

    private void calculateVideoWidthAndHeight() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) centerLayout.getLayoutParams();
        lp.width = mVideoWidth;
        lp.height = mVideoHeight;
        centerLayout.setLayoutParams(lp);
    }

    private void startVideoPlayback() {
        handler.post(progressCallback);
        mediaPlayer.setDisplay(surfaceHolder);
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
        showSystemUi = false;
    }

    private void showSystemUI() {
        toolbar.setAlpha(1f);
        AnimUtils.animIn(mediaController);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_SHOW_SYSTEM_UI);
        showSystemUi = true;

        mediaController.postDelayed(this::hideSystemUI, 3000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (showSystemUi) hideSystemUI();
            else showSystemUI();
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
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(progressCallback);
        release();
    }

}
