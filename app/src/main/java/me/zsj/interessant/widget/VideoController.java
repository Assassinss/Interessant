package me.zsj.interessant.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import me.zsj.interessant.R;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.utils.AnimUtils;
import me.zsj.interessant.utils.ScreenUtils;
import me.zsj.interessant.utils.TimeUtils;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author zsj
 */

public class VideoController extends FrameLayout
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private ImageButton pause;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private FrameLayout centerLayout;
    private TextView currentTime;
    private TextView endTime;
    private TextView progressTime;
    private FrameLayout mediaController;
    private Toolbar toolbar;

    private IjkMediaPlayer mediaPlayer;
    private Context context;

    private ItemList item;
    private Handler handler = new Handler();
    private int mVideoWidth;
    private int mVideoHeight;
    private int maxProgress;
    private boolean isVideoSizeKnown = false;
    private boolean isVideoReadyToBePlayed = false;
    private boolean showSystemUi;

    public VideoController(Context context) {
        this(context, null);
    }

    public VideoController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOnTouchListener(this);
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        pause = (ImageButton) findViewById(R.id.pause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        centerLayout = (FrameLayout) findViewById(R.id.center_layout);
        currentTime = (TextView) findViewById(R.id.current_time);
        endTime = (TextView) findViewById(R.id.end_time);
        progressTime = (TextView) findViewById(R.id.progress_time);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mediaController = (FrameLayout) findViewById(R.id.media_controller);

        pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    public void attachPlayer(IjkMediaPlayer mediaPlayer, ItemList item) {
        this.mediaPlayer = mediaPlayer;
        this.item = item;
        hideSystemUI();
    }

    public void startVideoPlayback(SurfaceHolder surfaceHolder) {
        handler.post(progressCallback);
        mediaPlayer.setDisplay(surfaceHolder);
        surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mediaPlayer.start();
    }

    public void onPrepared(SurfaceHolder surfaceHolder) {
        isVideoReadyToBePlayed = true;
        if (isVideoSizeKnown) {
            startVideoPlayback(surfaceHolder);
        }
    }

    public void surfaceCreated() {
        maxProgress = (int) item.data.duration;
        seekBar.setMax(maxProgress);
        seekBar.setProgress(0);
        endTime.setText(TimeUtils.secToTime((int) item.data.duration));
        mVideoWidth = ScreenUtils.getWidth(context);
        mVideoHeight = ScreenUtils.getHeight(context);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void surfaceDestory() {
        handler.removeCallbacks(progressCallback);
    }

    public void calculateVideoWidthAndHeight() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) centerLayout.getLayoutParams();
        lp.width = mVideoWidth;
        lp.height = mVideoHeight;
        centerLayout.setLayoutParams(lp);
    }

    public void onCompleted() {
        handler.removeCallbacks(progressCallback);
        pause.setSelected(true);
    }

    public void onVideoChanged(SurfaceHolder surfaceHolder, int width, int height) {
        float ratio = (float) width / (float) height;
        mVideoHeight = (int) (mVideoWidth / ratio);
        isVideoSizeKnown = true;
        if (isVideoReadyToBePlayed) {
            calculateVideoWidthAndHeight();
            startVideoPlayback(surfaceHolder);
        }
    }

    public void onBufferUpdate(int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    public void onInfo(int what) {
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
    }

    @Override
    public void onClick(View v) {
        if (mediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    public void start() {
        mediaPlayer.start();
        handler.post(progressCallback);
        pause.setSelected(false);
    }

    public void pause() {
        mediaPlayer.pause();
        pause.setSelected(true);
    }

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

    private void seekTo(long mesc) {
        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
        mediaPlayer.seekTo(mesc);
        pause.setSelected(false);
        handler.post(progressCallback);
    }

    private void hideSystemUI() {
        toolbar.setAlpha(0);
        AnimUtils.animOut(mediaController);
        showSystemUi = false;
    }

    private void showSystemUI() {
        toolbar.setAlpha(1f);
        AnimUtils.animIn(mediaController);
        showSystemUi = true;

        mediaController.postDelayed(this::hideSystemUI, 3000);
    }

    private float touchX;
    private int currentProgress;

    @SuppressLint("StringFormatMatches")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                currentProgress = seekBar.getProgress();
                if (showSystemUi) hideSystemUI();
                else showSystemUI();
                break;
            case MotionEvent.ACTION_MOVE:
                if (seekBar.getProgress() <= 0 ||
                        seekBar.getProgress() >= maxProgress) break;

                float moveX = event.getX();
                float distance = moveX - touchX;
                if (distance >= 100) {
                    hideSystemUI();
                }

                int moveProgress = (int) (distance / 70);
                long mesc = (currentProgress + moveProgress) * 1000;
                if (Math.abs(moveProgress) > 0) {
                    seekTo(mesc);
                    progressTime.setVisibility(VISIBLE);
                    progressTime.setText(
                            getResources().getString(
                                    R.string.progress_time_text,
                                    TimeUtils.secToTime(currentProgress + moveProgress),
                                    TimeUtils.secToTime(maxProgress)));
                }
                break;
            case MotionEvent.ACTION_UP:
                progressTime.setVisibility(GONE);
                break;
        }
        return true;
    }

}
